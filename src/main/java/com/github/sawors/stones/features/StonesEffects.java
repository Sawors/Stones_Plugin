package com.github.sawors.stones.features;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.DataHolder;
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
                for(int i = 0; i< DataHolder.getEffectmap().keySet().toArray().length; i++){
                    UUID id = (UUID) DataHolder.getEffectmap().keySet().toArray()[i];
                    if(Bukkit.getPlayer(id) != null && Objects.requireNonNull(Bukkit.getPlayer(id)).isOnline() && DataHolder.getEffectmap().containsKey(id)){
                        Player p = Bukkit.getPlayer(id);
                        ArrayList<StoneEffect> effectlist = DataHolder.effectmapGetEntry(id);
                        if(effectlist.size() > 0 && p != null){
                            for(StoneEffect effect : effectlist){
                                switch (effect){
                                    case CARRY:
                                        if(p.getPassengers().size() > 0){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, (int) Math.sqrt(4*p.getPassengers().get(0).getBoundingBox().getVolume()), false, false));
                                        } else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 2, false, false));
                                        }
                                }
                                
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Stones.getPlugin(), 0, 80);
        
        
    }
    
    
}
