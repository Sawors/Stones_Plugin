package com.github.sawors.stones.commandexecutors;

import com.github.sawors.stones.Stones;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class StestCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            
            Player p = (Player) sender;
            Vector axisv = p.getLocation().getDirection();
            Vector v = axisv.clone().setX(-axisv.getY()).setY(axisv.getX());
            Location loc = p.getLocation().add(0,1,0).add(axisv);
            v.multiply(1.5);
            final int maxstep = 80;
            new BukkitRunnable(){
                int count = maxstep;
                
                @Override
                public void run() {
                    
                    v.rotateAroundAxis(axisv, Math.toRadians(8*360f/maxstep));
                    p.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(v), 1,0,0,0,0);
                    
                    count--;
                    if(count <= 0){
                        this.cancel();
                    }
                    
                }
            }.runTaskTimer(Stones.getPlugin(),1,1);
    
            //DataHolder.effectmapAdd(p.getUniqueId(), StoneEffect.DEMON);
            final Location ploc = p.getLocation();
            final double x = ploc.getX();
            final double y = ploc.getY()+1;
            final double z = ploc.getZ();
            new BukkitRunnable(){
                int count = 80;
                @Override
                public void run() {
                    count--;
                    p.getLocation().getWorld().spawnParticle(Particle.REDSTONE,null,null,x,y,z,0,0,1,0,1,new Particle.DustOptions(Color.AQUA, 2),false);
                    p.getLocation().getWorld().spawnParticle(Particle.GLOW_SQUID_INK,ploc.clone().add(0,1,0),0,0,1,0,2);
                    if(count<=0){
                        this.cancel();
                    }
                }
            }.runTaskTimer(Stones.getPlugin(), 0,1);
            
            
            return true;
        }
        return false;
    }
}
