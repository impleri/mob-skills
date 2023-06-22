package net.impleri.mobskills.integrations.kubejs.events

import dev.latvian.mods.rhino.util.HideFromJS
import net.impleri.mobskills.api.EntitySpawnMode
import net.impleri.mobskills.restrictions.MobConditions
import net.impleri.mobskills.restrictions.Restriction
import net.impleri.playerskills.integrations.kubejs.api.AbstractRestrictionConditionsBuilder
import net.impleri.playerskills.integrations.kubejs.api.PlayerDataJS
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import java.util.function.Predicate

class RestrictionConditionsBuilderJS @HideFromJS constructor(
  server: Lazy<MinecraftServer>,
) : AbstractRestrictionConditionsBuilder<EntityType<*>, Restriction>(server), MobConditions<PlayerDataJS> {
  @HideFromJS
  override var replacement: EntityType<*>? = null

  @HideFromJS
  override var spawnMode: EntitySpawnMode = EntitySpawnMode.ALLOW_ALWAYS

  @HideFromJS
  override var usable: Boolean? = true

  @HideFromJS
  override var includeSpawners: MutableList<MobSpawnType> = ArrayList()

  @HideFromJS
  override var excludeSpawners: MutableList<MobSpawnType> = ArrayList()

  override fun unless(predicate: Predicate<PlayerDataJS>): RestrictionConditionsBuilderJS {
    super<MobConditions>.unless(predicate)

    return this
  }

  fun spawnable(): RestrictionConditionsBuilderJS {
    spawnable(null)

    return this
  }

  fun unspawnable(): RestrictionConditionsBuilderJS {
    unspawnable(null)

    return this
  }

  @HideFromJS
  override fun fromSpawner(spawner: MobSpawnType): RestrictionConditionsBuilderJS {
    return super.fromSpawner(spawner) as RestrictionConditionsBuilderJS
  }

  @HideFromJS
  override fun notFromSpawner(spawner: MobSpawnType): RestrictionConditionsBuilderJS {
    return super.notFromSpawner(spawner) as RestrictionConditionsBuilderJS
  }
}
