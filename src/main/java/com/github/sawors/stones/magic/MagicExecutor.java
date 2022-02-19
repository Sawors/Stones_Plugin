package com.github.sawors.stones.magic;

import com.github.sawors.stones.Stones;
import org.bukkit.GameRule;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MagicExecutor {
    
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
    
    
    
}
