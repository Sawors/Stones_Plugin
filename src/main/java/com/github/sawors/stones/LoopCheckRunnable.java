package com.github.sawors.stones;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class LoopCheckRunnable extends BukkitRunnable {
    private final JavaPlugin plugin;

    public LoopCheckRunnable(JavaPlugin plugin){
        this.plugin = plugin;
    }


    @Override
    public void run() {

        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().hasItemMeta() && p.getInventory().getHelmet().getItemMeta().getLocalizedName().equals("blindfold")){
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2*20, 99, false, false));
            }
            if(p.getInventory().getItemInMainHand().getType() != Material.AIR && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("handcuffsON")){
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2*20, 0, false, false));
            }
            if(p.getInventory().getItemInMainHand().getType() == Material.RAW_IRON){
                //p.setVelocity(new Vector((-p.getLocation().getX() + 0)/2, (-p.getLocation().getY() + 4)/2, (-p.getLocation().getZ() + 0)/2));
                //p.setLeashHolder(p.getWorld().spawnEntity(p.getLocation(), EntityType.BAT));
            }
        }
    }
}
