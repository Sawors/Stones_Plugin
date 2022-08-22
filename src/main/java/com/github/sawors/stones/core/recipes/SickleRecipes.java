package com.github.sawors.stones.core.recipes;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.items.SItem;
import com.github.sawors.stones.items.StonesItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.Locale;

public class SickleRecipes implements Listener {
    
    
    @EventHandler
    public void onPlayerCutPlant(PlayerInteractEvent event){
        if(event.getClickedBlock() != null && !event.getClickedBlock().getType().isAir()){
            Block b = event.getClickedBlock();
            Player p = event.getPlayer();
            ItemStack item = p.getInventory().getItemInMainHand();
            byte globaltimer = 5;
            
            if(UsefulThings.isShortPlant(b) && event.getAction().isLeftClick()){
                for(Entity e : b.getLocation().add(0.5,1,0.5).getNearbyEntities(1,1,1)){
                    if(e.getType().equals(EntityType.ARMOR_STAND) && e.getCustomName() != null && (e.getCustomName().toLowerCase().contains("shovel") || e.getCustomName().toLowerCase().contains("sickle"))){
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            
            
            if(UsefulThings.isFlower(b.getLocation().add(0,1,0).getBlock()) && b.isSolid() && (b.getType().toString().contains("DIRT") || b.getType().toString().contains("GRASS_") || b.getType().toString().contains("PODZOL")) && event.getAction().isRightClick() && item.getType().toString().contains("SHOVEL")){
                Location mid = b.getLocation();
                mid.setX(b.getLocation().add(0,1,0).getBlock().getBoundingBox().getCenter().getX());
                mid.setY(b.getLocation().add(0,1,0).getBlock().getBoundingBox().getCenter().getY() +13/16f);
                mid.setZ(b.getLocation().add(0,1,0).getBlock().getBoundingBox().getCenter().getZ());
                for(Entity e : mid.getNearbyEntities(.5,.5,.5)){
                    if(e.getType().equals(EntityType.ARMOR_STAND)){
                        return;
                    }
                }
                event.setCancelled(true);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) ((globaltimer-1)/0.2), 2, false, false));
                ArmorStand stand2 = UsefulThings.createDisplay(mid.add((p.getLocation().getX() - b.getBoundingBox().getCenter().getX())/10,-2/16f,(p.getLocation().getZ()-b.getBoundingBox().getCenter().getZ())/10), item.clone(), true);
                stand2.setSmall(true);
                stand2.setCustomName("_shovel");
                stand2.setHeadPose(new EulerAngle(0, Math.toRadians(p.getLocation().getYaw()+180), 0));
                new BukkitRunnable(){
                    byte timer = (byte) ((globaltimer-1)/2);
                    
                    
                    @Override
                    public void run(){
                        if(timer <= -1){
                            stand2.remove();
                            this.cancel();
                            return;
                        }
                        stand2.setHeadPose(new EulerAngle(stand2.getHeadPose().getX()+Math.toRadians(10), stand2.getHeadPose().getY(), 0));
                        b.getWorld().spawnParticle(Particle.BLOCK_DUST, mid, 8,0,0,0, 0, b.getBlockData());
                        b.getWorld().playSound(mid, Sound.BLOCK_GRAVEL_BREAK, .5f, 1 + 1f/(4*timer));
                        if(timer == 0){
                            Material baseb = b.getType();
                            b.setType(Material.DIRT);
                            b.getWorld().spawnParticle(Particle.BLOCK_DUST, mid, 16,0,0,0, 0, b.getBlockData());
                            b.getWorld().playSound(mid, Sound.BLOCK_GRAVEL_BREAK, .5f, 1.25f);
                            b.setType(baseb);
                            b.getLocation().add(0,1,0).getBlock().breakNaturally();
                            if(b.getType().toString().contains("GRASS_")){
                                b.setType(Material.DIRT);
                            }
                            
                        }
                        timer --;
                    }
                }.runTaskTimer(Stones.getPlugin(), 1, 10);
            }
            
            
            
            
            if(UsefulThings.isShortPlant(b) && item.hasItemMeta() && item.getItemMeta().getLocalizedName().contains("sickle")){
                
                Location mid = b.getLocation();
                mid.setX(b.getBoundingBox().getCenter().getX());
                mid.setY(b.getBoundingBox().getCenter().getY() +13/16f);
                mid.setZ(b.getBoundingBox().getCenter().getZ());
                
                
                for(Entity e : mid.getNearbyEntities(.5,.5,.5)){
                    if(e.getType().equals(EntityType.ARMOR_STAND)){
                        return;
                    }
                }
                if(event.getAction().isRightClick()){
                    
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, globaltimer+5, 2, false, false));
                    
                    final ArmorStand stand = UsefulThings.createDisplay(mid.add((p.getLocation().getX() - b.getBoundingBox().getCenter().getX())/10,0,(p.getLocation().getZ()-b.getBoundingBox().getCenter().getZ())/10), item.clone(), true);
                    stand.setSmall(true);
                    stand.setCustomName("_sickle");
                    Location standloc = stand.getLocation();
                    stand.setHeadPose(new EulerAngle(0, Math.toRadians(p.getLocation().getYaw()+225), 0));
                    
                    new BukkitRunnable(){
                        byte timer = globaltimer;
                        
                        
                        @Override
                        public void run(){
                            if(timer <= -5){
                                stand.remove();
                                this.cancel();
                                return;
                            }
                            if(timer > 0){
                                standloc.setYaw(standloc.getYaw()-125f/globaltimer);
                                stand.teleport(standloc);
                            }
                            if(timer == 0){
                                Material baseb = b.getType();
                                b.setType(Material.GRASS);
                                b.getWorld().spawnParticle(Particle.BLOCK_DUST, mid, 8,0.2,0.2,0.2, 0, b.getBlockData());
                                b.getWorld().playSound(mid, Sound.ENTITY_SNOW_GOLEM_SHEAR, .25f, 1f);
                                b.getWorld().playSound(mid, Sound.BLOCK_GRASS_BREAK, .5f, 1.5f);
                                b.setType(baseb);
                                if(!UsefulThings.plantToItem(b).getType().isAir()){
                                    b.getWorld().dropItem(mid, SickleRecipes.sickleCut(b));
                                }
                                b.setType(Material.AIR);
                            }
                            timer --;
                        }
                    }.runTaskTimer(Stones.getPlugin(), 1, 1);
                } else {
                    event.setCancelled(true);
                    b.setType(Material.GRASS);
                    b.getWorld().spawnParticle(Particle.BLOCK_DUST, mid, 8,0.2,0.2,0.2, 0, b.getBlockData());
                    b.getWorld().dropItem(mid, StonesItems.get(SItem.THATCH));
                    b.setType(Material.AIR);
                    
                    
                    
                    
                }
                
            }
        }
    }
    
    
    public static ItemStack sickleCut(ItemStack item){
    
        String type = item.getItemMeta().getLocalizedName().toLowerCase(Locale.ENGLISH);
        if(!item.hasItemMeta() || (item.hasItemMeta() && !item.getItemMeta().hasLocalizedName()) || (item.hasItemMeta() && item.getItemMeta().getLocalizedName().length()<1)){
            type = item.getType().toString().toLowerCase(Locale.ENGLISH);
        }
        switch(type){
            case("dandelion"):
                item = StonesItems.get(SItem.DANDELION_HEAD);
                break;
            case("poppy"):
                item = StonesItems.get(SItem.POPPY_HEAD);
                break;
            case("blue_orchid"):
                item = StonesItems.get(SItem.BLUE_ORCHID_BUDS);
                break;
            case("allium"):
                item = StonesItems.get(SItem.ALLIUM_HEAD);
                break;
            case("azure_bluet"):
                item = StonesItems.get(SItem.AZURE_BLUET_BUDS);
                break;
            case("red_tulip"):
                item = StonesItems.get(SItem.RED_TULIP_PETALS);
                break;
            case("orange_tulip"):
                item = StonesItems.get(SItem.ORANGE_TULIP_PETALS);
                break;
            case("white_tulip"):
                item = StonesItems.get(SItem.WHITE_TULIP_PETALS);
                break;
            case("pink_tulip"):
                item = StonesItems.get(SItem.PINK_TULIP_PETALS);
                break;
            case("oxeye_daisy"):
                item = StonesItems.get(SItem.OXEYE_DAISY_HEAD);
                break;
            case("cornflower"):
                item = StonesItems.get(SItem.CORNFLOWER_HEAD);
                break;
            case("lily_of_the_valley"):
                item = StonesItems.get(SItem.LILY_OF_THE_VALLEY_BUDS);
                break;
            case("grass"):
                item = StonesItems.get(SItem.THATCH);
                break;
        }
        return item;
    }
    
    public static ItemStack sickleCut(Block b){
        ItemStack item = UsefulThings.plantToItem(b);
        return sickleCut(item);
    }
    
    
    
}
