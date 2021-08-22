package com.github.alfonsoleandro.extraores.ores.defaultores;

import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;

public class ExperienceOre extends ExtraOre {

    private final int experience;

    public ExperienceOre(String skinURL, Collection<String> enabledInWorlds,
                         Collection<String> replaces, int maxOresPerChunk, double chunkProbability,
                         double probability, int minY, int maxY, int experience, ItemStack item) {
        super("experience_ore", skinURL, enabledInWorlds, replaces, maxOresPerChunk, chunkProbability,
                probability, minY, maxY, item);
        this.experience = experience;
    }

    @Override
    public void onBreak(Player player, Location location) {
        ExperienceOrb orb = (ExperienceOrb) Objects.requireNonNull(location.getWorld())
                .spawnEntity(location, EntityType.EXPERIENCE_ORB);
        orb.setExperience(experience);
    }

}
