package com.github.sawors.stones.listeners;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ForbiddenMoveItemListener implements Listener {
    Component message = Component.text(ChatColor.RED + "you cannot do that !");

    @EventHandler
    public void onPlayerClickEvent(InventoryClickEvent event){

        ItemStack item = null;
        if(event.getCurrentItem() != null) {item = event.getCurrentItem();}
        HumanEntity p = event.getWhoClicked();

        if(UsefulThings.isItemImmovable(item)){
            p.sendActionBar(message);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NETHERITE_BLOCK_BREAK, 1f, 0.1f);
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        ItemStack item = event.getItemDrop().getItemStack();
        if(UsefulThings.isItemImmovable(item)){
            event.getPlayer().sendActionBar(message);
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NETHERITE_BLOCK_BREAK, 1f, 0.1f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerClickAtEntity(PlayerInteractEntityEvent event){
        Player p = event.getPlayer();
        ItemStack item = p.getActiveItem();
        if(UsefulThings.isItemImmovable(item)){
            p.sendActionBar(message);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NETHERITE_BLOCK_BREAK, 1f, 0.1f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractAtVehicle(VehicleEnterEvent event){
        if(event.getEntered() instanceof Player && !(event.getVehicle() instanceof Minecart || event.getVehicle() instanceof ArmorStand || event.getVehicle() instanceof Boat)){
            ItemStack item = ((Player) event.getEntered()).getInventory().getItemInMainHand();
            if(UsefulThings.isItemImmovable(item)){
                //event.getEntered().teleport(event.getEntered().getLocation().add(0,0.25,0));
                event.getEntered().sendActionBar(message);
                event.getEntered().getWorld().playSound(event.getEntered().getLocation(), Sound.BLOCK_NETHERITE_BLOCK_BREAK, 1f, 0.1f);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtBlock(PlayerInteractEvent event){
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if(UsefulThings.isItemImmovable(item)){
            //event.getEntered().teleport(event.getEntered().getLocation().add(0,0.25,0));
            p.sendActionBar(message);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NETHERITE_BLOCK_BREAK, 1f, 0.1f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSwitchItemInHand(PlayerItemHeldEvent event){
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItem(event.getPreviousSlot());
        if(UsefulThings.isItemImmovable(item)){
            //event.getEntered().teleport(event.getEntered().getLocation().add(0,0.25,0));
            p.sendActionBar(message);
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NETHERITE_BLOCK_BREAK, 1f, 0.1f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event){

        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if(event.hasChangedBlock()){
            //block
            if(UsefulThings.isItemImmovable(item)){
                //event.getEntered().teleport(event.getEntered().getLocation().add(0,0.25,0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40,0 ));
                p.getWorld().playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 0.1f, 0.8f);


            }
        }
    }
}
