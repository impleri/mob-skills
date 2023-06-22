package net.impleri.mobskills.integrations.kubejs.events

import dev.latvian.mods.kubejs.server.ServerEventJS
import net.impleri.mobskills.MobSkills
import net.impleri.mobskills.api.RestrictionBuilder
import net.minecraft.server.MinecraftServer

class RestrictionsRegistrationEventJS(s: MinecraftServer) : ServerEventJS(s) {
  fun restrict(name: String, consumer: (RestrictionConditionsBuilderJS) -> Unit) {
    val builder = RestrictionConditionsBuilderJS(MobSkills.server)
    consumer(builder)

    RestrictionBuilder.register(name, builder)
  }
}
