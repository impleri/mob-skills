package net.impleri.mobskills.integrations.crafttweaker

import com.blamejared.crafttweaker.api.annotation.ZenRegister
import net.impleri.mobskills.MobSkills
import net.impleri.mobskills.api.RestrictionBuilder
import org.openzen.zencode.java.ZenCodeType

@ZenRegister
@ZenCodeType.Name("mods.mobskills.MobSkills")
object MobSkills {
  @ZenCodeType.Method
  @JvmStatic
  fun restrict(name: String): RestrictionConditionsBuilder {
    return RestrictionConditionsBuilder(MobSkills.INSTANCE.server) {
      RestrictionBuilder.register(name, it)
      true
    }
  }
}
