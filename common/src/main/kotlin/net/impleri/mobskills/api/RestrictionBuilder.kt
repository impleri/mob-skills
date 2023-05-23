package net.impleri.mobskills.api

import net.impleri.mobskills.MobSkills
import net.impleri.mobskills.restrictions.MobConditions
import net.impleri.mobskills.restrictions.Restriction
import net.impleri.playerskills.restrictions.AbstractRestrictionBuilder
import net.impleri.playerskills.restrictions.RestrictionConditionsBuilder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType

class RestrictionBuilder : AbstractRestrictionBuilder<EntityType<*>, Restriction>(
  Registry.ENTITY_TYPE,
  MobSkills.LOGGER,
) {
  override fun <Player> restrictOne(
    name: ResourceLocation,
    builder: RestrictionConditionsBuilder<EntityType<*>, Player, Restriction>,
  ) {
    builder as MobConditions<Player>

    val type = MobRestriction.getValue(name)

    if (type == null) {
      MobSkills.LOGGER.warn("Could not find any mob named $name")
      return
    }

    val restriction = Restriction(
      type,
      builder.actualCondition,
      builder.includeDimensions,
      builder.excludeDimensions,
      builder.includeBiomes,
      builder.excludeBiomes,
      builder.spawnMode,
      builder.usable,
      builder.includeSpawners,
      builder.excludeSpawners,
      builder.replacement,
    )

    MobRestriction.add(name, restriction)
    logRestriction(name, restriction)
  }

  override fun isTagged(tag: TagKey<EntityType<*>>): (EntityType<*>) -> Boolean {
    return { it.`is`(tag) }
  }

  override fun getName(resource: EntityType<*>): ResourceLocation {
    return MobRestriction.getName(resource)
  }

  companion object {
    private val instance = RestrictionBuilder()

    fun <Player> register(name: String, builder: MobConditions<Player>) {
      instance.create(name, builder)
    }
  }
}
