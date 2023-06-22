package net.impleri.mobskills.fabric

import net.fabricmc.api.ModInitializer
import net.impleri.mobskills.MobSkills

class MobSkillsFabric : ModInitializer {
  override fun onInitialize() {
    MobSkills.init()
  }
}
