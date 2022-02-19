package com.github.sawors.stones.siege;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.github.sawors.stones.Stones;
import com.github.sawors.stones.items.StoneItem;
import com.github.sawors.stones.items.StonesItems;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import net.kyori.adventure.key.Key;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class StonesSiege implements Listener {
    
    private static HashMap<UUID, StoneProjectile> projectilemap = new HashMap<>();
    private static final ProtocolManager pmanager = Stones.getProtocolManager();
    
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
    
    @EventHandler
    public void autoshoot(BlockPreDispenseEvent event){
        Block core = event.getBlock();
            if(core.getType().equals(Material.DISPENSER) && ((Dispenser) core.getState()).getInventory().getItem(4) != null){
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
        }
    }
    
    public static void shootCanon(Block core, Location cannonend, Vector direction, StoneProjectile projectile, float power, int cannonlength ){
        //we do this to create a unit vector in order to then apply the correct power to the projectile
        direction = direction.multiply(1/direction.length());
        direction.setY(0);
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
        world.playSound(cannonend, Sound.ENTITY_BLAZE_SHOOT,4,.8f+pitch);
        for(int foo = 8-cannonlength; foo >= 0; foo--){
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
        
            Arrow a;
        
        a = (Arrow) registerProjectile(core.getWorld().spawnArrow(cannonend, direction, power,accuracy), projectile);
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
                                projectileEntityRemove(a);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Stones.getPlugin(), 0,10);
                }
                timer --;
                
                if(timer <= 0){
                    projectileEntityRemove(a);
                    this.cancel();
                }
            }
        }.runTaskTimer(Stones.getPlugin(), 1,1);
        
    }
    
    
    @EventHandler
    public void onProjectileHitBlock(ProjectileHitEvent event){
        Projectile p = event.getEntity();
        if(containsProjectile(p.getUniqueId())){
            StoneProjectile type = getProjectile(p.getUniqueId());
            switch(type){
                case CANNON_BALL:
                    Location loc = p.getLocation();
                    World wrld = p.getWorld();
                    wrld.createExplosion(loc, 1);
                    projectileEntityRemove(p);
            }
        }
    }
    
    @EventHandler
    public void onProjectileHitGlobal(ProjectileCollideEvent event){
    }
    
    
    
    
    
    
    //
    //      PROJECTILE MAP
    //
    public static Projectile registerProjectile(Projectile projectile, StoneProjectile projectiletype){
        if(!projectilemap.containsKey(projectile.getUniqueId())){
            projectilemap.put(projectile.getUniqueId(), projectiletype);
        }
        return projectile;
    }
    public static void registerProjectile(UUID id, StoneProjectile projectiletype){
        if(!projectilemap.containsKey(id)){
            projectilemap.put(id, projectiletype);
        }
    }
    
    public static boolean containsProjectile(UUID id){
        return projectilemap.containsKey(id);
    }
    
    public static StoneProjectile getProjectile(UUID id){
        return projectilemap.get(id);
    }
    
    public static void removeProjectile(UUID id){
        projectilemap.remove(id);
    }
    
    public static void projectileEntityRemove(Projectile p){
        removeProjectile(p.getUniqueId());
        for(Entity rider : p.getPassengers()){
            rider.remove();
        }
        p.remove();
    }
    
    //
    //      PACKETS
    //
    static PacketAdapter blockprojectiles = new PacketAdapter(Stones.getPlugin(), PacketType.Play.Server.SPAWN_ENTITY) {
        @Override
        public void onPacketSending(PacketEvent event) {
            PacketContainer packet = event.getPacket();
            if(packet.getType() == PacketType.Play.Server.SPAWN_ENTITY){
                String hex = "#"+Integer.toHexString(new java.awt.Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)).getRGB()).substring(2);
                ChatColor color = ChatColor.of(hex);
                if(packet.getEntityTypeModifier().read(0) == EntityType.ARROW){
                    // Entity Type
                    
                    
                    PacketContainer spawn = packet.shallowClone();
                    
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            if(containsProjectile(spawn.getUUIDs().read(0))){
                                spawn.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
                                spawn.getUUIDs().write(0, UUID.randomUUID());
                                try {
                                    Stones.getProtocolManager().sendServerPacket(event.getPlayer(), spawn);
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        PacketContainer equipment = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
                                        equipment.getIntegers().write(0,spawn.getIntegers().read(0));
                                        Pair<EnumWrappers.ItemSlot, ItemStack> pair = new Pair<>(EnumWrappers.ItemSlot.HEAD, StonesItems.get(StoneItem.CANNON_BALL));
                                        ArrayList<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
                                        list.add(pair);
                                        equipment.getSlotStackPairLists().write(0, list);
                                        try {
                                            Stones.getProtocolManager().sendServerPacket(event.getPlayer(), equipment);
                                        } catch (InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.runTaskLater(Stones.getPlugin(), 1);
                            }
                        }
                    }.runTaskLater(Stones.getPlugin(), 1);
                }
            }
        }
    };
    static PacketAdapter readequipment = new PacketAdapter(Stones.getPlugin(), PacketType.Play.Server.ENTITY_EQUIPMENT) {
        @Override
        public void onPacketSending(PacketEvent event) {
            PacketContainer packet = event.getPacket();
            if(packet.getType() == PacketType.Play.Server.ENTITY_EQUIPMENT){
                java.awt.Color your_color = new java.awt.Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
                String hex = "#"+Integer.toHexString(your_color.getRGB()).substring(2);
                ChatColor color = ChatColor.of(hex);
                for(int i = packet.getSlotStackPairLists().size()-1; i>= 0; i--){
                    for(int i2 = packet.getSlotStackPairLists().read(i).size()-1; i2>=0; i2--){
                        Stones.adminLog(color+String.valueOf(packet.getSlotStackPairLists().read(i).get(i2).toString()));
                    }
                }
            }
        }
    };
    @EventHandler(priority = EventPriority.HIGH)
    public static void onEnable(PluginEnableEvent event){
        Stones.getProtocolManager().addPacketListener(blockprojectiles);
        //Stones.getProtocolManager().addPacketListener(readequipment);
    }
}
