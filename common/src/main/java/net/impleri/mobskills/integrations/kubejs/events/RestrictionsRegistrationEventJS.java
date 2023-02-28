package net.impleri.mobskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.mobskills.MobHelper;
import net.impleri.mobskills.restrictions.Registry;
import net.impleri.playerskills.utils.SkillResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RestrictionsRegistrationEventJS extends EventJS {
    public void restrict(String entityType, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        if (entityType.trim().endsWith(":*")) {
            var namespace = entityType.substring(0, entityType.indexOf(":"));

            restrictNamespace(namespace, consumer);
            return;
        }

        var name = SkillResourceLocation.of(entityType);
        restrictEntity(name, consumer);
    }

    @HideFromJS
    private void restrictEntity(ResourceLocation name, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var builder = new RestrictionJS.Builder(name);

        consumer.accept(builder);

        var type = MobHelper.getEntityType(name);

        if (type == null) {
            ConsoleJS.SERVER.warn("Could not find any mob named " + name.toString());
        }

        var restriction = builder.createObject(type);
        ConsoleJS.SERVER.info("Created mob restriction for " + name);
        Registry.INSTANCE.add(name, restriction);
    }

    @HideFromJS
    private void restrictNamespace(String namespace, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        net.minecraft.core.Registry.ENTITY_TYPE.keySet()
                .stream()
                .filter(name -> name.getNamespace().equals(namespace))
                .forEach(name -> restrictEntity(name, consumer));
    }
}
