package net.impleri.mobskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.impleri.mobskills.integrations.kubejs.events.EventsBinding;
import net.impleri.mobskills.integrations.kubejs.events.RestrictionsRegistrationEventJS;
import net.minecraft.server.MinecraftServer;

public class MobSkillsPlugin extends KubeJSPlugin {
    public static void onStartup(MinecraftServer server) {
        new RestrictionsRegistrationEventJS(server).post(ScriptType.SERVER, EventsBinding.RESTRICTIONS);
    }
}
