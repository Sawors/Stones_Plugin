package com.github.sawors.stones.listeners;

import com.github.sawors.stones.UsefulThings.DataHolder;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player p = event.getPlayer();

        //event if a player is in the checklist
        if(DataHolder.getMovementCheckList().contains(p)){
          //based on checklist
        }

        //event if literally change block
        if(event.hasChangedBlock()){


            //logic
            if(p.getWorld().hasStorm()){
                if(p.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.FIRE_ASPECT) || p.getInventory().getItemInOffHand().getEnchantments().containsKey(Enchantment.FIRE_ASPECT)) {
                    if (p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().getY() < p.getLocation().getY()) {
                        if (p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.FIRE_ASPECT) >= 1) {
                            UsefulThings.extinguishItem(p.getInventory().getItemInMainHand(), "fake", p);
                        }
                        if (p.getInventory().getItemInOffHand().getEnchantmentLevel(Enchantment.FIRE_ASPECT) >= 1) {
                            UsefulThings.extinguishItem(p.getInventory().getItemInOffHand(), "fake", p);
                        }
                    }
                }
            }

        }

        //event if moves slightly
        if(event.hasChangedPosition()){
            //logic
            if(p.getEyeLocation().getBlock().getType() == Material.WATER){
                if(p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.FIRE_ASPECT) == 1){
                    UsefulThings.extinguishItem(p.getInventory().getItemInMainHand(), "water", p);
                } else if(p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.FIRE_ASPECT) >= 1){
                    p.getWorld().spawnParticle(Particle.BUBBLE_COLUMN_UP, p.getLocation(), 1, 0.2, 0.5, 0.2, 0.5);
                }
                if (p.getInventory().getItemInOffHand().getEnchantmentLevel(Enchantment.FIRE_ASPECT) == 1){
                    UsefulThings.extinguishItem(p.getInventory().getItemInOffHand(), "water", p);
                } else if(p.getInventory().getItemInOffHand().getEnchantmentLevel(Enchantment.FIRE_ASPECT) >= 1){
                    p.getWorld().spawnParticle(Particle.BUBBLE_COLUMN_UP, p.getLocation(), 1, 0.2, 0.5, 0.2, 0.5);
                }

            }
        }
    }
}
