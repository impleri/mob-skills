package net.impleri.mobskills.integrations.crafttweaker

import com.blamejared.crafttweaker.api.annotation.ZenRegister
import net.impleri.mobskills.MobSkills
import net.impleri.mobskills.api.MobRestriction
import net.impleri.mobskills.api.RestrictionBuilder
import net.minecraft.world.entity.EntityType
import org.openzen.zencode.java.ZenCodeType

@ZenRegister
@ZenCodeType.Name("mods.mobskills.MobRestrictions")
object MobRestrictions {
  @ZenCodeType.Method
  @JvmStatic
  fun create(name: String): RestrictionConditionsBuilder {
    return RestrictionConditionsBuilder(MobSkills.server) {
      MobSkills.LOGGER.debug("Registering restriction for $name")
      RestrictionBuilder.register(name, it)
    }
  }

  @ZenCodeType.Method
  @JvmStatic
  fun create(entityType: EntityType<*>): RestrictionConditionsBuilder {
    return create(MobRestriction.getName(entityType).toString())
  }
}
