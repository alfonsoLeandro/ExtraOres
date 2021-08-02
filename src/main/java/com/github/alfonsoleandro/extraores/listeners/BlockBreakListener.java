package com.github.alfonsoleandro.extraores.listeners;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.events.OreBreakEvent;
import com.github.alfonsoleandro.extraores.managers.OresManager;
import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;


public final class BlockBreakListener implements Listener {

    private final ExtraOres plugin;
    private final OresManager oresManager;

    public BlockBreakListener(ExtraOres plugin){
        this.plugin = plugin;
        this.oresManager = plugin.getOresManager();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(!player.getGameMode().equals(GameMode.SURVIVAL)) return;
        Block block = event.getBlock();
        if(!block.getType().equals(Material.PLAYER_WALL_HEAD) && !block.getType().equals(Material.PLAYER_HEAD)) return;
        Skull skull = (Skull) block.getState();
        PersistentDataContainer data = skull.getPersistentDataContainer();
        NamespacedKey nsk = new NamespacedKey(plugin, "ExtraOre");
        if(!data.has(nsk, PersistentDataType.STRING)) return;
        ExtraOre ore = oresManager.getOreByName(data.get(nsk, PersistentDataType.STRING));
        if(ore == null) return;
        boolean hasSilkTouch = event.getPlayer().getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH);
        event.setDropItems(false);
        if(hasSilkTouch){
            Objects.requireNonNull(block.getLocation().getWorld())
                    .dropItemNaturally(block.getLocation(), ore.getOreItem());
        }
        Bukkit.getPluginManager().callEvent(new OreBreakEvent(event, ore,
                hasSilkTouch));
    }

}
