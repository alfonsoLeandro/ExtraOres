package com.github.alfonsoleandro.extraores.ores.defaultores;

import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RandomMobOre extends ExtraOre {

    private final List<EntityType> types;
    private final Random r;

    public RandomMobOre(String skinURL, Collection<String> enabledInWorlds, Collection<String> replaces,
                        int maxOresPerChunk, double chunkProbability, double probability, int minY, int maxY,
                        ItemStack item, List<String> mobTypes) {
        super("random_mob_ore", skinURL, enabledInWorlds, replaces, maxOresPerChunk, chunkProbability,
                probability, minY, maxY, item);
        this.types = new ArrayList<>();
        this.r = new Random();
        for(String pType : mobTypes){
            try {
                this.types.add(EntityType.valueOf(pType));
            } catch (IllegalArgumentException ignored) {}
        }
    }

    @Override
    public void onBreak(Player player, Location location) {
        Objects.requireNonNull(location.getWorld()).spawnEntity(location, this.types.get(r.nextInt(this.types.size())));
    }
}
