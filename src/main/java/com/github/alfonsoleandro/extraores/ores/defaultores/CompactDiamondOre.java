package com.github.alfonsoleandro.extraores.ores.defaultores;

import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;

public class CompactDiamondOre extends ExtraOre {

    private final int amount;

    public CompactDiamondOre(String skinURL, Collection<String> enabledInWorlds, Collection<String> replaces,
                                 int maxOresPerChunk, double chunkProbability, double probability, int minY,
                             int maxY, int amount, ItemStack item) {
        super("compact_diamond_ore", skinURL, enabledInWorlds, replaces, maxOresPerChunk, chunkProbability,
                probability, minY, maxY, item);
        this.amount = amount;
    }

    @Override
    public void onBreak(Player player, Location location) {
        Objects.requireNonNull(location.getWorld()).dropItemNaturally(location, new ItemStack(Material.DIAMOND, amount));
    }

}
