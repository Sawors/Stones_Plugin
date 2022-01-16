package com.github.sawors.stones.features;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.enums.StoneEffect;
import com.github.sawors.stones.enums.StoneItem;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class StonesBodyParts implements Listener {
    
    @EventHandler
    public void onPlayerRopeEntity(PlayerInteractEntityEvent event){
        Entity e = event.getRightClicked();
        Player p = event.getPlayer();
        if(e instanceof Animals && event.getHand().equals(EquipmentSlot.HAND) && !p.getInventory().getItemInMainHand().getType().isAir() && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().contains("rope")){
            final int max = (int) (e.getBoundingBox().getVolume()*12.0);
            new BukkitRunnable(){
                
                private int timer = max;
                
                @Override
                public void run(){
                    if(timer <= 0){
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 1, 1.5f);
                        if(!p.getInventory().getItemInMainHand().getType().isAir() && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().contains("rope") && Objects.equals(p.getTargetEntity(4), e)){
                            e.setCustomName("Dinnerbone");
                            Stones.getHideNameTeam().addEntity(e);
                            e.setSilent(true);
                            ((Animals) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000*20,100, false ,false));
                            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
                        }
                        this.cancel();
                        return;
                    }
                    
                    if(!p.getInventory().getItemInMainHand().getType().isAir() && p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().contains("rope") && Objects.equals(p.getTargetEntity(4), e)){
                        p.getWorld().playSound(e.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, .5f, ((max-timer)+(1.5f*max))/(2*max));
                        e.getWorld().spawnParticle(Particle.ITEM_CRACK, e.getLocation().set(e.getBoundingBox().getCenterX(), e.getBoundingBox().getCenterY(), e.getBoundingBox().getCenterZ()),8,e.getBoundingBox().getWidthX()/2,e.getBoundingBox().getHeight()/2,e.getBoundingBox().getWidthZ()/2, .05, new ItemStack(Material.BROWN_WOOL));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 4, false ,false));
                    } else {
                        p.getWorld().playSound(e.getLocation(), Sound.ENTITY_LEASH_KNOT_BREAK, .5f, .5f);
                        this.cancel();
                        return;
                    }
                    
                    timer--;
                }
                
            }.runTaskTimer(Stones.getPlugin(), 10, 10);
        } else if(e instanceof Animals && !e.isInsideVehicle() && (p.getInventory().getItemInMainHand().getType().toString().contains("_AXE") || p.getInventory().getItemInMainHand().getType().toString().contains("_SWORD") || p.getInventory().getItemInMainHand().getType().equals(Material.SHEARS)) && event.getHand().equals(EquipmentSlot.HAND) && e.getCustomName() != null && e.getCustomName().equals("Dinnerbone") && e.isSilent() && ((Animals) e).hasPotionEffect(PotionEffectType.SLOW)){
            e.setCustomName("");
            Stones.getHideNameTeam().removeEntity(e);
            e.setSilent(false);
            ((Animals) e).removePotionEffect(PotionEffectType.SLOW);
            p.getWorld().dropItemNaturally(e.getLocation(), StonesItems.get(StoneItem.ROPE));
            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_SHEEP_SHEAR, .5f,1);
        }
        if(e instanceof Animals && event.getHand().equals(EquipmentSlot.HAND) && p.isSneaking() && p.getInventory().getItemInMainHand().getType().isAir() && e.getCustomName() != null && e.getCustomName().contains("Dinnerbone") && e.isSilent() && ((Animals) e).hasPotionEffect(PotionEffectType.SLOW) && p.getPassengers().size() == 0){
            p.addPassenger(e);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2, false ,false));
            Stones.effectmapAdd(event.getPlayer().getUniqueId(), StoneEffect.CARRY);
        }
    }
    
    @EventHandler
    public void playerRemoveEntityFromHead(PlayerInteractEvent event){
        if(event.getAction().isLeftClick() && event.getPlayer().isSneaking() && event.getClickedBlock() == null && event.getPlayer().getPassengers().size() >= 1 && event.getPlayer().getPassengers().get(event.getPlayer().getPassengers().size()-1) instanceof Animals){
            event.setCancelled(true);
            event.getPlayer().removePassenger(event.getPlayer().getPassengers().get(event.getPlayer().getPassengers().size()-1));
            event.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            Stones.effectmapRemove(event.getPlayer().getUniqueId(), StoneEffect.CARRY);
        }
    }
    @EventHandler
    public void avoidPlayerKillingCarry(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getDamager().getPassengers().size() >= 1 && event.getDamager().getPassengers().get(0) instanceof Animals){
            event.setCancelled(true);
        }
    }
    
}
