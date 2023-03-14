package net.impleri.mobskills.forge;

import dev.architectury.platform.forge.EventBuses;
import net.impleri.mobskills.MobSkills;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MobSkills.MOD_ID)
public class MobSkillsForge {
    public MobSkillsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(MobSkills.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        MobSkills.init();
    }
}
