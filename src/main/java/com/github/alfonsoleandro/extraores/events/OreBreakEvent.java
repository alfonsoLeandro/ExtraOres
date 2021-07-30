package com.github.alfonsoleandro.extraores.events;

import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

public class OreBreakEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    /**
     * The ore that caused this event to fire.
     */
    protected final ExtraOre ore;
    /**
     * The {@link BlockBreakEvent} that triggered this OreBreakEvent.
     */
    protected final BlockBreakEvent blockBreakEvent;
    /**
     * The cancellation state of this event.
     */
    protected boolean isCancelled;

    /**
     * Called when a custom ore registered in this plugin's custom ores' manager is broken.
     * @param blockBreakEvent The event that triggered this OreBreakEvent.
     * @param ore The ExtraOre being broke.
     */
    public OreBreakEvent(BlockBreakEvent blockBreakEvent, ExtraOre ore) {
        this.blockBreakEvent = blockBreakEvent;
        this.ore = ore;
    }

    /**
     * The ore broken in this event.
     * @return The ore that caused this event to fire.
     */
    public ExtraOre getOre() {
        return ore;
    }


    /**
     * Gets the Player that is breaking the block involved in this event.
     *
     * @return The Player that is breaking the block involved in this event
     */
    public Player getPlayer() {
        return blockBreakEvent.getPlayer();
    }


    /**
     * Gets the block involved in this event.
     *
     * @return The Block which block is involved in this event
     */
    public final Block getBlock() {
        return blockBreakEvent.getBlock();
    }


    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled(){
        return this.isCancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel){
        this.isCancelled = cancel;
        this.blockBreakEvent.setCancelled(true);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
