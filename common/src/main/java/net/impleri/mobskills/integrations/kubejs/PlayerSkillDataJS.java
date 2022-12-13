package net.impleri.mobskills.integrations.kubejs;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.playerskills.api.Skill;
import net.impleri.playerskills.registry.RegistryItemNotFound;
import net.impleri.playerskills.server.ServerApi;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerSkillDataJS {
    private final Player player;

    public PlayerSkillDataJS(Player player) {
        this.player = player;
    }

    @Nullable
    @HideFromJS
    private <T> Skill<T> getSkill(String skillName) {
        try {
            return ServerApi.getSkill(player, skillName);
        } catch (RegistryItemNotFound e) {
            // TODO: handle error
            e.printStackTrace();
        }

        return null;
    }

    public <T> boolean can(String skillName, @Nullable T expectedValue) {
        var skill = getSkill(skillName);

        if (skill == null) {
            return false;
        }

        return ServerApi.can(player, skill, expectedValue);
    }

    public <T> boolean can(String skill) {
        return can(skill, null);
    }

    public <T> boolean cannot(String skill, @Nullable T expectedValue) {
        return !can(skill, expectedValue);
    }

    public <T> boolean cannot(String skill) {
        return cannot(skill, null);
    }
}
