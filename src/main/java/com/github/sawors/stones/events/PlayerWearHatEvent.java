package com.github.sawors.stones.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerWearHatEvent extends PlayerEvent implements Cancellable {
    
    private boolean cancelled = false;
    ItemStack item;
    boolean puttinghat;
    
    public PlayerWearHatEvent(@NotNull Player who, ItemStack hat, boolean putting) {
        super(who);
        item = hat;
        this.puttinghat = putting;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    
    public boolean isPuttingHat(){
        return puttinghat;
    }
    
    public ItemStack getItem(){
        return item;
    }
    
    
    // just to avoid having weird behaviour with handlers
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
