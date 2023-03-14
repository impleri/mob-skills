package net.impleri.mobskills.api;

import net.impleri.mobskills.MobHelper;
import net.impleri.mobskills.MobSkills;
import net.impleri.mobskills.restrictions.EntitySpawnMode;
import net.impleri.mobskills.restrictions.Restriction;
import net.impleri.playerskills.api.RestrictionsApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;

public class Restrictions extends RestrictionsApi<EntityType<?>, Restriction> {
    private static final Field[] allRestrictionFields = Restriction.class.getDeclaredFields();

    public static final Restrictions INSTANCE = new Restrictions(MobSkills.RESTRICTIONS, allRestrictionFields);

    private Restrictions(net.impleri.playerskills.restrictions.Registry<Restriction> registry, Field[] fields) {
        super(registry, fields);
    }

    @Override
    protected ResourceLocation getTargetName(EntityType<?> target) {
        return MobHelper.getEntityKey(target);
    }

    @Override
    protected Predicate<EntityType<?>> createPredicateFor(EntityType<?> type) {
        return type::equals;
    }

    public List<Restriction> getSpawnRestrictionsFor(EntityType<?> entity) {
        var isRestrictionFor = createPredicateFor(entity);
        Predicate<Restriction> isTargetingEntitySpawn = restriction -> isRestrictionFor.test(restriction.target) && restriction.spawnMode != EntitySpawnMode.ALLOW_ALWAYS;

        return registry.entries().stream()
                .filter(isTargetingEntitySpawn)
                .toList();
    }

    public boolean isUsable(Player player, EntityType<?> entity) {
        return canPlayer(player, entity, "usable");
    }
}
