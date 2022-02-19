package com.github.sawors.stones.death;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.database.DataHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class DeathManager implements Listener {


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        event.setCancelled(true);
        Player p = event.getEntity();
        for(PotionEffect potion : p.getActivePotionEffects()){
            p.removePotionEffect(potion.getType());
        }
        p.setInvulnerable(true);
        p.setInvisible(true);
        p.setUnsaturatedRegenRate(0);
        p.setSaturatedRegenRate(0);
        p.setSaturation(0);
        p.setHealth(1);
        p.setArrowsInBody(0);
        if(!p.getItemOnCursor().getType().isAir()){
            p.getWorld().dropItemNaturally(p.getLocation().add(0,2.5,0), p.getItemOnCursor());
        }
        p.setItemOnCursor(null);
        p.updateInventory();
        Inventory deadinventory = Bukkit.createInventory(p, 5*9, Component.text("Dead Body"));
        if(p.getInventory().getContents().length > 0){
            deadinventory.setContents(p.getInventory().getContents());
        }



        DataHolder.getInventoryMap().put(p.getName(), deadinventory);



        p.getInventory().clear();

        p.getInventory().setHeldItemSlot(4);
        p.showTitle(Title.title(Component.text(ChatColor.RED + "- ALMOST DEAD -"), Component.text(ChatColor.DARK_GRAY + "you can still be revived")));

    }

    @EventHandler
    public void onPlayerLifeRegen(EntityRegainHealthEvent event){
        if(event.getEntity() instanceof Player){
            Player p = (Player) event.getEntity();
            if(p.getHealth() == 1 && p.isInvisible() && p.isInvulnerable()){
                p.setInvulnerable(false);
                p.setInvisible(false);
                PlayerInventory playerinv = p.getInventory();

                for(int i = 0; i <= 35; i++){
                    if(DataHolder.getInventoryMap().get(p.getName()).getItem(i) == null){
                        playerinv.setItem(i, new ItemStack(Material.AIR));
                    } else {
                        playerinv.setItem(i, DataHolder.getInventoryMap().get(p.getName()).getItem(i));
                    }

                }
                playerinv.setBoots(DataHolder.getInventoryMap().get(p.getName()).getItem(36));
                playerinv.setLeggings(DataHolder.getInventoryMap().get(p.getName()).getItem(37));
                playerinv.setChestplate(DataHolder.getInventoryMap().get(p.getName()).getItem(38));
                playerinv.setHelmet(DataHolder.getInventoryMap().get(p.getName()).getItem(39));
                playerinv.setItemInOffHand(DataHolder.getInventoryMap().get(p.getName()).getItem(40));
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(UsefulThings.isDead(event.getPlayer())){
            if(event.getFrom().getX() != event.getTo().getX() && event.getFrom().getZ() != event.getTo().getZ()){
                event.getPlayer().setVelocity(new Vector(0,-5,0));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractWithPlayer(PlayerInteractEntityEvent event){
        if(event.getRightClicked() instanceof Player){
            Player clicker = event.getPlayer();
            Player clicked = (Player) event.getRightClicked();

            //if clicked is dead
            if(UsefulThings.isDead(clicked)){
                if(DataHolder.getInventoryMap().containsKey(clicked.getName())){
                    clicker.openInventory(DataHolder.getInventoryMap().get(clicked.getName()));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtBlock(PlayerInteractEvent event){
        if(UsefulThings.isDead(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractAtVehicle(VehicleEnterEvent event){
        if(event.getEntered() instanceof Player && UsefulThings.isDead((Player) event.getEntered())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerClickAtEntity(PlayerInteractEntityEvent event){
        if(UsefulThings.isDead(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        if(UsefulThings.isDead(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerAttemptPickupItemEvent event){
        if(UsefulThings.isDead(event.getPlayer())){
            event.setCancelled(true);
        }
    }
}
