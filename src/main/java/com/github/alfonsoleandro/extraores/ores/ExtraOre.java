package com.github.alfonsoleandro.extraores.ores;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class ExtraOre {

    protected String oreName;
    protected String skinURL;
    protected Set<String> enabledInWorlds;
    protected Set<String> replaces;
    protected int maxOresPerChunk;
    protected double chunkProbability;
    protected double probability;
    protected int maxY;
    protected int minY;


    public ExtraOre(String oreName, String skinURL, Collection<String> enabledInWorlds, Collection<String> replaces, int maxOresPerChunk,
                    double chunkProbability, double probability, int minY, int maxY) {
        this.oreName = oreName;
        this.skinURL = skinURL;
        this.enabledInWorlds = new HashSet<>();
        this.enabledInWorlds.addAll(enabledInWorlds);
        this.replaces = new HashSet<>();
        this.replaces.addAll(replaces);
        this.maxOresPerChunk = maxOresPerChunk;
        this.chunkProbability = chunkProbability;
        this.probability = probability;
        this.maxY = maxY;
        this.minY = minY;
    }

    /**
     * Method called when this ore is broken by a player.
     * Unless the {@link com.github.alfonsoleandro.extraores.events.OreBreakEvent} is cancelled.
     * @param player The player breaking this ore.
     * @param location  The location of the ore (block).
     */
    public abstract void onBreak(Player player, Location location);

    public String getOreName() {
        return oreName;
    }

    public String getSkinURL() {
        return skinURL;
    }

    public Set<String> getEnabledInWorlds() {
        return enabledInWorlds;
    }

    public Set<String> getReplaces() {
        return replaces;
    }

    public int getMaxOresPerChunk() {
        return maxOresPerChunk;
    }

    public double getChunkProbability() {
        return chunkProbability;
    }

    public double getProbability() {
        return probability;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMinY() {
        return minY;
    }
}
