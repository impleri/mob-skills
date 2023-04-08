package net.impleri.mobskills;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import net.impleri.playerskills.commands.PlayerSkillsCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class MobEvents {
    public void registerEventHandlers() {
        LifecycleEvent.SERVER_STARTING.register(this::onStartup);

        EntityEvent.LIVING_CHECK_SPAWN.register(this::onCheckSpawn);

        InteractionEvent.INTERACT_ENTITY.register(this::onInteract);
    }

    public void registerCommands() {
        CommandRegistrationEvent.EVENT.register(this::registerDebugCommand);
    }

    private void registerDebugCommand(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection) {
        PlayerSkillsCommands.registerDebug(dispatcher, "mobskills", PlayerSkillsCommands.toggleDebug("Mob Skills", MobSkills::toggleDebug));
    }

    private void onStartup(MinecraftServer minecraftServer) {
        if (Platform.isModLoaded("kubejs")) {
            net.impleri.mobskills.integrations.kubejs.MobSkillsPlugin.onStartup(minecraftServer);
        }
    }

    private EventResult onInteract(Player player, Entity entity, InteractionHand interactionHand) {
        if (MobHelper.canInteractWith(player, entity.getType())) {
            return EventResult.pass();
        }

        MobSkills.LOGGER.debug("Preventing {} from interacting with {}", player.getName().getString(), MobHelper.getEntityKey(entity.getType()));

        return EventResult.interruptFalse();
    }

    private EventResult onCheckSpawn(LivingEntity livingEntity, LevelAccessor levelAccessor, double x, double y, double z, MobSpawnType mobSpawnType, @Nullable BaseSpawner baseSpawner) {
        var pos = new Vec3i(x, y, z);

        if (MobHelper.canSpawn(livingEntity, levelAccessor, pos, mobSpawnType)) {
            return EventResult.pass();
        }

        MobSkills.LOGGER.debug("Preventing {} from spawning at {}", MobHelper.getEntityKey(livingEntity.getType()), pos.toShortString());

        return EventResult.interruptFalse();
    }
}
