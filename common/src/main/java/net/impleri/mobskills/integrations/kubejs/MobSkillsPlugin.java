package net.impleri.mobskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class MobSkillsPlugin extends KubeJSPlugin {
    private final MobSkillsKubeJSWrapper skillWrapper = new MobSkillsKubeJSWrapper();

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("MobSkills", skillWrapper);
    }
}
