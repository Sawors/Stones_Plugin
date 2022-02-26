package com.github.sawors.stones.siege.weapons;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.siege.SiegeUnit;
import com.github.sawors.stones.siege.StoneProjectile;
import com.github.sawors.stones.siege.StonesSiege;
import net.kyori.adventure.key.Key;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Mortar implements SiegeUnit {
    
    Block core;
    Vector direction;
    Location cannonend;
    StoneProjectile projectile = StoneProjectile.BLANK;
    int cannonlength;
    int power = 4;
    
    Mortar(Block core, Location cannonend, Vector direction, StoneProjectile projectile) {
        this.core = core;
        this.cannonend = cannonend;
        this.direction = direction;
        this.projectile = projectile;
    }
    public Mortar(Block core) {
        create(core);
    }
    Mortar() {
    }
    
    @Override
    public void create(Block core){
        if(core.getType().equals(Material.DISPENSER) && ((Dispenser) core.getState()).getInventory().getItem(4) != null){
            Block cannon = core.getRelative(BlockFace.UP);
            Location cannonend = cannon.getLocation();
            Vector dirunit = new Vector(0,1,0);
            Vector direction = dirunit;
                if(core.getLocation().clone().add(.5,.5,.5).add(dirunit).getBlock().getType().equals(Material.POLISHED_BASALT)){
                    for(int i2 = 1; i2 <= 5; i2++){
                        cannonend = core.getLocation().add(.5,.5,.5).add(dirunit.clone().multiply(i2));
                        if(cannonend.getBlock().getType().isAir()){
                            //cannon end successfully acquired
                            direction = StonesSiege.getSpyglassDirection(core.getLocation().add(.5,.5,.5), dirunit, 1.5, 45);
                            if(direction.length() <= 0){
                                direction = dirunit;
                            }
                            this.core = core;
                            this.cannonend = cannonend;
                            this.direction = direction;
                            return;
                        }
                    }
                }
        }
    }
    
    @Override
    public boolean check() {
        return core != null && core.getType().equals(Material.DISPENSER) && direction.length() > 0 && cannonend != null && cannonend.distance(core.getLocation().add(.5, .5, .5)) > .5;
    }
    
    public static boolean checkCannon(Block corecheck) {
        if(corecheck.getType().equals(Material.DISPENSER)){
            Vector dirunit = new Vector(0,1,0);
            if(corecheck.getLocation().clone().add(.5,.5,.5).add(dirunit).getBlock().getType().equals(Material.POLISHED_BASALT)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void shoot() {
        if(this.check() && !this.cannonend.getBlock().getType().isSolid()){
            //System.out.println("new cannon :"+"\n"+core.getType()+"\n"+cannonend+"\n"+direction+"\n"+power+"\n"+cannonlength );
            //we do this to create a unit vector in order to then apply the correct power to the projectile
            direction = direction.multiply(1/direction.length());
            cannonlength = (int) core.getLocation().add(.5,.5,.5).distance(cannonend);
            direction = direction.setX(.1);
            //direction.setY(0);
            
            cannonend = cannonend.subtract(direction.clone().multiply(0.4));
            Location coreloc = core.getLocation().add(0.5,0.5,0.5);
            for(LivingEntity e : core.getLocation().add(0.5,0.5,0.5).getNearbyLivingEntities(3)){
                Location eloc = e.getLocation().clone().add(0,1,0);
                Vector recoil = new Vector(eloc.getX()-coreloc.getX(), eloc.getY()-coreloc.getY(),eloc.getZ()-coreloc.getZ());
                recoil = recoil.multiply(1/recoil.length()).setY(recoil.getY()/2);
                e.setVelocity(e.getVelocity().add(recoil));
            }
            for(LivingEntity e : cannonend.add(direction).getNearbyLivingEntities(2+direction.getX(),2+direction.getY(),2+direction.getZ())){
                e.setFireTicks(60);
                e.damage(2);
                e.playSound(net.kyori.adventure.sound.Sound.sound(Key.key("minecraft:sawors.deaf"), net.kyori.adventure.sound.Sound.Source.PLAYER,.75f,1));
            }
            
            World world = core.getWorld();
            float accuracy = (-2*cannonlength)+11;
            float pitch = (float) (((cannonlength)*0.04)-.04);
            
            for(int foo = 8-cannonlength; foo >= 0; foo--){
                world.playSound(cannonend, Sound.ENTITY_BLAZE_SHOOT,4,.8f+pitch);
                world.playSound(cannonend, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,2,1.2f);
                world.playSound(cannonend, Sound.ENTITY_GENERIC_EXPLODE,4, (float) (1+(Math.random()-0.5)/5)+pitch);
            }
            
            
            for(int corecloud = 6; corecloud >= 0; corecloud--){
                world.spawnParticle(Particle.CLOUD, core.getLocation().add(0.5+((Math.random()-0.5)/2),.6+((12-corecloud)/12f),0.5+((Math.random()-0.5)/2)), 0,0,.1,0,.8);
            }
            for(int endcloud = 12; endcloud >= 0; endcloud--){
                for(float projcloud = cannonlength/2f; projcloud >= 0; projcloud-=.25){
                    world.spawnParticle(Particle.REDSTONE, cannonend.clone().add((Math.random()-0.5),(Math.random()-0.5),(Math.random()-0.5)).add(direction.clone().multiply(projcloud)).subtract(direction),0, direction.getX(),direction.getY(),direction.getZ(),.1,new Particle.DustOptions(Color.GRAY, 2));
                    world.spawnParticle(Particle.CLOUD, cannonend.clone().add((Math.random()-0.5)/2,(Math.random()-0.5)/2,(Math.random()-0.5)/2).add(direction.clone().multiply(projcloud)).subtract(direction),0, direction.getX()/2,direction.getY()/2,direction.getZ()/2,.2);
                    world.spawnParticle(Particle.REDSTONE, cannonend.clone().add((Math.random()-0.5)/4,(Math.random()-0.5)/4,(Math.random()-0.5)/4).add(direction.clone().multiply(projcloud)).subtract(direction),0, direction.getX(),direction.getY(),direction.getZ(),.3,new Particle.DustOptions(Color.ORANGE, 1));
                    
                }
                world.spawnParticle(Particle.FLAME, cannonend.clone().add((Math.random()-0.5)/4,(Math.random()-0.5)/4,(Math.random()-0.5)/4).add(direction),1, .2*direction.getX(),.2*direction.getY(),.2*direction.getZ(),.05);
                world.spawnParticle(Particle.CLOUD, cannonend.clone().add(direction.clone().multiply(1.5)),8, 1+(direction.getZ()/2),1,1+(direction.getX()/2),.05);
                world.spawnParticle(Particle.REDSTONE, core.getLocation().add(0.5,-.75,0.5),6, 3,.5,3,.05, new Particle.DustOptions(Color.WHITE,.75f));
            }
            
            
            Arrow a = core.getWorld().spawnArrow(cannonend, direction, power,accuracy);
            StonesSiege.registerProjectile(a.getUniqueId(), projectile);
            a.setSilent(true);
            a.setBounce(false);
            a.setCustomName("cannonball");
            a.setDamage(30);
            
            final Color color = Color.RED;
            boolean showtrajectory = false;
            //Color.fromRGB((int) (Math.random() * 255),(int) (Math.random() * 255),(int) (Math.random() * 255));
            new BukkitRunnable(){
                int timer = 12*20;
                
                @Override
                public void run() {
                    //System.out.println(a.getVelocity().lengthSquared());
                    //a.setVelocity(a.getVelocity().setY(a.getVelocity().getY()-accuracy));
                    if(showtrajectory){
                        Location loc = a.getLocation();
                        new BukkitRunnable(){
                            int timer2 = 16;
                            final Location thisloc = loc;
                            
                            @Override
                            public void run() {
                                a.getWorld().spawnParticle(Particle.REDSTONE, this.thisloc,4,0,0,0,0,new Particle.DustOptions(color, 1));
                                timer2 --;
                                
                                if(timer2 <= 0){
                                    StonesSiege.projectileEntityRemove(a);
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Stones.getPlugin(), 0,10);
                    }
                    timer --;
                    
                    if(timer <= 0){
                        StonesSiege.projectileEntityRemove(a);
                        this.cancel();
                    }
                }
            }.runTaskTimer(Stones.getPlugin(), 1,1);
        }
    }
}
