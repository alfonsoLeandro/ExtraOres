package com.github.alfonsoleandro.extraores.listeners;

import com.github.alfonsoleandro.extraores.events.OreBreakEvent;
import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockBreakListener implements Listener {

//    private final ExtraOres plugin;
//    private final OresManager oresManager;
//
//    public BlockBreakListener(ExtraOres plugin){
//        this.plugin = plugin;
//        this.oresManager = plugin.getOresManager();
//    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        if(!block.getType().equals(Material.PLAYER_WALL_HEAD)) return;
        if(!block.hasMetadata("ExtraOre")) return;
        event.setDropItems(false);
        Bukkit.getPluginManager().callEvent(
                new OreBreakEvent(event, (ExtraOre) block.getMetadata("ExtraOre").get(0).value())
        );
    }
}
