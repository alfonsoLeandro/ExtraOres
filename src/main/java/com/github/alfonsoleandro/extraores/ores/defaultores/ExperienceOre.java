package com.github.alfonsoleandro.extraores.ores.defaultores;

import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ExperienceOre extends ExtraOre {

    private final int experience;

    public ExperienceOre(String skinURL, Collection<String> enabledInWorlds,
                         Collection<String> replaces, int maxOresPerChunk, double chunkProbability,
                         double probability, int minY, int maxY, int experience) {
        super("experience_ore", skinURL, enabledInWorlds, replaces, maxOresPerChunk, chunkProbability, probability, minY, maxY);
        this.experience = experience;
    }

    @Override
    public void onBreak(Player player, Location location) {
        ExperienceOrb orb = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
        orb.setExperience(experience);
        //todo remove debug
        Bukkit.broadcastMessage("EXPERIENCE ORE BROKEN");
    }

}
