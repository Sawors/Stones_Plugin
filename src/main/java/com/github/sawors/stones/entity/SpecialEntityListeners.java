package com.github.sawors.stones.entity;

import com.github.sawors.stones.core.database.DataHolder;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

public class SpecialEntityListeners implements Listener {

    @EventHandler
    public void onSpecialEntityDeath(EntityDeathEvent event){
        if(event.getEntity().getPersistentDataContainer().get(DataHolder.getSpecialEntity(), PersistentDataType.STRING) != null){
            LivingEntity e = event.getEntity();
            String ename = e.getPersistentDataContainer().get(DataHolder.getSpecialEntity(), PersistentDataType.STRING);
            if(ename != null) {
                switch (ename) {
                    case ("firefly"):
                        e.getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 64, 1, 1, 1, .15);
                        e.getWorld().playSound(e.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, .8f);
                        e.getLocation().getBlock().setType(Material.FIRE);
                        for (Entity i : e.getNearbyEntities(3, 3, 3)) {
                            i.setFireTicks(20 * 3);
                        }
                }
            }
        }
    }

    @EventHandler
    public void onSpecialEntityMove(EntityMoveEvent event){
        if(event.getEntity().getPersistentDataContainer().get(DataHolder.getSpecialEntity(), PersistentDataType.STRING) != null){
            LivingEntity e = event.getEntity();
            String ename = e.getPersistentDataContainer().get(DataHolder.getSpecialEntity(), PersistentDataType.STRING);
            if(ename != null) {
                switch (ename) {
                    case ("firefly"):
                        e.getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 2, .1, .1, .1, 0);
                        if (event.hasChangedBlock() && Math.random() <= 0.5 && e.getLocation().getBlock().getType().isAir()) {
                            e.getLocation().getBlock().setType(Material.FIRE);
                        }
                        if (e.getFireTicks() > 0) {
                            e.setFireTicks(0);
                        }
                        if (e.isInWaterOrRainOrBubbleColumn()) {
                            if (event.hasChangedBlock()) {
                                //if(e.getWorld().getHighestBlockAt(e.getLocation()).getY() < e.getLocation().getY()){
                                e.getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getLocation(), 2, .1, .1, .1, .1);
                                e.getWorld().playSound(e.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, .5f, .8f);
                                e.damage(1);
                                //}
                            }
                        }
                }
            }
        }
    }
}
