package com.github.sawors.stones.features;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.enums.StoneProjectile;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class StonesSiege implements Listener {
    
    private static HashMap<UUID, StoneProjectile> projectilemap = new HashMap<>();
    
    
    
    @EventHandler
    public void cannonShoot(PlayerInteractEvent event){
        Player p = event.getPlayer();
        Block core = event.getClickedBlock();
        
        
        if(event.getAction().isRightClick() && core != null){
            if(core.getType().equals(Material.DISPENSER) && p.getInventory().getItemInMainHand().getType().equals(Material.FLINT_AND_STEEL)){
                event.setCancelled(true);
                Block cannon = core.getRelative(BlockFace.NORTH);
                Location cannonend = cannon.getLocation();
                Vector dirunit = new Vector(0,0,1);
                for(int i = 0; i < 4; i++){
                    dirunit = new Vector(0,0,1).rotateAroundY(Math.toRadians(90*i));
                    if(core.getLocation().clone().add(.5,.5,.5).add(dirunit).getBlock().getType().equals(Material.POLISHED_BASALT)){
                        for(int i2 = 1; i2 <= 6; i2++){
                            cannonend = core.getLocation().add(.5,.5,.5).add(dirunit.clone().multiply(i2));
                            if(cannonend.getBlock().getType().isAir()){
                                //cannon end successfully acquired
                                
                                    shootCanon(core, cannonend, dirunit, StoneProjectile.CANNON_BALL, 3f, i2-1);
                                
                                
                                break;
                            } else if(cannonend.getBlock().getType() != Material.POLISHED_BASALT){
                                cannonend.getWorld().playSound(cannonend, Sound.ENTITY_VILLAGER_NO,1,1);
                                break;
                            }
                        }
                        break;
                    }
                }
                
                
                //Vector dir = p.getLocation().getDirection();
            }
            
            
            
            
            
        }
        
        
    }
    
    public static void shootCanon(Block core, Location cannonend, Vector direction, StoneProjectile projectile, float power, int cannonlength ){
        //we do this to create a unit vector in order to then apply the correct power to the projectile
        direction = direction.multiply(1/direction.length());
        direction.setY(0);
        
        for(Player p : core.getLocation().getNearbyPlayers(3)){
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10,4,false,false));
        }
        
        World world = core.getWorld();
        float accuracy = (-2*cannonlength)+11;
    
        float pitch = (float) (((cannonlength)*0.04)-.04);
        world.playSound(cannonend, Sound.ENTITY_BLAZE_SHOOT,UsefulThings.getVolume(16),.8f+pitch);
        for(int foo = 7-cannonlength; foo >= 0; foo--){
            world.playSound(cannonend, Sound.ENTITY_GENERIC_EXPLODE,UsefulThings.getVolume(16), (float) (1+(Math.random()-0.5)/5)+pitch);
        }
        
        
        for(int corecloud = 6; corecloud >= 0; corecloud--){
            world.spawnParticle(Particle.CLOUD, core.getLocation().add(0.5+((Math.random()-0.5)/2),.6+((12-corecloud)/12f),0.5+((Math.random()-0.5)/2)), 0,0,.1,0,.8);
        }
        for(int endcloud = 12; endcloud >= 0; endcloud--){
            for(float projcloud = cannonlength/2f; projcloud >= 0; projcloud-=.25){
                world.spawnParticle(Particle.REDSTONE, cannonend.clone().add((Math.random()-0.5),(Math.random()-0.5),(Math.random()-0.5)).add(direction.clone().multiply(projcloud)),0, direction.getX(),direction.getY(),direction.getZ(),.1,new Particle.DustOptions(Color.GRAY, 2));
                world.spawnParticle(Particle.CLOUD, cannonend.clone().add((Math.random()-0.5)/2,(Math.random()-0.5)/2,(Math.random()-0.5)/2).add(direction.clone().multiply(projcloud+1)),0, direction.getX(),direction.getY(),direction.getZ(),.2);
                world.spawnParticle(Particle.REDSTONE, cannonend.clone().add((Math.random()-0.5)/4,(Math.random()-0.5)/4,(Math.random()-0.5)/4).add(direction.clone().multiply(projcloud+1.5)),0, direction.getX(),direction.getY(),direction.getZ(),.3,new Particle.DustOptions(Color.ORANGE, 1));
                
            }
            world.spawnParticle(Particle.FLAME, cannonend.clone().add((Math.random()-0.5)/4,(Math.random()-0.5)/4,(Math.random()-0.5)/4).add(direction),1, .2*direction.getX(),.2*direction.getY(),.2*direction.getZ(),.05);
            world.spawnParticle(Particle.CLOUD, cannonend.clone().add(direction.clone().multiply(1.5)),2, 1+(direction.getZ()/2),1,1+(direction.getX()/2),.05);
        }
        
            Arrow a = core.getWorld().spawnArrow(cannonend, direction, power,accuracy);
        a.setBounce(false);
        a.setCustomName("cannonball");
        registerProjectile(a.getUniqueId(), projectile);
        
        
        final Color color = Color.fromRGB((int) (Math.random() * 255),(int) (Math.random() * 255),(int) (Math.random() * 255));
        new BukkitRunnable(){
            int timer = 12*20;
            
            @Override
            public void run() {
                //System.out.println(a.getVelocity().lengthSquared());
                //a.setVelocity(a.getVelocity().setY(a.getVelocity().getY()-accuracy));
                Location loc = a.getLocation();
                new BukkitRunnable(){
                    int timer2 = 16;
                    final Location thisloc = loc;
                    
                    @Override
                    public void run() {
                        a.getWorld().spawnParticle(Particle.REDSTONE, this.thisloc,4,0,0,0,0,new Particle.DustOptions(color, 1));
                        timer2 --;
                        
                        if(timer2 <= 0){
                            a.remove();
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Stones.getPlugin(), 0,10);
                timer --;
                
                if(timer <= 0){
                    a.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(Stones.getPlugin(), 1,1);
        //ArmorStand stand = UsefulThings.createDisplay(cannonend, StonesItems.get(StoneItem.CANNON_BALL));
        //stand.setGravity(true);
        //stand.setSmall(true);
        
        /*stand.setVelocity(direction.multiply(power));
        final Color color = Color.fromRGB((int) (Math.random() * 255),(int) (Math.random() * 255),(int) (Math.random() * 255));
        new BukkitRunnable(){
            int timer = 12*20;
            
            @Override
            public void run() {
                Location loc = stand.getLocation();
                new BukkitRunnable(){
                    int timer = 3*20;
                    final Location thisloc = loc;
                    
                    @Override
                    public void run() {
                        stand.getWorld().spawnParticle(Particle.REDSTONE, this.thisloc,1,0,0,0,0,new Particle.DustOptions(color, 1));
                        timer --;
            
                        if(timer <= 0){
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Stones.getPlugin(), 0,4);
                Location facing1 = stand.getLocation().clone().add(stand.getVelocity().multiply(1/stand.getVelocity().length()));
                Location facing2 = facing1.clone().multiply(2);
                Location facing3 = facing1.clone().multiply(3);
                stand.getWorld().spawnParticle(Particle.FLAME,facing1.getBlock().getLocation().clone().add(0.5,0.5,0.5),128,.5,.5,.5,0);
                if(facing1.getBlock().getType().isSolid() || facing2.getBlock().getType().isSolid() || facing3.getBlock().getType().isSolid()){
                    //stand.getWorld().spawnParticle(Particle.FLAME,facing.getBlock().getLocation().clone().add(0.5,0.5,0.5),128,.5,.5,.5,0);
                    stand.getWorld().createExplosion(stand, (float) power/4,false,true);
                    stand.remove();
                    this.cancel();
                }
                timer --;
                
                if(timer <= 0){
                    stand.getWorld().createExplosion(stand, (float) power/6,false,true);
                    stand.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(Stones.getPlugin(), 2,1);
        new BukkitRunnable(){
            @Override
            public void run() {
                stand.remove();
            }
        }.runTaskLater(Stones.getPlugin(), 20*8);*/
    }
    
    
    
    
    
    //
    //      PROJECTILE MAP
    //
    public static void registerProjectile(UUID id, StoneProjectile projectiletype){
        if(!projectilemap.containsKey(id)){
            Bukkit.getLogger().log(Level.INFO, id+" successfully registered");
            projectilemap.put(id, projectiletype);
        }
    }
    
    public static boolean containsProjectile(UUID id){
        return projectilemap.containsKey(id);
    }
    
    public static void removeProjectile(UUID id){
        projectilemap.remove(id);
    }
}
