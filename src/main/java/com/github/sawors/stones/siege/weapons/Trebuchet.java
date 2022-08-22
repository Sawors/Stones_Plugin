package com.github.sawors.stones.siege.weapons;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.StonesMaterial;
import com.github.sawors.stones.core.database.DataHolder;
import com.github.sawors.stones.core.player.SPlayerAction;
import com.github.sawors.stones.core.player.StonesPlayerData;
import com.github.sawors.stones.siege.SiegeUnit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Trebuchet implements SiegeUnit, Listener {
    
    float length;
    Vector direction;
    Vector coretoaxle;
    Vector axle;
    int power;
    ArrayList<Entity> projectiles;
    Block core;
    
    
    public Trebuchet(Block core){
        create(core);
    }
    
    @Override
    public void shoot() {
        projectiles = getProjectiles();
        if(power >=1 && length >= 3 && projectiles.size() >= 1){
            
            for(Entity e : projectiles){
                Location baseloc = e.getLocation();
                Vector reverseaxle = coretoaxle.clone().multiply(-1);
                new BukkitRunnable(){
                    final int timermax = 10;
                    int timer = timermax;
                    Location tploc;
                    
                    @Override
                    public void run(){
                        Vector projection = new Vector(direction.clone().multiply(length).getX()*1,2,direction.clone().multiply(length).getZ()*1);
                        if(timer <= 0){
                            if(e instanceof Player){
                                ArmorStand arm =(ArmorStand) e.getWorld().spawnEntity(e.getLocation(), EntityType.ARMOR_STAND);
                                arm.setInvisible(true);
                                arm.setSmall(true);
                                arm.setInvulnerable(true);
                                arm.setVelocity(projection);
                                arm.addPassenger(e);
                                DataHolder.addToRemoveList(arm.getUniqueId());
                                StonesPlayerData.logAction(e.getUniqueId(), SPlayerAction.LAUNCHED);
                            } else {
                                e.setVelocity(projection);
                            }
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    e.setFallDistance(0);
                                    if(e instanceof Player && e.getVehicle() != null && e.getVehicle().isOnGround()){
                                        DataHolder.removeFromRemoveList(e.getVehicle().getUniqueId());
                                        e.getVehicle().remove();
                                        this.cancel();
                                        new BukkitRunnable(){
                                            @Override
                                            public void run() {
                                                StonesPlayerData.getActions(e.getUniqueId()).remove(SPlayerAction.LAUNCHED);
                                            }
                                        }.runTaskLater(Stones.getPlugin(),10);
                                        return;
                                    } else if(e.isOnGround() || e instanceof Bee || e instanceof Bat || e instanceof Flying || e instanceof EnderDragon){
                                        this.cancel();
                                        return;
                                    }
                                    e.getWorld().spawnParticle(Particle.CLOUD,e.getLocation(),4,.12,.12,.12,0);
                                    
                                }
                            }.runTaskTimer(Stones.getPlugin(), 1,2);
                            this.cancel();
                            return;
                        }
                        tploc = baseloc.clone().add(coretoaxle.clone().add(reverseaxle.rotateAroundAxis(axle, Math.toRadians(135f/timermax))));
                        tploc.setYaw(e.getLocation().getYaw());
                        tploc.setPitch(e.getLocation().getPitch());
                        if(!tploc.getBlock().getType().isOccluding()){
                            e.teleport(tploc);
                            e.getWorld().spawnParticle(Particle.CLOUD,tploc,4,.12,.12,.12,0);
                        } else {
                            return;
                        }
                        
                        timer--;
                    }
                }.runTaskTimer(Stones.getPlugin(), 10, 1);
            }
        }
    }
    
    
    
    @Override
    public void create(Block core) {
        if(core.getRelative(BlockFace.UP).getType().toString().contains("TRAPDOOR")){
            Vector dirunit = new Vector(1,1,0);
            Location checkloc;
            for(int i = 0; i<4; i++){
                dirunit = new Vector(1,1,0).rotateAroundY(Math.toRadians(90f*i));
                checkloc = core.getLocation().clone().add(.5,.5,.5).add(dirunit);
                if(StonesMaterial.isWoodFullBlock(checkloc.getBlock())){
                    dirunit.setX((int)dirunit.getX());
                    dirunit.setY((int)dirunit.getY());
                    dirunit.setZ((int)dirunit.getZ());
                    this.direction = dirunit;
                    break;
                }
            }
            
            this.power = 1;
            this.core = core;
            this.coretoaxle = new Vector(0,0,0);
            this.axle = direction.clone().setY(0).rotateAroundY(Math.toRadians(90));
            Location loc;
            for(int i = 1; i<=24; i++){
                loc = core.getLocation().clone().add(0.5,0.5,0.5).add(dirunit.clone().multiply(i));
                if(loc.getBlock().getType().equals(Material.OAK_LOG)){
                    coretoaxle = direction.clone().multiply(i);
                }
                if(!StonesMaterial.isWoodFullBlock(loc.getBlock())){
                    this.length = i-1;
                    return;
                }
            }
        }
    }
    
    @Override
    public boolean check() {
        return false;
    }
    
    public static boolean checkCannon(Block core){
        return core.getRelative(BlockFace.UP).getType().toString().contains("TRAPDOOR");
    }
    
    public ArrayList<Entity> getProjectiles(){
        Block basket = core.getRelative(BlockFace.UP);
        return new ArrayList<>(basket.getLocation().add(0.5, 1, 0.5).getNearbyEntities(.5,1,.5));
        
    }
}
