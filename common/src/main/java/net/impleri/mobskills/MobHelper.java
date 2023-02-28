package net.impleri.mobskills;

import net.impleri.mobskills.api.Restrictions;
import net.impleri.mobskills.restrictions.Restriction;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class MobHelper {
    public static ResourceLocation getEntityKey(EntityType<?> type) {
        return EntityType.getKey(type);
    }

    public static EntityType<?> getEntityType(ResourceLocation type) {
        var entityType = Registry.ENTITY_TYPE.get(type);

        // Currently, the default entity type is minecraft:pig, so we're making sure that the restriction actually targets
        // a pig before returning that value
        var foundType = getEntityKey(entityType);
        var isDefaultType = Registry.ENTITY_TYPE.getDefaultKey().equals(foundType);
        var isTargetingDefaultType = type.equals(foundType);

        return isDefaultType & !isTargetingDefaultType ? null : entityType;
    }

    private static List<Player> getNearbyPlayers(List<Player> playersInDim, Vec3i position, int radius) {
        return playersInDim.stream()
                .filter(player -> Math.sqrt(player.distanceToSqr(Vec3.atCenterOf(position))) <= radius)
                .toList();
    }

    public static boolean canInteractWith(Player player, EntityType<?> entity) {
        var usable = Restrictions.INSTANCE.isUsable(player, entity);

        MobSkills.LOGGER.debug("Can {} interact with {}? {}", player.getName().getString(), getEntityKey(entity), usable);

        return usable;
    }

    public static boolean canSpawn(EntityType<?> type, LevelAccessor levelAccessor, Vec3i position) {
        var spawnRadius = type.getCategory().getDespawnDistance();

        var playersInRange = getNearbyPlayers((List<Player>) levelAccessor.players(), position, spawnRadius);

        MobSkills.LOGGER.debug("Found {} players within {} blocks of {}", playersInRange.size(), spawnRadius, position.toShortString());

        var restrictions = Restrictions.INSTANCE.getSpawnRestrictionsFor(type);

        MobSkills.LOGGER.debug("Found {} restrictions for {} spawning", restrictions.size(), getEntityKey(type));

        var denials = restrictions.stream()
                .filter(restriction -> !doesRestrictionAllowSpawn(restriction, playersInRange))
                .count();

        return denials == 0;
    }

    private static boolean doesRestrictionAllowSpawn(Restriction restriction, List<Player> players) {
        return switch (restriction.spawnMode) {
            case ALLOW_IF_ANY_MATCH, DENY_UNLESS_ANY_MATCH -> ifAny(players, restriction.condition);
            case ALLOW_IF_ALL_MATCH, DENY_UNLESS_ALL_MATCH -> ifAll(players, restriction.condition);
            case ALLOW_UNLESS_ANY_MATCH, DENY_IF_ALL_MATCH -> !ifAll(players, restriction.condition);
            case ALLOW_UNLESS_ALL_MATCH, DENY_IF_ANY_MATCH -> !ifAny(players, restriction.condition);
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
