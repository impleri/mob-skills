package net.impleri.mobskills.restrictions;

import net.impleri.playerskills.restrictions.AbstractRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class Restriction extends AbstractRestriction<EntityType<?>> {
    public final EntitySpawnMode spawnMode;

    public final boolean usable;

    public Restriction(
            EntityType entity,
            Predicate<Player> condition,
            @Nullable List<ResourceLocation> includeDimensions,
            @Nullable List<ResourceLocation> excludeDimensions,
            @Nullable List<ResourceLocation> includeBiomes,
            @Nullable List<ResourceLocation> excludeBiomes,
            @Nullable EntitySpawnMode spawnMode,
            @Nullable Boolean usable,
            @Nullable EntityType replacement
    ) {
        super(entity, condition, includeDimensions, excludeDimensions, includeBiomes, excludeBiomes, replacement);
        this.spawnMode = (spawnMode != null) ? spawnMode : EntitySpawnMode.ALLOW_ALWAYS;
        this.usable = Boolean.TRUE.equals(usable);
    }
}
