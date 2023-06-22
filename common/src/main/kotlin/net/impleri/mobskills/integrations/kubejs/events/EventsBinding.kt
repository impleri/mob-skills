package net.impleri.mobskills.integrations.kubejs.events

import dev.latvian.mods.kubejs.event.EventGroup
import dev.latvian.mods.kubejs.event.EventHandler

object EventsBinding {
  val GROUP: EventGroup = EventGroup.of("MobSkillEvents")
  val RESTRICTIONS: EventHandler = GROUP.server("register") { RestrictionsRegistrationEventJS::class.java }
}
