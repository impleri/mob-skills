package net.impleri.mobskills;

import net.impleri.playerskills.PlayerSkillsLogger;

public class MobSkills {
    public static final String MOD_ID = "mobskills";
    public static final PlayerSkillsLogger LOGGER = PlayerSkillsLogger.create(MOD_ID, "PS-MOB");

    public static void init() {
        LOGGER.info("Loaded Mob Skills");
    }
}
