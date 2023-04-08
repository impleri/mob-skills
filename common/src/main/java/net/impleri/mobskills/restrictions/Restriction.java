package net.impleri.mobskills.restrictions;

import net.impleri.playerskills.restrictions.AbstractRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Restriction extends AbstractRestriction<EntityType<?>> {
    public final EntitySpawnMode spawnMode;

    public final boolean usable;

    public final List<MobSpawnType> includeSpawners;

    public final List<MobSpawnType> excludeSpawners;

    public Restriction(
            EntityType entity,
            Predicate<Player> condition,
            @Nullable List<ResourceLocation> includeDimensions,
            @Nullable List<ResourceLocation> excludeDimensions,
            @Nullable List<ResourceLocation> includeBiomes,
            @Nullable List<ResourceLocation> excludeBiomes,
            @Nullable EntitySpawnMode spawnMode,
            @Nullable Boolean usable,
            @Nullable List<MobSpawnType> includeSpawners,
            @Nullable List<MobSpawnType> excludeSpawners,
            @Nullable EntityType replacement
    ) {
        super(entity, condition, includeDimensions, excludeDimensions, includeBiomes, excludeBiomes, replacement);
        this.spawnMode = (spawnMode != null) ? spawnMode : EntitySpawnMode.ALLOW_ALWAYS;
        this.usable = Boolean.TRUE.equals(usable);
        this.includeSpawners = (includeSpawners == null) ? new ArrayList<>() : includeSpawners;
        this.excludeSpawners = (excludeSpawners == null) ? new ArrayList<>() : excludeSpawners;
    }
}
