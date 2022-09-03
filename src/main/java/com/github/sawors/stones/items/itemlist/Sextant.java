package com.github.sawors.stones.items.itemlist;

import com.github.sawors.stones.items.StonesItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Sextant extends StonesItem implements Listener {
    public Sextant() {
        super();
        setMaterial(Material.COPPER_INGOT);
    }
    
    // TODO : Give it a real role in navigation
    @EventHandler
    public static void onPlayerUseSextant(PlayerInteractEvent event){
        event.getPlayer().sendMessage(event.getPlayer().getLocation().getChunk().getX()+" : "+event.getPlayer().getLocation().getChunk().getZ());
    }
}
