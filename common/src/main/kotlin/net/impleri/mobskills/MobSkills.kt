package net.impleri.mobskills

import com.mojang.brigadier.CommandDispatcher
import dev.architectury.event.EventResult
import dev.architectury.event.events.common.CommandRegistrationEvent
import dev.architectury.event.events.common.EntityEvent
import dev.architectury.event.events.common.InteractionEvent
import dev.architectury.event.events.common.LifecycleEvent
import net.impleri.mobskills.api.MobRestriction
import net.impleri.playerskills.commands.PlayerSkillsCommands.registerDebug
import net.impleri.playerskills.commands.PlayerSkillsCommands.toggleDebug
import net.impleri.playerskills.utils.PlayerSkillsLogger
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.Vec3i
import net.minecraft.server.MinecraftServer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BaseSpawner
import net.minecraft.world.level.LevelAccessor

class MobSkills {
  private var savedServer: MinecraftServer? = null

  internal val server: MinecraftServer by lazy {
    savedServer ?: throw RuntimeException("Unable to access the server before it is available")
  }

  private fun registerEventHandlers() {
    LifecycleEvent.SERVER_STARTING.register(
      LifecycleEvent.ServerState { onStartup(it) },
    )

    EntityEvent.LIVING_CHECK_SPAWN.register(
      EntityEvent.LivingCheckSpawn { livingEntity: LivingEntity, levelAccessor: LevelAccessor, x: Double, y: Double, z: Double, mobSpawnType: MobSpawnType, _: BaseSpawner? ->
        onCheckSpawn(
          livingEntity,
          levelAccessor,
          x,
          y,
          z,
          mobSpawnType,
        )
      },
    )

    InteractionEvent.INTERACT_ENTITY.register(
      InteractionEvent.InteractEntity { player: Player, entity: Entity, _: InteractionHand ->
        onInteract(
          player,
          entity,
        )
      },
    )
  }

  private fun registerCommands() {
    CommandRegistrationEvent.EVENT.register(
      CommandRegistrationEvent { dispatcher: CommandDispatcher<CommandSourceStack>, _: CommandBuildContext, _: Commands.CommandSelection ->
        registerDebugCommand(dispatcher)
      },
    )
  }

  private fun registerDebugCommand(
    dispatcher: CommandDispatcher<CommandSourceStack>,
  ) {
    registerDebug(dispatcher, "mobskills", toggleDebug("Mob Skills", MobSkills::toggleDebug))
  }

  private fun onStartup(minecraftServer: MinecraftServer) {
    savedServer = minecraftServer
  }

  private fun onInteract(player: Player, entity: Entity): EventResult {
    if (MobRestriction.canInteractWith(entity.type, player)) {
      return EventResult.pass()
    }
    LOGGER.debug("Preventing ${player.name} from interacting with ${MobRestriction.getName(entity.type)}")
    return EventResult.interruptFalse()
  }

  private fun onCheckSpawn(
    livingEntity: LivingEntity,
    levelAccessor: LevelAccessor,
    x: Double,
    y: Double,
    z: Double,
    mobSpawnType: MobSpawnType,
  ): EventResult {
    val pos = Vec3i(x, y, z)
    if (MobRestriction.canSpawn(livingEntity, levelAccessor, pos, mobSpawnType)) {
      return EventResult.pass()
    }
    LOGGER.debug("Preventing ${MobRestriction.getName(livingEntity.type)} from spawning at ${pos.toShortString()}")
    return EventResult.interruptFalse()
  }

  companion object {
    const val MOD_ID = "mobskills"

    val LOGGER: PlayerSkillsLogger = PlayerSkillsLogger.create(MOD_ID, "MOBS")

    private val INSTANCE = MobSkills()

    internal val server: Lazy<MinecraftServer> = lazy { INSTANCE.server }

    fun init() {
      LOGGER.info("Loaded Mob Skills")
      INSTANCE.registerEventHandlers()
      INSTANCE.registerCommands()
    }

    fun toggleDebug(): Boolean {
      return LOGGER.toggleDebug()
    }
  }
}
