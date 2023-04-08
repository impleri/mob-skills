package net.impleri.mobskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.mobskills.restrictions.EntitySpawnMode;
import net.impleri.mobskills.restrictions.Restriction;
import net.impleri.playerskills.restrictions.AbstractRestrictionBuilder;
import net.impleri.playerskills.restrictions.PlayerDataJS;
import net.impleri.playerskills.utils.SkillResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class RestrictionJS extends Restriction {
    private static final ResourceKey<Registry<Restriction>> key = ResourceKey.createRegistryKey(SkillResourceLocation.of("mob_restriction_builders_registry"));

    public static final RegistryObjectBuilderTypes<Restriction> registry = RegistryObjectBuilderTypes.add(key, Restriction.class);

    private static final Map<String, MobSpawnType> spawnTypeMap = new HashMap<>() {{
        put("natural", MobSpawnType.NATURAL);
        put("chunk", MobSpawnType.CHUNK_GENERATION);
        put("spawner", MobSpawnType.SPAWNER);
        put("structure", MobSpawnType.STRUCTURE);
        put("patrol", MobSpawnType.PATROL);
    }};

    public RestrictionJS(EntityType<?> type, Builder builder) {
        super(
                type,
                builder.condition,
                builder.includeDimensions,
                builder.excludeDimensions,
                builder.includeBiomes,
                builder.excludeBiomes,
                builder.spawnMode,
                builder.usable,
                builder.includeSpawners,
                builder.excludeSpawners,
                builder.replacement
        );
    }

    public static class Builder extends AbstractRestrictionBuilder<Restriction> {
        public EntityType<?> replacement;

        public EntitySpawnMode spawnMode = EntitySpawnMode.ALLOW_ALWAYS;
        public boolean usable = true;

        public List<MobSpawnType> includeSpawners = new ArrayList<>();

        public List<MobSpawnType> excludeSpawners = new ArrayList<>();


        @HideFromJS
        public Builder(ResourceLocation id, MinecraftServer server) {
            super(id, server);
        }

        @Override
        public Builder unless(Predicate<PlayerDataJS> consumer) {
            switch (spawnMode) {
                case ALLOW_IF_ANY_MATCH -> {
                    spawnMode = EntitySpawnMode.ALLOW_UNLESS_ANY_MATCH;
                    condition(consumer);
                }
                case ALLOW_IF_ALL_MATCH -> {
                    spawnMode = EntitySpawnMode.ALLOW_UNLESS_ALL_MATCH;
                    condition(consumer);
                }
                case DENY_IF_ANY_MATCH -> {
                    spawnMode = EntitySpawnMode.DENY_UNLESS_ANY_MATCH;
                    condition(consumer);
                }
                case DENY_IF_ALL_MATCH -> {
                    spawnMode = EntitySpawnMode.DENY_UNLESS_ALL_MATCH;
                    condition(consumer);
                }
                default -> super.unless(consumer);
            }

            return this;
        }

        public Builder always() {
            switch (spawnMode) {
                case ALLOW_IF_ANY_MATCH, ALLOW_UNLESS_ANY_MATCH, ALLOW_IF_ALL_MATCH, ALLOW_UNLESS_ALL_MATCH ->
                        spawnMode = EntitySpawnMode.ALLOW_ALWAYS;
                case DENY_IF_ANY_MATCH, DENY_UNLESS_ANY_MATCH, DENY_IF_ALL_MATCH, DENY_UNLESS_ALL_MATCH ->
                        spawnMode = EntitySpawnMode.DENY_ALWAYS;
            }

            return this;
        }

        public Builder spawnable(boolean requireAll) {
            this.spawnMode = Boolean.TRUE.equals(requireAll) ? EntitySpawnMode.ALLOW_IF_ALL_MATCH : EntitySpawnMode.ALLOW_IF_ANY_MATCH;

            return this;
        }

        public Builder spawnable() {
            return spawnable(false);
        }

        public Builder unspawnable(boolean requireAll) {
            this.spawnMode = Boolean.TRUE.equals(requireAll) ? EntitySpawnMode.DENY_IF_ALL_MATCH : EntitySpawnMode.DENY_IF_ANY_MATCH;

            return this;
        }

        public Builder unspawnable() {
            return unspawnable(false);
        }

        public Builder fromSpawner(String spawner) {
            var spawnType = spawnTypeMap.get(spawner);

            if (spawnType == null) {
                ConsoleJS.SERVER.warn("Could not find spawn type named " + spawner);
            } else {
                includeSpawners.add(spawnType);
                excludeSpawners.remove(spawnType);
            }

            return this;
        }

        public Builder notFromSpawner(String spawner) {
            var spawnType = spawnTypeMap.get(spawner);

            if (spawnType == null) {
                ConsoleJS.SERVER.warn("Could not find spawn type named " + spawner);
            } else {
                excludeSpawners.add(spawnType);
                includeSpawners.remove(spawnType);
            }

            return this;
        }

        public Builder usable() {
            this.usable = true;

            return this;
        }

        public Builder unusable() {
            this.usable = false;

            return this;
        }

        public Builder nothing() {
            this.usable = true;
            this.spawnMode = EntitySpawnMode.ALLOW_ALWAYS;

            return this;
        }

        public Builder everything() {
            this.usable = false;
            this.spawnMode = EntitySpawnMode.DENY_ALWAYS;

            return this;
        }

        @HideFromJS
        @Override
        public RegistryObjectBuilderTypes<Restriction> getRegistryType() {
            return registry;
        }

        @HideFromJS
        @Override
        public Restriction createObject() {
            return null;
        }

        @HideFromJS
        public Restriction createObject(EntityType<?> type) {
            return new RestrictionJS(type, this);
        }
    }
}
