package net.impleri.mobskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.mobskills.MobHelper;
import net.impleri.mobskills.MobSkills;
import net.impleri.mobskills.restrictions.Restriction;
import net.impleri.playerskills.restrictions.AbstractRegistrationEventJS;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;


public class RestrictionsRegistrationEventJS extends AbstractRegistrationEventJS<EntityType<?>, Restriction, RestrictionJS.Builder> {
    public RestrictionsRegistrationEventJS(MinecraftServer server) {
        super(server, "mob", Registry.ENTITY_TYPE);
    }

    @HideFromJS
    @Override
    protected void restrictOne(ResourceLocation name, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var builder = new RestrictionJS.Builder(name, server);

        consumer.accept(builder);

        var type = MobHelper.getEntityType(name);

        if (type == null) {
            ConsoleJS.SERVER.warn("Could not find any mob named " + name.toString());
        }

        var restriction = builder.createObject(type);
        ConsoleJS.SERVER.info("Created mob restriction for " + name);
        MobSkills.RESTRICTIONS.add(name, restriction);
    }

    @HideFromJS
    @Override
    public Predicate<EntityType<?>> isTagged(TagKey<EntityType<?>> tag) {
        return type -> type.is(tag);
    }

    @HideFromJS
    @Override
    public ResourceLocation getName(EntityType<?> resource) {
        return MobHelper.getEntityKey(resource);
    }
}
