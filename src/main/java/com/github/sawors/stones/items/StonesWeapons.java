package com.github.sawors.stones.items;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class StonesWeapons implements Listener {
    
    
    @EventHandler
    public void weaponsDraw(PlayerItemHeldEvent event){
        Player p = event.getPlayer();
        ItemStack newitem = p.getInventory().getItem(event.getNewSlot());
        ItemStack olditem = p.getInventory().getItem(event.getPreviousSlot());
        
        if(newitem != null && newitem.getType().toString().contains("SWORD") && (!newitem.getType().toString().contains("WOOD") && !newitem.getType().toString().contains("STONE"))){
            p.getWorld().playSound(p.getLocation().add(0,1,0), "sawors.weapons.sword.draw", 1, 1.25f);
        }
        if(olditem != null && olditem.getType().toString().contains("SWORD") && (!olditem.getType().toString().contains("WOOD") && !olditem.getType().toString().contains("STONE"))){
            p.getWorld().playSound(p.getLocation().add(0,1,0), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, .75f);
        }
        
    }
    
}
