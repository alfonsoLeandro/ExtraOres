package com.github.alfonsoleandro.extraores.ores.defaultores;

import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;

public class HealthOre extends ExtraOre {

    private final double health;

    public HealthOre(String skinURL, Collection<String> enabledInWorlds,
                     Collection<String> replaces, int maxOresPerChunk, double chunkProbability,
                     double probability, int minY, int maxY, double health, ItemStack item) {
        super("heart_ore", skinURL, enabledInWorlds, replaces, maxOresPerChunk, chunkProbability,
                probability, minY, maxY, item);
        this.health = health;
    }

    @Override
    public void onBreak(Player player, Location location) {
        double maxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
        player.setHealth(Math.min(maxHealth, player.getHealth()+health));
        Objects.requireNonNull(player.getLocation().getWorld()).spawnParticle(Particle.HEART, player.getLocation(), 4);
        //todo remove debug
        Bukkit.broadcastMessage("Health ORE BROKEN");
    }

}
