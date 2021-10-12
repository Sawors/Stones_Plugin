package com.github.sawors.stones.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class OnPlayerConnect implements Listener {


    @EventHandler
    public void onConnect(PlayerJoinEvent event) {

        Player p = event.getPlayer();

        event.joinMessage(null);
        if(event.getPlayer().getUniqueId().equals(UUID.fromString("30b80f6f-f0dc-4b4a-96b2-c37b28494b1b"))){
            //MOLE      30b80f6f-f0dc-4b4a-96b2-c37b28494b1b
            //Sawors    f96b1fab-2391-4c41-b6aa-56e6e91950fd

            PlayerProfile profile = p.getPlayerProfile();
            profile.setName("MOLE1283");
            event.getPlayer().setPlayerProfile(profile);
        }

    }
}
