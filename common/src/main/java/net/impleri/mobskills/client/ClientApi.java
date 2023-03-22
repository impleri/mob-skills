package net.impleri.mobskills.client;

import net.impleri.mobskills.MobSkills;
import net.impleri.mobskills.restrictions.Restriction;
import net.impleri.mobskills.restrictions.Restrictions;
import net.impleri.playerskills.client.RestrictionsClient;
import net.minecraft.world.entity.EntityType;

public class ClientApi extends RestrictionsClient<EntityType<?>, Restriction, Restrictions> {
    public static final ClientApi INSTANCE = new ClientApi(MobSkills.RESTRICTIONS, Restrictions.INSTANCE);

    private ClientApi(net.impleri.playerskills.restrictions.Registry<Restriction> registry, Restrictions serverApi) {
        super(registry, serverApi);
    }

    public boolean isUsable(EntityType<?> entity) {
        return serverApi.isUsable(getPlayer(), entity);
    }
}
