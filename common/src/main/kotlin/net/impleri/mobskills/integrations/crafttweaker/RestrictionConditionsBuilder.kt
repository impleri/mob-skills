package net.impleri.mobskills.integrations.crafttweaker

import com.blamejared.crafttweaker.api.annotation.ZenRegister
import net.impleri.mobskills.api.EntitySpawnMode
import net.impleri.mobskills.restrictions.MobConditions
import net.impleri.mobskills.restrictions.Restriction
import net.impleri.playerskills.integrations.crafttweaker.AbstractRestrictionConditionsBuilder
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.player.Player
import org.openzen.zencode.java.ZenCodeType

@ZenRegister
@ZenCodeType.Name("mods.mobskills.RestrictionConditionsBuilder")
class RestrictionConditionsBuilder(
  server: MinecraftServer,
  val onSave: (RestrictionConditionsBuilder) -> Boolean,
) : AbstractRestrictionConditionsBuilder<EntityType<*>, Restriction>(server), MobConditions<Player> {
  override var replacement: EntityType<*>? = null
  override var spawnMode: EntitySpawnMode = EntitySpawnMode.ALLOW_ALWAYS
  override var usable: Boolean? = true
  override var includeSpawners: MutableList<MobSpawnType> = ArrayList()
  override var excludeSpawners: MutableList<MobSpawnType> = ArrayList()

  @ZenCodeType.Method
  fun save(): Boolean {
    return onSave(this)
  }

  @ZenCodeType.Method
  override fun unless(predicate: (Player) -> Boolean): RestrictionConditionsBuilder {
    return super<MobConditions>.unless(predicate) as RestrictionConditionsBuilder
  }

  @ZenCodeType.Method
  override fun always(): RestrictionConditionsBuilder {
    return super.always() as RestrictionConditionsBuilder
  }

  @ZenCodeType.Method
  override fun spawnable(requireAll: Boolean?): RestrictionConditionsBuilder {
    return super.spawnable(requireAll) as RestrictionConditionsBuilder
  }

  @ZenCodeType.Method
  override fun unspawnable(requireAll: Boolean?): RestrictionConditionsBuilder {
    return super.unspawnable(requireAll) as RestrictionConditionsBuilder
  }

  @ZenCodeType.Method
  override fun fromSpawner(spawner: String): RestrictionConditionsBuilder {
    return super.fromSpawner(spawner) as RestrictionConditionsBuilder
  }

  @ZenCodeType.Method
  override fun notFromSpawner(spawner: String): RestrictionConditionsBuilder {
    return super.notFromSpawner(spawner) as RestrictionConditionsBuilder
  }

  @ZenCodeType.Method
  override fun usable(): RestrictionConditionsBuilder {
    return super.usable() as RestrictionConditionsBuilder
  }

  @ZenCodeType.Method
  override fun unusable(): RestrictionConditionsBuilder {
    return super.unusable() as RestrictionConditionsBuilder
  }

  @ZenCodeType.Method
  override fun nothing(): RestrictionConditionsBuilder {
    return super.nothing() as RestrictionConditionsBuilder
  }

  @ZenCodeType.Method
  override fun everything(): RestrictionConditionsBuilder {
    return super.everything() as RestrictionConditionsBuilder
  }
}
