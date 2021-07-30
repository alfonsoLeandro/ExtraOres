package com.github.alfonsoleandro.extraores.listeners;

import com.github.alfonsoleandro.extraores.ExtraOres;
import com.github.alfonsoleandro.extraores.events.OreBreakEvent;
import com.github.alfonsoleandro.extraores.managers.OresManager;
import com.github.alfonsoleandro.extraores.utils.MessageSender;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OreBreakListener implements Listener {

    private final MessageSender messageSender;

    public OreBreakListener(ExtraOres plugin){
        this.messageSender = plugin.getMessageSender();
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onOreBreak(OreBreakEvent event){
        messageSender.debug("Ore break even triggered, ore: "+event.getOre().getOreName());
        Bukkit.broadcastMessage("Ore broken: "+event.getOre().getOreName());
        if(event.isCancelled()){
            Bukkit.broadcastMessage("Ore break cancelled.");
            return;
        }
        event.getOre().onBreak(event.getPlayer(), event.getBlock().getLocation());
    }


}
