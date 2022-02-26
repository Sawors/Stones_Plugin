package com.github.sawors.stones.siege;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.items.StoneItem;
import com.github.sawors.stones.items.StonesItems;
import com.github.sawors.stones.siege.weapons.Mortar;
import com.github.sawors.stones.siege.weapons.StraightCannon;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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
    
    //TODO :
    //  - add a difference between explosions / impact / penetration
    //  - fix the projectile system
    
    
    
    private static HashMap<UUID, StoneProjectile> projectilemap = new HashMap<>();
    private static final ProtocolManager pmanager = Stones.getProtocolManager();
    
    @EventHandler
    public void cannonShoot(PlayerInteractEvent event){
        Block core = event.getClickedBlock();
        if(core != null && core.getType() == Material.DISPENSER && event.getAction().isRightClick() && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.FLINT_AND_STEEL)){
            org.bukkit.block.data.type.Dispenser disp = (org.bukkit.block.data.type.Dispenser) core.getBlockData();
            switch(disp.getFacing()){
                case NORTH:
                case EAST:
                case WEST:
                case SOUTH:
                    if(StraightCannon.checkCannon(core)){
                        StraightCannon cannon = new StraightCannon(core);
                        event.setCancelled(true);
                        cannon.shoot();
                    }
                    break;
                case UP:
                case DOWN:
                    if(Mortar.checkCannon(core)){
                        Mortar mortar = new Mortar(core);
                        event.setCancelled(true);
                        mortar.shoot();
                    }
                    break;
            }
        }
    }
    
    @EventHandler
    public void autoshoot(BlockPreDispenseEvent event){
        Block core = event.getBlock();
        if(core.getType() == Material.DISPENSER){
            org.bukkit.block.data.type.Dispenser disp = (org.bukkit.block.data.type.Dispenser) core.getBlockData();
            switch(disp.getFacing()){
                case NORTH:
                case EAST:
                case WEST:
                case SOUTH:
                    if(StraightCannon.checkCannon(core)){
                        StraightCannon cannon = new StraightCannon(core);
                        event.setCancelled(true);
                        cannon.shoot();
                    }
                    break;
                case UP:
                case DOWN:
                    if(Mortar.checkCannon(core)){
                        Mortar mortar = new Mortar(core);
                        event.setCancelled(true);
                        mortar.shoot();
                    }
                    break;
            }
        }
        
    }
    
    @EventHandler
    public void onProjectileHitBlock(ProjectileHitEvent event){
        event.setCancelled(true);
        Projectile p = event.getEntity();
        
        if(containsProjectile(p.getUniqueId())){
            StoneProjectile type = getProjectile(p.getUniqueId());
            Location loc = p.getLocation();
            switch(type){
                case CANNON_BALL:
                    p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p.getLocation(),4,0,0,0,0);
                    StonesExplosions.doDirectionalExplosion(loc, 6, StoneExplosionPattern.CANNON_BALL, p.getVelocity().normalize());
                    projectileEntityRemove(p);
                    break;
                case BLANK:
                    p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p.getLocation(),4,0,0,0,0);
                    p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(),128,1.5,1.5,1.5,.1);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,2,.8f);
                    p.getWorld().createExplosion(p.getLocation(), 2,false);
                    projectileEntityRemove(p);
                    break;
            }
        }
    }
    
    @EventHandler
    public void onProjectileHitGlobal(ProjectileCollideEvent event){
    }
    
    
    //
    //      PROJECTILE MAP
    //
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
    
    public static Vector getSpyglassDirection(Location center, Vector cannondirection, double radius, double maxconeangle){
        ArrayList<Player> watchers = new ArrayList<>(center.getNearbyPlayers(radius));
        if(watchers.size() <= 0){
            return new Vector();
        }
        Player finalwatcher = watchers.get(0);
        
        watchers.removeIf(p -> !(p.getInventory().getItemInMainHand().getType().equals(Material.SPYGLASS) || p.getInventory().getItemInMainHand().getType().equals(Material.SPYGLASS)));
        for(Player p : watchers){
            if(p.getLocation().distance(center) < finalwatcher.getLocation().distance(center)){
                finalwatcher = p;
            }
        }
        if(!watchers.contains(finalwatcher)){
            return new Vector();
        }
        return finalwatcher.getEyeLocation().getDirection();
    }
    
    //
    //      PACKETS
    //
    static PacketAdapter blockprojectiles = new PacketAdapter(Stones.getPlugin(), PacketType.Play.Server.SPAWN_ENTITY) {
        @Override
        public void onPacketSending(PacketEvent event) {
            PacketContainer packet = event.getPacket();
            if(packet.getType() == PacketType.Play.Server.SPAWN_ENTITY){
                if(packet.getEntityTypeModifier().read(0) == EntityType.ARROW){
                    
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
    
    static PacketAdapter test = new PacketAdapter(Stones.getPlugin(), PacketType.Play.Server.SPAWN_ENTITY) {
        @Override
        public void onPacketSending(PacketEvent event) {
            PacketContainer packet = event.getPacket();
            if(packet.getType() == PacketType.Play.Server.SPAWN_ENTITY) {
                if (packet.getEntityTypeModifier().read(0) == EntityType.CHICKEN) {
                    packet.getEntityTypeModifier().write(0, EntityType.DOLPHIN);
                }
            }
        }
    };
    
    static PacketAdapter test2 = new PacketAdapter(Stones.getPlugin(), PacketType.Play.Server.PLAYER_INFO) {
        @Override
        public void onPacketSending(PacketEvent event) {
            PacketContainer packet = event.getPacket();
            ChatColor color = UsefulThings.randomChatColor();
            if(packet.getType() == PacketType.Play.Server.PLAYER_INFO) {
                PlayerInfoData basedata = packet.getPlayerInfoDataLists().read(0).get(0);
                PlayerInfoData pdata = new PlayerInfoData(WrappedGameProfile.fromOfflinePlayer(Bukkit.getOfflinePlayer(UUID.fromString("486c4ffe-667d-4be1-be4d-8acbbc3420a2"))),basedata.getLatency(), basedata.getGameMode(),basedata.getDisplayName());
                        //packet.getPlayerInfoDataLists().read(0).get(0);
                ArrayList<PlayerInfoData> data = new ArrayList<>();
                data.add(pdata);
                //Stones.adminLog(color+""+data.set(0,pdata));
                packet.getPlayerInfoDataLists().write(0,data);
                    //event.setCancelled(true);
                
            }
        }
    };
    
    
    
    @EventHandler(priority = EventPriority.HIGH)
    public static void onEnable(PluginEnableEvent event){
        Stones.getProtocolManager().addPacketListener(blockprojectiles);
        //Stones.getProtocolManager().addPacketListener(test2);
    }
}
