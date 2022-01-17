package com.github.sawors.stones.features;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.enums.StoneEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class StonesEffects implements Listener {
    
    public static void doEffects(){
    
        new BukkitRunnable(){
            @Override
            public void run(){
                for(int i =0; i<Stones.getEffectmap().keySet().toArray().length;i++){
                    UUID id = (UUID) Stones.getEffectmap().keySet().toArray()[i];
                    if(Bukkit.getPlayer(id) != null && Objects.requireNonNull(Bukkit.getPlayer(id)).isOnline() && Stones.getEffectmap().containsKey(id)){
                        Player p = Bukkit.getPlayer(id);
                        ArrayList<StoneEffect> effectlist = Stones.effectmapGetEntry(id);
                        if(effectlist.size() > 0 && p != null){
                            for(StoneEffect effect : effectlist){
                                switch (effect){
                                    case CARRY:
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, (int) Math.sqrt(6*p.getPassengers().get(0).getBoundingBox().getVolume()), false, false));
                                }
                                
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Stones.getPlugin(), 0, 80);
        
        
    }
    
    
}
