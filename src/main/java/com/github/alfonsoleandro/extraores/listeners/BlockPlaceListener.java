package com.github.alfonsoleandro.extraores.listeners;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.managers.OresManager;
import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import com.github.alfonsoleandro.extraores.managers.MessageSender;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockPlaceListener implements Listener {

    private final ExtraOres plugin;
    private final MessageSender messageSender;
    private final OresManager oresManager;


    public BlockPlaceListener(ExtraOres plugin) {
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        this.oresManager = plugin.getOresManager();
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onOrePlace(BlockPlaceEvent event){
        Block block = event.getBlock();
        if(block.getType().equals(Material.PLAYER_HEAD) || block.getType().equals(Material.PLAYER_WALL_HEAD)){
            Bukkit.broadcastMessage("ore: "+event.getItemInHand().getType());
            ExtraOre ore = oresManager.getOreByItem(event.getItemInHand());
            if(ore == null) return;
            Skull skull = (Skull) block.getState();
            PersistentDataContainer data = skull.getPersistentDataContainer();
            data.set(new NamespacedKey(plugin, "ExtraOre"), PersistentDataType.STRING,
                    ore.getOreName());
            skull.update();
            messageSender.send(event.getPlayer(), MessageSender.Message.ORE_PLACED,
                    "%ore_name%",ore.getOreName());
        }
    }


}
