package com.github.sawors.stones.features;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.DataHolder;
import com.github.sawors.stones.enums.StoneEffect;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class StonesEffects implements Listener {
    
    public static void doEffects(){
    
        new BukkitRunnable(){
            @Override
            public void run(){
                for(int i = 0; i< DataHolder.getEffectmap().keySet().toArray().length; i++){
                    UUID id = (UUID) DataHolder.getEffectmap().keySet().toArray()[i];
                    if(Bukkit.getPlayer(id) != null && Objects.requireNonNull(Bukkit.getPlayer(id)).isOnline() && DataHolder.getEffectmap().containsKey(id)){
                        Player p = Bukkit.getPlayer(id);
                        ArrayList<StoneEffect> effectlist = DataHolder.effectmapGetEntry(id);
                        if(effectlist.size() > 0 && p != null){
                            for(StoneEffect effect : effectlist){
                                switch (effect){
                                    case CARRY:
                                        doCarry(p);
                                    case DEMON:
                                        doDemon(p);
                                }
                                
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Stones.getPlugin(), 0, 80);
        
        
    }
    
    
    public static void doCarry(Player p){
        if(p.getPassengers().size() > 0){
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, (int) Math.sqrt(4*p.getPassengers().get(0).getBoundingBox().getVolume()), false, false));
        } else {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 2, false, false));
        }
    }
    
    public static void doDemon(Player p){
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,90,0,false,false));
        new BukkitRunnable(){
            int count = 80;
            @Override
            public void run() {
                Vector eyes = new Vector(0,0,1).rotateAroundY(Math.toRadians(-p.getLocation().getYaw()));
                Vector lefteye = eyes.clone().multiply(0.2).add(eyes.clone().setX(-eyes.getZ()).setZ(eyes.getX()).multiply(.2));
                Vector righteye = eyes.clone().multiply(0.2).add(eyes.clone().setX(-eyes.getZ()).setZ(eyes.getX()).multiply(-.2));
                p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p.getLocation().clone().add(0,1,0),32,.2,.3,.2,0);
                p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p.getEyeLocation().add(lefteye),1,0,0,0,0);
                p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p.getEyeLocation().add(righteye),1,0,0,0,0);
                count--;
                if(count <= 0){
                    this.cancel();
                }
            }
        }.runTaskTimer(Stones.getPlugin(), 0, 2);
    }
    
    
}
