package net.impleri.mobskills.restrictions;

import net.impleri.mobskills.MobHelper;
import net.impleri.mobskills.MobSkills;
import net.impleri.playerskills.restrictions.RestrictionsApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class Restrictions extends RestrictionsApi<EntityType<?>, Restriction> {
    private static final Field[] allRestrictionFields = Restriction.class.getDeclaredFields();

    public static final Restrictions INSTANCE = new Restrictions(MobSkills.RESTRICTIONS, allRestrictionFields);

    private Restrictions(net.impleri.playerskills.restrictions.Registry<Restriction> registry, Field[] fields) {
        super(registry, fields, MobSkills.LOGGER);
    }

    @Override
    protected ResourceLocation getTargetName(EntityType<?> target) {
        return MobHelper.getEntityKey(target);
    }

    @Override
    protected Predicate<EntityType<?>> createPredicateFor(EntityType<?> type) {
        return type::equals;
    }

    public boolean isUsable(Player player, EntityType<?> entity) {
        var level = player.getLevel();
        var dimension = level.dimension().location();
        var biome = level.getBiome(player.getOnPos()).unwrapKey().orElseThrow().location();
        return canPlayer(player, entity, dimension, biome, "usable");
    }

    private final HashMap<EntityType<?>, List<Restriction>> entityRestrictionsCache = new HashMap<>();

    private List<Restriction> populateEntityRestrictions(EntityType<?> entity) {
        var isTargetingEntity = createPredicateFor(entity);

        return registry.entries().stream()
                .filter(restriction -> isTargetingEntity.test(restriction.target))
                .toList();
    }

    private List<Restriction> getRestrictionsFor(EntityType<?> entity) {
        return entityRestrictionsCache.computeIfAbsent(entity, this::populateEntityRestrictions);
    }

    public boolean canSpawnAround(EntityType<?> entity, List<Player> players, ResourceLocation dimension, ResourceLocation biome, MobSpawnType spawnType) {
        var isTargetingNearbyPlayers = createExtraFilter(players);

        var denials = getRestrictionsFor(entity).stream()
                .filter(inIncludedDimension(dimension))
                .filter(notInExcludedDimension(dimension))
                .filter(inIncludedBiome(biome))
                .filter(notInExcludedBiome(biome))
                .filter(inIncludedSpawner(spawnType))
                .filter(notInExcludedSpawner(spawnType))
                .filter(isTargetingNearbyPlayers)
                .count();

        return denials == 0;
    }

    protected Predicate<Restriction> inIncludedSpawner(MobSpawnType spawnType) {
        return restriction -> restriction.includeSpawners.size() == 0 || restriction.includeSpawners.contains(spawnType);
    }

    protected Predicate<Restriction> notInExcludedSpawner(MobSpawnType spawnType) {
        return restriction -> restriction.excludeSpawners.size() == 0 || !restriction.excludeSpawners.contains(spawnType);
    }

    private Predicate<Restriction> createExtraFilter(List<Player> players) {
        return restriction -> !doesRestrictionAllowSpawn(restriction, players);
    }


    private static boolean doesRestrictionAllowSpawn(Restriction restriction, List<Player> players) {
        return switch (restriction.spawnMode) {
            case ALLOW_IF_ANY_MATCH, DENY_UNLESS_ANY_MATCH -> ifAny(players, restriction.condition);
            case ALLOW_IF_ALL_MATCH, DENY_UNLESS_ALL_MATCH -> ifAll(players, restriction.condition);
            case ALLOW_UNLESS_ANY_MATCH, DENY_IF_ANY_MATCH -> !ifAll(players, restriction.condition);
            case ALLOW_UNLESS_ALL_MATCH, DENY_IF_ALL_MATCH -> !ifAny(players, restriction.condition);
            case ALLOW_ALWAYS -> true;
            case DENY_ALWAYS -> false;
        };
    }

    private static boolean ifAny(List<Player> players, Predicate<Player> consumer) {
        return players.stream().anyMatch(consumer);
    }

    private static boolean ifAll(List<Player> players, Predicate<Player> consumer) {
        return players.stream().allMatch(consumer);
    }

}
