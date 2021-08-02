package com.github.alfonsoleandro.extraores.ores.defaultores;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;

public class FakeCompactDiamondOre extends ExtraOre {

    private final ExtraOres plugin;
    private final int power;
    private final String message;

    public FakeCompactDiamondOre(String skinURL, Collection<String> enabledInWorlds, Collection<String> replaces,
                             int maxOresPerChunk, double chunkProbability, double probability, int minY, int maxY,
                             int power, ExtraOres plugin, String message, ItemStack item) {
        super("fake_compact_diamond_ore", skinURL, enabledInWorlds, replaces, maxOresPerChunk,
                chunkProbability, probability, minY, maxY, item);
        this.plugin = plugin;
        this.power = power;
        this.message = message;
    }

    @Override
    public void onBreak(Player player, Location location) {
        Objects.requireNonNull(location.getWorld()).createExplosion(location, power);
        if(message != null && !message.equalsIgnoreCase("")) plugin.getMessageSender().send(player,
                message);
    }
}
