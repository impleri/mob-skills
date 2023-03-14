package net.impleri.mobskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public abstract class EventsBinding {
    public static final EventGroup GROUP = EventGroup.of("MobSkillEvents");

    public static final EventHandler RESTRICTIONS = GROUP.server("register", () -> RestrictionsRegistrationEventJS.class);
}
