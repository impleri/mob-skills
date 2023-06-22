package net.impleri.mobskills.integrations.crafttweaker

import com.blamejared.crafttweaker.api.annotation.ZenRegister
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration
import net.impleri.mobskills.restrictions.MobConditions

@ZenRegister
@NativeTypeRegistration(
  value = MobConditions::class,
  zenCodeName = "mods.mobskills.MobConditions",
)
class ZenMobConditions
