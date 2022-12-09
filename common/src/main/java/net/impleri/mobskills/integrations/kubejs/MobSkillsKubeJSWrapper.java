package net.impleri.mobskills.integrations.kubejs;

import dev.latvian.mods.kubejs.entity.CheckLivingEntitySpawnEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.mobskills.MobSkills;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class MobSkillsKubeJSWrapper {
    private static final double FISH_SPAWN_RANGE = 64;
    private static final double MOB_SPAWN_RANGE = 128;

    @HideFromJS
    private List<Player> getNearbyPlayers(CheckLivingEntitySpawnEventJS event) {
        BlockPos position = event.getBlock().getPos();
        List<Player> players = (List<Player>) event.getBlock().getLevel().players();

        double spawnRadius = event.getEntity().getType() == EntityType.TROPICAL_FISH ? FISH_SPAWN_RANGE : MOB_SPAWN_RANGE;

        List<Player> playersInRange = players.stream()
                .filter(player -> Math.sqrt(player.distanceToSqr(Vec3.atCenterOf(position))) < spawnRadius)
                .toList();

        MobSkills.LOGGER.debug("Found {} players within {} blocks of {}", playersInRange.size(), spawnRadius, position.toShortString());

        return playersInRange;
    }

    @HideFromJS
    private Predicate<Player> getMatcher(Predicate<PlayerSkillDataJS> consumer) {
        return player -> {
            var skills = new PlayerSkillDataJS(player);
            return consumer.test(skills);
        };
    }

    @HideFromJS
    private boolean matchAny(CheckLivingEntitySpawnEventJS event, Predicate<PlayerSkillDataJS> consumer) {
        return getNearbyPlayers(event).stream().anyMatch(getMatcher(consumer));
    }

    @HideFromJS
    private boolean matchAll(CheckLivingEntitySpawnEventJS event, Predicate<PlayerSkillDataJS> consumer) {
        return getNearbyPlayers(event).stream().allMatch(getMatcher(consumer));
    }

    /**
     * Allow the spawn if any player matches the predicate.
     * If 1 matches, yes === If none match, no
     */
    public void allowIfAny(CheckLivingEntitySpawnEventJS event, Predicate<PlayerSkillDataJS> consumer) {
        if (!matchAny(event, consumer)) {
            event.cancel();
        }
    }

    /**
     * Allow the spawn if all players match the predicate.
     * If all match, yes === if 1 doesn't match, no
     */
    public void allowIfAll(CheckLivingEntitySpawnEventJS event, Predicate<PlayerSkillDataJS> consumer) {
        if (!matchAll(event, consumer)) {
            event.cancel();
        }
    }

    /**
     * Allow the spawn if any player doesn't match the predicate.
     * if 1 doesn't match, yes === if all match, no
     */
    public void allowUnlessAny(CheckLivingEntitySpawnEventJS event, Predicate<PlayerSkillDataJS> consumer) {
        if (matchAll(event, consumer)) {
            event.cancel();
        }
    }

    /**
     * Allow the spawn if all players match the predicate.
     * if all don't match, yes === if 1 matches, no
     */
    public void allowUnlessAll(CheckLivingEntitySpawnEventJS event, Predicate<PlayerSkillDataJS> consumer) {
        if (matchAny(event, consumer)) {
            event.cancel();
        }
    }

    /**
     * Deny the spawn if any player match the predicate.
     * if 1 matches, no === If all doesn't match, yes
     */
    public void denyIfAny(CheckLivingEntitySpawnEventJS event, Predicate<PlayerSkillDataJS> consumer) {
        allowUnlessAll(event, consumer);
    }

    /**
     * Deny the spawn if all players match the predicate.
     * if all match, no === if 1 doesn't match, yes
     */
    public void denyIfAll(CheckLivingEntitySpawnEventJS event, Predicate<PlayerSkillDataJS> consumer) {
        allowUnlessAny(event, consumer);
    }

    /**
     * Deny the spawn if no player match the predicate.
     * if none match, no === If 1 matches, yes
     */
    public void denyUnlessAny(CheckLivingEntitySpawnEventJS event, Predicate<PlayerSkillDataJS> consumer) {
        allowIfAny(event, consumer);
    }

    /**
     * Deny the spawn if any player does not match the predicate.
     * if 1 doesn't match, no === If all matches, yes
     */
    public void denyUnlessAll(CheckLivingEntitySpawnEventJS event, Predicate<PlayerSkillDataJS> consumer) {
        allowIfAll(event, consumer);
    }
}
