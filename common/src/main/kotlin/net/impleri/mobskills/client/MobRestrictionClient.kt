package net.impleri.mobskills.client

import net.impleri.mobskills.api.MobRestriction
import net.impleri.mobskills.restrictions.Restriction
import net.impleri.playerskills.client.RestrictionsClient
import net.impleri.playerskills.restrictions.Registry
import net.minecraft.world.entity.EntityType

class MobRestrictionClient(
  registry: Registry<Restriction>,
  serverApi: MobRestriction,
) : RestrictionsClient<EntityType<*>, Restriction, MobRestriction>(registry, serverApi) {
  internal fun isUsable(entity: EntityType<*>): Boolean {
    return player?.let { serverApi.isUsable(it, entity) } ?: true
  }

  companion object {
    private val INSTANCE = MobRestrictionClient(MobRestriction.RestrictionRegistry, MobRestriction.INSTANCE)

    fun isUsable(entity: EntityType<*>): Boolean {
      return INSTANCE.isUsable(entity)
    }
  }
}
