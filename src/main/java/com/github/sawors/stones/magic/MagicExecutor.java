package com.github.sawors.stones.magic;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.entitiy.SEntity;
import com.github.sawors.stones.entitiy.StonesEntities;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MagicExecutor implements Listener {
    
    private static final Stones plugin = Stones.getPlugin();
    
    
    public static void timeSkipper(Player p){
                if(p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("time_skipper")){
    
                    final Integer basespeed;
                    
                    if(p.getWorld().getGameRuleValue(GameRule.RANDOM_TICK_SPEED) != null){
                        basespeed = p.getWorld().getGameRuleValue(GameRule.RANDOM_TICK_SPEED);
                    } else {
                        basespeed = 3;
                    }
                    
                    
                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            if(p.isBlocking() && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("time_skipper")){
                                p.getWorld().setTime(p.getWorld().getTime() + 19);
                                p.getWorld().setGameRule(GameRule.RANDOM_TICK_SPEED, basespeed*20);
                                for(int i = 0; i<8; i++){
                                    p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, p.getLocation().add(3-(Math.random()*6),Math.random()*4,(Math.random()-0.5)*4), 0,-.5,0,0,.5);
                                }
                            } else{
                                p.getWorld().setGameRule(GameRule.RANDOM_TICK_SPEED, basespeed);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin,10,1);
    
                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            if(p.isBlocking() && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("time_skipper")){
                                p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1,1.0f);
                            } else{
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin,10,20);
                }
            
    }
    
    public static void invokeShadowfax(Player p){
        Location ploc = p.getLocation();
        Horse shadowfax = (Horse) p.getWorld().spawnEntity(ploc, EntityType.HORSE, false);
        shadowfax.setColor(Horse.Color.WHITE);
        shadowfax.setStyle(Horse.Style.WHITE);
        shadowfax.setAdult();
        shadowfax.addPassenger(p);
        shadowfax.setTamed(true);
        shadowfax.setOwner(p);
        shadowfax.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        shadowfax.setJumpStrength(1);
        shadowfax.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000,1,false,false));
        StonesEntities.registerEntity(shadowfax.getUniqueId(), SEntity.SHADOWFAX);
    }
    
    @EventHandler
    public void leaveShadowfax(VehicleExitEvent event){
        if(StonesEntities.containsEntity(event.getVehicle().getUniqueId()) && StonesEntities.getEntity(event.getVehicle().getUniqueId()).equals(SEntity.SHADOWFAX)){
            StonesEntities.entityRemove(event.getVehicle());
        }
    }
    
    
    
}
