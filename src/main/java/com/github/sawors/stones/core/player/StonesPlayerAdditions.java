package com.github.sawors.stones.core.player;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Chain;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class StonesPlayerAdditions implements Listener {
    
    //
    //  COMPASS NORTH
    @EventHandler
    public void setCompassNorth(PlayerChangedWorldEvent event){
        new BukkitRunnable(){
            @Override
            public void run() {
                Player p = event.getPlayer();
                p.setCompassTarget(new Location(p.getWorld(), 0,0,-1000000));
                p.updateInventory();
            }
        }.runTask(Stones.getPlugin());
        
    }
    @EventHandler
    public void setCompassNorth(PlayerJoinEvent event){
        new BukkitRunnable(){
            @Override
            public void run() {
                Player p = event.getPlayer();
                p.setCompassTarget(new Location(p.getWorld(), 0,0,-1000000));
                p.updateInventory();
            }
        }.runTask(Stones.getPlugin());
    }
    //
    //  DISABLE RESPAWN ANCHORS
    @EventHandler(priority = EventPriority.LOW)
    public void disableRespawnAnchors(PlayerInteractEvent event){
        if(event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.RESPAWN_ANCHOR) && event.getAction().isRightClick() && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GLOWSTONE)){
            event.setCancelled(true);
        }
    }
    //
    //  CHAIN CLIMB
    @EventHandler
    public void chainClimber(PlayerInteractEvent event){
        Player p = event.getPlayer();
    
        if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHAIN && p.getLocation().add(0,1,0).getBlock().getType().equals(Material.CHAIN) && ((Chain) p.getLocation().add(0,1,0).getBlock().getBlockData()).getAxis().equals(Axis.Y)){
            if(p.isSneaking()){
                p.setVelocity(new Vector(0,.25,0));
                p.getWorld().playSound(p.getLocation().add(0,1,0), Sound.BLOCK_CHAIN_STEP, .25f, 1);
            }else{
                p.setVelocity(new Vector(0,.33,0));
                p.getWorld().playSound(p.getLocation().add(0,1,0), Sound.BLOCK_CHAIN_STEP, 1, 1);
            }
        }
    }
    //
    //  OPEN DOUBLE DOORS
    @EventHandler
    public void onPlayerOpenDoor(PlayerInteractEvent event){
        Block b = event.getClickedBlock();
        if(b != null && b.getType().toString().contains("_DOOR") && event.getAction().isRightClick()){
            //event.setCancelled(true);
            Door door = (Door) b.getBlockData().clone();
        
        
        
            //DOUBLE DOOR LOGIC
            if(!event.useInteractedBlock().equals(Event.Result.DENY) && !event.getPlayer().isSneaking()) {
                Block b1;
                Block b2;
                if (door.getFacing().equals(BlockFace.NORTH) || door.getFacing().equals(BlockFace.SOUTH)) {
                    b1 = b.getLocation().add(1, 0, 0).getBlock();
                    b2 = b.getLocation().add(-1, 0, 0).getBlock();
                
                
                } else {
                    b1 = b.getLocation().add(0, 0, 1).getBlock();
                    b2 = b.getLocation().add(0, 0, -1).getBlock();
                }
                if (b1.getType().toString().contains("_DOOR") && ((Door) b1.getBlockData()).getHinge() != door.getHinge()) {
                    Door d1 = (Door) b1.getBlockData().clone();
                    if (door.isOpen()) {
                        d1.setOpen(false);
                        b1.getWorld().playSound(b1.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);
                    } else {
                        d1.setOpen(true);
                        b1.getWorld().playSound(b1.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_DOOR_OPEN, 1, 1);
                    }
                    b1.setBlockData(d1);
                    b1.getState().update();
                
                }
                if (b2.getType().toString().contains("_DOOR") && ((Door) b2.getBlockData()).getHinge() != door.getHinge()) {
                    Door d2 = (Door) b2.getBlockData().clone();
                    if (door.isOpen()) {
                        d2.setOpen(false);
                        b2.getWorld().playSound(b1.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);
                    } else {
                        d2.setOpen(true);
                        b2.getWorld().playSound(b1.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_DOOR_OPEN, 1, 1);
                    }
                    b2.setBlockData(d2);
                    b2.getState().update();
                }
            }
        }
        if(b != null && b.getType().toString().contains("_DOOR") && event.getAction().isLeftClick() && event.getPlayer().getInventory().getItemInMainHand().getType().isAir()){
            if(event.getPlayer().isSneaking()){
                b.getWorld().playSound(b.getLocation().add(.5,0,.5), "minecraft:sawors.door.knock", .25f, UsefulThings.randomPitchSimple()-0.5f);
            }else{
                b.getWorld().playSound(b.getLocation().add(.5,0,.5), "minecraft:sawors.door.knock", 1, UsefulThings.randomPitchSimple());
            }
        }
    }
    //
    //  TORCH BURN BLOCKS WHEN DROPPED
    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event){
        if(event.getEntity().getItemStack().getType() == Material.TORCH && Math.random() < 0.5){
            event.getEntity().getLocation().getBlock().setType(Material.FIRE);
        }
    }
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event){
        if(event.getEntity().getItemStack().getType() == Material.TORCH){
            event.getEntity().setTicksLived(4800);
        }
    }
    //
    //  ARROW SOUND + BREAK BLOCKS
    @EventHandler
    public void onArrowHit(ProjectileHitEvent event){
        if(event.getHitBlock() != null && event.getEntity() instanceof Arrow){
            Arrow arrow = (Arrow) event.getEntity();
            Block block = event.getHitBlock();
            if(block.getType().getBlastResistance() > 1){
                if(Math.random() < block.getType().getBlastResistance()/10){
                    arrow.getWorld().spawnParticle(Particle.ITEM_CRACK, arrow.getLocation(),2,.1,.1,.1,.1,new ItemStack(Material.STICK));
                    arrow.getWorld().spawnParticle(Particle.ITEM_CRACK, arrow.getLocation(),4,.1,.1,.1,.1,new ItemStack(Material.IRON_INGOT));
                    block.getWorld().playSound(block.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, UsefulThings.randomPitchSimple()+2f);
                    arrow.remove();
                } else {
                    block.getWorld().playSound(block.getLocation(), block.getSoundGroup().getPlaceSound(), 1f, UsefulThings.randomPitchSimple()+0.5f);
                }
            } else {
                arrow.getWorld().spawnParticle(Particle.BLOCK_CRACK, arrow.getLocation(),6,.1,.1,.1,.1,block.getBlockData());
                block.getWorld().playSound(block.getLocation(), block.getSoundGroup().getPlaceSound(), 1f, UsefulThings.randomPitchSimple()+0.2f);
            }
        
            // switch for sound
        
        
        
            //arrow break reaction
            if(block.getType().toString().contains("GLASS_PANE")){
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1,1.2f);
                        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getX()+0.5, block.getY()+0.5, block.getZ()+0.5,16, 0, 0,0, block.getBlockData());
                        block.getWorld().spawnParticle(Particle.FLAME, block.getLocation(), 1);
                        block.breakNaturally();
                    }
                }.runTaskLater(Stones.getPlugin(), 1);
            }
        }
    }
    //
    //  FOOD BUFFS
    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event){
        Player p = event.getPlayer();
        ItemStack consumed_item = event.getItem();
    
        if(consumed_item.getType() == Material.COOKED_MUTTON || consumed_item.getType() == Material.COOKED_PORKCHOP || consumed_item.getType() == Material.COOKED_BEEF){
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2, false));
        }
    }
    //
    //  REMOVE XP
    @EventHandler
    public void onXpSpawn(EntityAddToWorldEvent event){
        if(event.getEntity() instanceof ExperienceOrb){
            event.getEntity().remove();
        }
    }
    
}
