package net.impleri.mobskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import net.impleri.mobskills.integrations.kubejs.events.EventsBinding;
import net.impleri.mobskills.integrations.kubejs.events.RestrictionsRegistrationEventJS;
import net.impleri.mobskills.restrictions.Registry;

public class MobSkillsPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        EventsBinding.GROUP.register();
    }

    public static void onStartup() {
        Registry.INSTANCE.clear();
        EventsBinding.RESTRICTIONS.post(new RestrictionsRegistrationEventJS());
    }
}
