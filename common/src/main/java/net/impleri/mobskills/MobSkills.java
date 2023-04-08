package net.impleri.mobskills;

import net.impleri.mobskills.restrictions.Restriction;
import net.impleri.playerskills.restrictions.Registry;
import net.impleri.playerskills.utils.PlayerSkillsLogger;

public class MobSkills {
    public static final String MOD_ID = "mobskills";

    public static final PlayerSkillsLogger LOGGER = PlayerSkillsLogger.create(MOD_ID, "MOBS");

    public static Registry<Restriction> RESTRICTIONS = new Registry<>(MOD_ID);

    private static final MobEvents INSTANCE = new MobEvents();

    public static void init() {
        LOGGER.info("Loaded Mob Skills");
        INSTANCE.registerEventHandlers();
        INSTANCE.registerCommands();
    }

    public static boolean toggleDebug() {
        return LOGGER.toggleDebug();
    }
}
