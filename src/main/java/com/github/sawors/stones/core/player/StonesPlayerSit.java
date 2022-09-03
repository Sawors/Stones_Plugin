package com.github.sawors.stones.core.player;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;

public class StonesPlayerSit implements Listener {
    
    @EventHandler
    public void playerSit(PlayerInteractEvent event){
        if(event.getClickedBlock() != null && !event.getPlayer().isSneaking() && event.getAction().isRightClick() && ((event.getClickedBlock().getType().toString().contains("STAIRS") && ((Stairs) event.getClickedBlock().getBlockData()).getShape().equals(Stairs.Shape.STRAIGHT) && ((Stairs) event.getClickedBlock().getBlockData()).getHalf().equals(Bisected.Half.BOTTOM)) || (event.getClickedBlock().getType().toString().contains("SLAB") && ((Slab) event.getClickedBlock().getBlockData()).getType().equals(Slab.Type.BOTTOM))) && event.getPlayer().getInventory().getItemInMainHand().getType().isAir()){
            Block b = event.getClickedBlock();
            Location loc = b.getLocation().add(0.5,0.5,0.5);
            Player p = event.getPlayer();
            if(
                    loc.clone().add(1,0,0).getBlock().getType().toString().contains("TRAPDOOR") || loc.clone().add(1,0,0).getBlock().getType().toString().contains("SIGN") ||
                    loc.clone().add(-1,0,0).getBlock().getType().toString().contains("TRAPDOOR") || loc.clone().add(1,0,0).getBlock().getType().toString().contains("SIGN") ||
                    loc.clone().add(0,0,1).getBlock().getType().toString().contains("TRAPDOOR") || loc.clone().add(1,0,0).getBlock().getType().toString().contains("SIGN") ||
                    loc.clone().add(0,0,-1).getBlock().getType().toString().contains("TRAPDOOR") || loc.clone().add(1,0,0).getBlock().getType().toString().contains("SIGN") ||
                    loc.clone().add(0,-1,0).getBlock().getType().toString().contains("TRAPDOOR")
            ){
                loc.setYaw(p.getLocation().getYaw()+180);
                StonesPlayerSit.sitEntity(p, loc);
            }
            
            
        }
    }
    
    @EventHandler
    public void onPlayerLeaveSit(EntityDismountEvent event){
        if(event.getEntity() instanceof Player && event.getDismounted() instanceof ArmorStand){
            Player player = (Player) event.getEntity();
            ArmorStand seat = (ArmorStand) event.getDismounted();
            if(seat.getCustomName() != null && seat.getCustomName().contains("seat")){
                seat.remove();
                player.teleport(player.getLocation().add(0,1,0));
            }
        }
    }
    
    /**
     * Used to sit an entity at its location. Conditions : entity must : be on ground, not swimming, not jumping
     * @param entity the entity to sit
     */
    public static void sitEntity(LivingEntity entity){
        if(entity.getLocation().subtract(0,0.1,0).getBlock().isSolid() && !entity.isJumping() && !entity.isSwimming()){
            ArmorStand e = UsefulThings.createDisplay(entity.getLocation().subtract(0,1,0), new ItemStack(Material.AIR));
            e.setSmall(true);
            e.addPassenger(entity);
            
        } else {
            if(entity instanceof Player){
                entity.sendActionBar(Component.text(ChatColor.RED + "you must be on ground to sit"));
            }
        }
    }
    
    
    
    /**
     * Used to sit an entity at its location. Conditions : entity must : be on ground, not swimming, not jumping
     * @param entity the entity to sit
     * @param forcesit if the sit should be forced or not (ignore Conditions)
     */
    public static void sitEntity(LivingEntity entity, boolean forcesit){
        if(forcesit){
            ArmorStand e = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation().subtract(0,1,0), EntityType.ARMOR_STAND);
            e.setGravity(false);
            e.setVisible(false);
            e.setInvulnerable(true);
            e.setSmall(true);
            e.setCustomName("seat");
            e.setCustomNameVisible(false);
            e.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);
            
            e.addPassenger(entity);
        } else {
            StonesPlayerSit.sitEntity(entity);
        }
    }
    
    /**
     * Used to sit an entity at it's location. Conditions : entity must : be on ground, not swimming, not jumping
     * @param entity the entity to sit
     * @param location where to sit the entity
     */
    public static void sitEntity(LivingEntity entity, Location location){
        if(entity.getLocation().subtract(0,0.1,0).getBlock().isSolid() && !entity.isJumping() && !entity.isSwimming()){
            ArmorStand e = (ArmorStand) entity.getWorld().spawnEntity(location.subtract(0,1,0), EntityType.ARMOR_STAND);
            e.setGravity(false);
            e.setVisible(false);
            e.setInvulnerable(true);
            e.setSmall(true);
            e.setCustomName("seat");
            e.setCustomNameVisible(false);
            e.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);
            
            e.addPassenger(entity);
            
        } else {
            if(entity instanceof Player){
                entity.sendActionBar(Component.text(ChatColor.RED + "you must be on ground to sit"));
            }
        }
    }
    
    /**
     * Used to sit an entity at it's location. Conditions : entity must : be on ground, not swimming, not jumping
     * @param entity the entity to sit
     * @param location where to sit the entity
     * @param forcesit if the sit should be forced or not (ignore Conditions)
     */
    public static void sitEntity(LivingEntity entity, Location location, boolean forcesit){
        if(forcesit){
            ArmorStand e = (ArmorStand) entity.getWorld().spawnEntity(location.subtract(0,1,0), EntityType.ARMOR_STAND);
            e.setGravity(false);
            e.setVisible(false);
            e.setInvulnerable(true);
            e.setSmall(true);
            e.setCustomName("seat");
            e.setCustomNameVisible(false);
            e.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);
            
            e.addPassenger(entity);
        } else {
            StonesPlayerSit.sitEntity(entity, location);
        }
    }
}
