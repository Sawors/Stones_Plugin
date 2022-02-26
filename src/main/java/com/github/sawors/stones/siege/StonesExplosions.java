package com.github.sawors.stones.siege;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class StonesExplosions {
    
    
    public static void doExplosion(Location loc, float power, StoneExplosionPattern pattern){
        ArrayList<Vector> vectors = new ArrayList<>();
        
    }
    
    public static void doDirectionalExplosion(Location loc, float power, StoneExplosionPattern pattern, Vector direction){
        ArrayList<Vector> vectors = new ArrayList<>();
        direction = direction.normalize();
        Vector perpendicular = direction.clone().rotateAroundY(Math.toRadians(90));
        int coneangle = 45;
        int pathnumber = 180;
        
        
        if(power > 0){
            switch(pattern){
                case CANNON_BALL:
                    coneangle = 30;
                    vectors.add(direction);
                    for(int i = pathnumber; i>0; i--){
                        for(int i2 = coneangle; i2 >= 0; i2--){
                            vectors.add(direction.clone().rotateAroundAxis(perpendicular,Math.toRadians(i2)).rotateAroundAxis(direction, Math.toRadians(2*i)).multiply(power));
                        }
                    }
            
                    for(Vector path : vectors){
                        for(double i = path.length(); i>= 0; i-=.5){
                            Location bloc = loc.clone().add(path.clone().normalize().multiply(i));
                            bloc.getWorld().spawnParticle(Particle.FLAME,loc.clone().add(path.clone().normalize().multiply(i)),1,0,0,0,0);
                            
                            Block b = bloc.getBlock();
                            if(b.getType() != Material.AIR){
                                b.getWorld().spawnParticle(Particle.BLOCK_DUST,b.getLocation().add(.5,.5,.5),1,1,1,1,.5, b.getBlockData());
                                b.setType(Material.AIR);
                            }
                        }
                    }
            
                    break;
            }
        }
    }
}
