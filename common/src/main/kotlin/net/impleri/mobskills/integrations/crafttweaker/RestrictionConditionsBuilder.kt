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
import java.util.function.Predicate

@ZenRegister
@ZenCodeType.Name("mods.mobskills.RestrictionConditionsBuilder")
class RestrictionConditionsBuilder(
  server: Lazy<MinecraftServer>,
  private val onSave: (RestrictionConditionsBuilder) -> Unit,
) : AbstractRestrictionConditionsBuilder<EntityType<*>, Restriction>(server), MobConditions<Player> {
  override var replacement: EntityType<*>? = null
  override var spawnMode: EntitySpawnMode = EntitySpawnMode.ALLOW_ALWAYS
  override var usable: Boolean? = true
  override var includeSpawners: MutableList<MobSpawnType> = ArrayList()
  override var excludeSpawners: MutableList<MobSpawnType> = ArrayList()

  @ZenCodeType.Method
  fun save() {
    onSave(this)
  }

  @ZenCodeType.Method
  override fun unless(predicate: Predicate<Player>): RestrictionConditionsBuilder {
    super<MobConditions>.unless(predicate)

    return this
  }

  @ZenCodeType.Method
  override fun always(): RestrictionConditionsBuilder {
    super.always()

    return this
  }

  @ZenCodeType.Method
  fun spawnable(): RestrictionConditionsBuilder {
    return spawnable(null)
  }

  @ZenCodeType.Method
  override fun spawnable(requireAll: Boolean?): RestrictionConditionsBuilder {
    super.spawnable(requireAll)

    return this
  }

  @ZenCodeType.Method
  fun unspawnable(): RestrictionConditionsBuilder {
    return unspawnable(null)
  }

  @ZenCodeType.Method
  override fun unspawnable(requireAll: Boolean?): RestrictionConditionsBuilder {
    super.unspawnable(requireAll)

    return this
  }

  @ZenCodeType.Method
  override fun fromSpawner(spawner: String): RestrictionConditionsBuilder {
    super.fromSpawner(spawner)

    return this
  }

  @ZenCodeType.Method
  override fun notFromSpawner(spawner: String): RestrictionConditionsBuilder {
    super.notFromSpawner(spawner)

    return this
  }

  @ZenCodeType.Method
  override fun usable(): RestrictionConditionsBuilder {
    super.usable()

    return this
  }

  @ZenCodeType.Method
  override fun unusable(): RestrictionConditionsBuilder {
    super.unusable()

    return this
  }

  @ZenCodeType.Method
  override fun nothing(): RestrictionConditionsBuilder {
    super.nothing()

    return this
  }

  @ZenCodeType.Method
  override fun everything(): RestrictionConditionsBuilder {
    super.everything()

    return this
  }
}
