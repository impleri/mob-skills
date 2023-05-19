package net.impleri.mobskills.integrations.kubejs.events

import dev.latvian.mods.kubejs.server.ServerEventJS
import net.impleri.mobskills.api.RestrictionBuilder
import net.minecraft.server.MinecraftServer

class RestrictionsRegistrationEventJS(s: MinecraftServer) : ServerEventJS(s) {
  fun restrict(name: String, consumer: (RestrictionConditionsBuilder) -> Unit) {
    val builder = RestrictionConditionsBuilder(server)
    consumer(builder)

    RestrictionBuilder.register(name, builder)
  }
}
