package net.impleri.mobskills.integrations.kubejs.events

import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes
import dev.latvian.mods.rhino.util.HideFromJS
import net.impleri.mobskills.api.EntitySpawnMode
import net.impleri.mobskills.restrictions.MobConditions
import net.impleri.mobskills.restrictions.Restriction
import net.impleri.playerskills.integrations.kubejs.api.AbstractRestrictionConditionsBuilder
import net.impleri.playerskills.integrations.kubejs.api.PlayerDataJS
import net.impleri.playerskills.utils.SkillResourceLocation
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType

class RestrictionConditionsBuilder @HideFromJS constructor(
  server: MinecraftServer,
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

  override fun unless(predicate: (PlayerDataJS) -> Boolean): RestrictionConditionsBuilder {
    return super<MobConditions>.unless(predicate) as RestrictionConditionsBuilder
  }

  @HideFromJS
  override fun getRegistryType(): RegistryObjectBuilderTypes<Restriction> {
    return registry
  }

  companion object {
    private val key =
      ResourceKey.createRegistryKey<Restriction>(SkillResourceLocation.of("mob_restriction_builders_registry"))

    val registry: RegistryObjectBuilderTypes<Restriction> = RegistryObjectBuilderTypes.add(key, Restriction::class.java)
  }
}
