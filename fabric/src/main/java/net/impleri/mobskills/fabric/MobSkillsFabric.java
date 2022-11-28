package net.impleri.mobskills.fabric;

import net.fabricmc.api.ModInitializer;
import net.impleri.mobskills.MobSkills;

public class MobSkillsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MobSkills.init();
    }
}
