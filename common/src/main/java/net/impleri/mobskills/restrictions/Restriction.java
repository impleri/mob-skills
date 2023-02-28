package net.impleri.mobskills.restrictions;

import net.impleri.playerskills.restrictions.AbstractRestriction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class Restriction extends AbstractRestriction<EntityType<?>> {
    public final EntitySpawnMode spawnMode;

    public Restriction(
            EntityType entity,
            Predicate<Player> condition,
            @Nullable EntityType replacement,
            @Nullable EntitySpawnMode spawnMode
    ) {
        super(entity, condition, replacement);
        this.spawnMode = (spawnMode != null) ? spawnMode : EntitySpawnMode.ALLOW_ALWAYS;
    }

    public Restriction(
            EntityType entity,
            Predicate<Player> condition,
            @Nullable EntityType replacement
    ) {
        this(entity, condition, replacement, null);
    }

    public Restriction(
            EntityType entity,
            Predicate<Player> condition,
            @Nullable EntitySpawnMode spawnMode
    ) {
        this(entity, condition, null, spawnMode);
    }
}
