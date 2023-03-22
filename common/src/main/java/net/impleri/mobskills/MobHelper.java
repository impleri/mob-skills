package net.impleri.mobskills;

import net.impleri.mobskills.restrictions.Restrictions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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

    public static boolean canSpawn(LivingEntity entity, LevelAccessor levelAccessor, Vec3i position) {
        var type = entity.getType();
        var spawnRadius = type.getCategory().getDespawnDistance();

        var dimension = entity.getLevel().dimension().location();
        var biome = levelAccessor.getBiome(new BlockPos(position)).unwrapKey().orElseThrow().location();

        var playersInRange = getNearbyPlayers((List<Player>) levelAccessor.players(), position, spawnRadius);

        MobSkills.LOGGER.debug("Found {} players within {} blocks of {}", playersInRange.size(), spawnRadius, position.toShortString());

        return Restrictions.INSTANCE.canSpawnAround(type, playersInRange, dimension, biome);
    }
}
