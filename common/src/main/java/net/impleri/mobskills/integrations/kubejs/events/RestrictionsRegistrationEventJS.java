package net.impleri.mobskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.server.ServerEventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.mobskills.MobHelper;
import net.impleri.mobskills.MobSkills;
import net.impleri.playerskills.utils.RegistrationType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public class RestrictionsRegistrationEventJS extends ServerEventJS {
    public RestrictionsRegistrationEventJS(MinecraftServer s) {
        super(s);
    }

    public void restrict(String entityType, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        RegistrationType<EntityType<?>> registrationType = new RegistrationType<EntityType<?>>(entityType, net.minecraft.core.Registry.ENTITY_TYPE_REGISTRY);

        registrationType.ifNamespace(namespace -> restrictNamespace(namespace, consumer));
        registrationType.ifName(name -> restrictEntity(name, consumer));
        registrationType.ifTag(tag -> restrictTag(tag, consumer));
    }

    @HideFromJS
    private void restrictEntity(ResourceLocation name, @NotNull Consumer<RestrictionJS.Builder> consumer) {
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
    private void restrictNamespace(String namespace, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        ConsoleJS.SERVER.info("Creating mob restrictions for mod " + namespace);

        net.minecraft.core.Registry.ENTITY_TYPE.keySet()
                .stream()
                .filter(name -> name.getNamespace().equals(namespace))
                .forEach(name -> restrictEntity(name, consumer));
    }

    @HideFromJS
    private void restrictTag(TagKey<EntityType<?>> tag, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        ConsoleJS.SERVER.info("Creating mob restrictions for tag " + tag);
        net.minecraft.core.Registry.ENTITY_TYPE.stream()
                .filter(type -> type.is(tag))
                .forEach(type -> restrictEntity(MobHelper.getEntityKey(type), consumer));
    }
}
