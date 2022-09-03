package com.github.sawors.stones.items.itemlist;

import com.github.sawors.stones.items.StonesItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class WoodCrayon extends StonesItem implements Listener {
    public WoodCrayon() {
        super();
        
        setMaterial(Material.STICK);
    }
    
    //
    //  SIGN EDIT WITH CRAYON
    @EventHandler
    public void playerUsesCrayon(PlayerInteractEvent event){
        if(event.getAction().isRightClick() && getItemId(event.getPlayer().getInventory().getItemInMainHand()).equals(new WoodCrayon().getTypeId())){
            Block b = event.getClickedBlock();
            Player p = event.getPlayer();
            if(b != null && b.getType().toString().contains("SIGN")){
                org.bukkit.block.Sign sign = ((org.bukkit.block.Sign) b.getState());
                p.openSign(sign);
            }
        }
    }
    //
    //  SIGN EDIT EXIT
    @EventHandler
    public void playerExitSign(SignChangeEvent event){
        Location loc = event.getBlock().getLocation().add(0.5,0.5,0.5);
        loc.getWorld().playSound(loc, Sound.ENTITY_VILLAGER_WORK_CARTOGRAPHER, .5f, 1);
    }
}
