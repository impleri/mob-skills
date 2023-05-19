package net.impleri.mobskills.restrictions

import net.impleri.mobskills.api.EntitySpawnMode
import net.impleri.playerskills.restrictions.AbstractRestriction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.player.Player

open class Restriction(
  entity: EntityType<*>,
  condition: (Player) -> Boolean,
  includeDimensions: List<ResourceLocation>?,
  excludeDimensions: List<ResourceLocation>?,
  includeBiomes: List<ResourceLocation>?,
  excludeBiomes: List<ResourceLocation>?,
  spawnMode: EntitySpawnMode?,
  usable: Boolean?,
  includeSpawners: List<MobSpawnType>?,
  excludeSpawners: List<MobSpawnType>?,
  replacement: EntityType<*>?,
) : AbstractRestriction<EntityType<*>>(
  entity,
  condition,
  includeDimensions ?: ArrayList(),
  excludeDimensions ?: ArrayList(),
  includeBiomes ?: ArrayList(),
  excludeBiomes ?: ArrayList(),
  replacement,
) {
  val spawnMode = spawnMode ?: EntitySpawnMode.ALLOW_ALWAYS
  val usable = usable ?: false
  val includeSpawners = includeSpawners ?: ArrayList()
  val excludeSpawners = excludeSpawners ?: ArrayList()
}
