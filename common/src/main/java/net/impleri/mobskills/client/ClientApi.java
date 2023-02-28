package net.impleri.mobskills.client;

import net.impleri.mobskills.api.Restrictions;
import net.impleri.mobskills.restrictions.Registry;
import net.impleri.mobskills.restrictions.Restriction;
import net.impleri.playerskills.api.RestrictionsApi;
import net.impleri.playerskills.client.RestrictionsClient;
import net.minecraft.world.entity.EntityType;

public class ClientApi extends RestrictionsClient<EntityType<?>, Restriction> {
    public static final ClientApi INSTANCE = new ClientApi(Registry.INSTANCE, Restrictions.INSTANCE);

    private ClientApi(net.impleri.playerskills.restrictions.Registry<Restriction> registry, RestrictionsApi<EntityType<?>, Restriction> serverApi) {
        super(registry, serverApi);
    }

    public boolean isUsable(EntityType<?> entity) {
        return canPlayer(entity, "usable");
    }
}
