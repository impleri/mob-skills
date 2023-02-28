package net.impleri.mobskills;

import net.impleri.playerskills.utils.PlayerSkillsLogger;

public class MobSkills {
    public static final String MOD_ID = "mobskills";
    public static final PlayerSkillsLogger LOGGER = PlayerSkillsLogger.create(MOD_ID, "MOBS");

    private static final MobEvents INSTANCE = new MobEvents();

    public static void init() {
        LOGGER.info("Loaded Mob Skills");
        INSTANCE.registerEventHandlers();
    }
}
