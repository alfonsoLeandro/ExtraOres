package com.github.alfonsoleandro.extraores.ores.defaultores;

import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;

public class ZombieOre extends ExtraOre {


    public ZombieOre(String skinURL, Collection<String> enabledInWorlds, Collection<String> replaces,
                     int maxOresPerChunk, double chunkProbability, double probability, int minY, int maxY,
                     ItemStack item) {
        super("zombie_ore", skinURL, enabledInWorlds, replaces, maxOresPerChunk, chunkProbability,
                probability, minY, maxY, item);
    }

    @Override
    public void onBreak(Player player, Location location) {
        Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.ZOMBIE);
    }
}
