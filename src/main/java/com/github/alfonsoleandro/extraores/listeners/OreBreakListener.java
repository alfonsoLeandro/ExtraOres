package com.github.alfonsoleandro.extraores.listeners;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.events.OreBreakEvent;
import com.github.alfonsoleandro.extraores.managers.MessageSender;
import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OreBreakListener implements Listener {

    private final MessageSender messageSender;

    public OreBreakListener(ExtraOres plugin){
        this.messageSender = plugin.getMessageSender();
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onOreBreak(OreBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ExtraOre ore = event.getOre();
        messageSender.debug("Ore break even triggered, ore: "+ore.getOreName());
        Bukkit.broadcastMessage("Ore broken: "+event.getOre().getOreName());
        if(event.isCancelled()){
            Bukkit.broadcastMessage("Ore break cancelled.");
            return;
        }
        if(!event.isSilkTouch()){
            ore.onBreak(player, block.getLocation());
        }
    }


}
