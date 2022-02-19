package com.github.sawors.stones.listeners;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.database.DataHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class OnPlayerConnect implements Listener {


    @EventHandler
    public void onConnect(PlayerJoinEvent event) {

        Player p = event.getPlayer();

        event.joinMessage(null);
        if (!event.getPlayer().getUniqueId().equals(UUID.fromString("f96b1fab-2391-4c41-b6aa-56e6e91950fd")) || !event.getPlayer().getUniqueId().equals(UUID.fromString("486c4ffe-667d-4be1-be4d-8acbbc3420a2"))) {
            //MOLE      30b80f6f-f0dc-4b4a-96b2-c37b28494b1b
            //Sawors    f96b1fab-2391-4c41-b6aa-56e6e91950fd
            
            //DEPRECATED BUT USEFUL FOR QUICK UPDATES
            //p.setResourcePack("https://github.com/Sawors/Stones_ResourcePack/raw/main/rp.zip");
        }
        
        
        DataHolder.createDataForPlayer(p.getUniqueId());
    
    
        if(!Stones.getHideNameTeam().getEntries().contains(p.getName())){
            Stones.getHideNameTeam().addEntry(p.getName());
        }
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        event.quitMessage(null);
    }
}
