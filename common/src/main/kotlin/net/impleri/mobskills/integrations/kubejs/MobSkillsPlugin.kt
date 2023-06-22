package net.impleri.mobskills.integrations.kubejs

import dev.architectury.event.events.common.LifecycleEvent
import dev.latvian.mods.kubejs.KubeJSPlugin
import net.impleri.mobskills.integrations.kubejs.events.EventsBinding
import net.impleri.mobskills.integrations.kubejs.events.RestrictionsRegistrationEventJS
import net.minecraft.server.MinecraftServer

class MobSkillsPlugin : KubeJSPlugin() {
  init {
    LifecycleEvent.SERVER_STARTING.register(
      LifecycleEvent.ServerState { onStartup(it) },
    )
  }

  override fun registerEvents() {
    EventsBinding.GROUP.register()
  }

  companion object {
    fun onStartup(server: MinecraftServer) {
      EventsBinding.RESTRICTIONS.post(
        RestrictionsRegistrationEventJS(server),
      )
    }
  }
}
