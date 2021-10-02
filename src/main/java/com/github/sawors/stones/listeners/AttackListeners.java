package com.github.sawors.stones.listeners;

import com.github.sawors.stones.UsefulThings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackListeners implements Listener {
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
            Player damager = (Player) event.getDamager();
            Entity receiver = event.getEntity();
            if(damager.getInventory().getItemInMainHand().getType() == Material.IRON_NUGGET){
                damager.sendMessage(ChatColor.YELLOW + "Yaws :" + "\n" + "  Player : " + ChatColor.GREEN + Math.abs(damager.getLocation().getYaw()) + ChatColor.YELLOW + "\n" + "  Victim : " +  ChatColor.GREEN + Math.abs(receiver.getLocation().getYaw()) + "\n");
                if(UsefulThings.isBehind(damager, receiver)){
                    damager.sendMessage("yay");
                }
            }
        }
    }
}
