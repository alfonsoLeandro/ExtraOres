package com.github.alfonsoleandro.extraores.events;

import com.github.alfonsoleandro.extraores.ores.ExtraOre;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;

public class OreBreakEvent extends BlockEvent implements Cancellable {

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
     * Whether the player mined the ore using an item enchanted with {@link org.bukkit.enchantments.Enchantment#SILK_TOUCH}.
     */
    protected final boolean isSilkTouch;

    /**
     * Called when a custom ore registered in this plugin's custom ores' manager is broken.
     * @param blockBreakEvent The event that triggered this OreBreakEvent.
     * @param ore The ExtraOre being broke.
     * @param isSilkTouch Whether the player mined the ore using an item enchanted with {@link org.bukkit.enchantments.Enchantment#SILK_TOUCH}.
     */
    public OreBreakEvent(BlockBreakEvent blockBreakEvent, ExtraOre ore, boolean isSilkTouch) {
        super(blockBreakEvent.getBlock());
        this.blockBreakEvent = blockBreakEvent;
        this.ore = ore;
        this.isSilkTouch = isSilkTouch;
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
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled(){
        return this.isCancelled || this.blockBreakEvent.isCancelled();
    }

    /**
     * Checks to see if the player used Silk touch to mine this ore. If true, {@link ExtraOre#onBreak(Player, Location)}
     * will not be called, instead, the ore item will be dropped.
     * @return Whether the player mined the ore using an item enchanted with {@link org.bukkit.enchantments.Enchantment#SILK_TOUCH}.
     */
    public boolean isSilkTouch(){
        return this.isSilkTouch;
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
