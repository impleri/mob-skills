package net.impleri.mobskills;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class MobEvents {
    public void registerEventHandlers() {
        LifecycleEvent.SERVER_STARTING.register(this::onStartup);

        EntityEvent.LIVING_CHECK_SPAWN.register(this::onCheckSpawn);

        InteractionEvent.INTERACT_ENTITY.register(this::onInteract);
    }

    private void onStartup(MinecraftServer minecraftServer) {
        if (Platform.isModLoaded("kubejs")) {
            net.impleri.mobskills.integrations.kubejs.MobSkillsPlugin.onStartup();
        }
    }

    private EventResult onInteract(Player player, Entity entity, InteractionHand interactionHand) {
        if (MobHelper.canInteractWith(player, entity.getType())) {
            return EventResult.pass();
        }

        MobSkills.LOGGER.info("Preventing {} from interacting with {}", player.getName().getString(), MobHelper.getEntityKey(entity.getType()));

        return EventResult.interruptFalse();
    }

    private EventResult onCheckSpawn(LivingEntity livingEntity, LevelAccessor levelAccessor, double x, double y, double z, MobSpawnType mobSpawnType, @Nullable BaseSpawner baseSpawner) {
        var pos = new Vec3i(x, y, z);

        if (MobHelper.canSpawn(livingEntity.getType(), levelAccessor, pos)) {
            return EventResult.pass();
        }

        MobSkills.LOGGER.info("Preventing {} from spawning at {}", MobHelper.getEntityKey(livingEntity.getType()), pos.toShortString());

        return EventResult.interruptFalse();
    }
}
