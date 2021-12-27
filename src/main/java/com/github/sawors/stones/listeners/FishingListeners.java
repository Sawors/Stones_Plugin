package com.github.sawors.stones.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingListeners implements Listener {

    
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event){
        event.setCancelled(true);
    }

}
