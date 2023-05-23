package net.impleri.mobskills.integrations.crafttweaker

import com.blamejared.crafttweaker.api.annotation.ZenRegister
import net.impleri.mobskills.MobSkills
import net.impleri.mobskills.api.RestrictionBuilder
import org.openzen.zencode.java.ZenCodeType

@ZenRegister
@ZenCodeType.Name("mods.mobskills.MobRestrictions")
object MobRestrictions {
  @ZenCodeType.Method
  @JvmStatic
  fun create(name: String): RestrictionConditionsBuilder {
    MobSkills.LOGGER.info("Starting to create restriction for $name")
    return RestrictionConditionsBuilder(MobSkills.INSTANCE.server) {
      MobSkills.LOGGER.info("Registering restriction for $name")
      RestrictionBuilder.register(name, it)
    }
  }
}
