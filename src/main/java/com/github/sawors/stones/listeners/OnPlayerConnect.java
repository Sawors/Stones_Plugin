package com.github.sawors.stones.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class OnPlayerConnect implements Listener {


    @EventHandler
    public void onConnect(PlayerJoinEvent event) {

        event.joinMessage(null);
        if(event.getPlayer().getUniqueId().equals(UUID.fromString("30b80f6f-f0dc-4b4a-96b2-c37b28494b1b"))){
            //MOLE      30b80f6f-f0dc-4b4a-96b2-c37b28494b1b
            //Sawors    f96b1fab-2391-4c41-b6aa-56e6e91950fd

            PlayerProfile profile = event.getPlayer().getPlayerProfile();
            profile.setName("MOLE1283");
            event.getPlayer().setPlayerProfile(profile);
        }
    }
}
