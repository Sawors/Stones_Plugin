package com.github.sawors.stones.items.itemlist;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.items.SItem;
import com.github.sawors.stones.items.StonesItem;
import com.github.sawors.stones.items.StonesItems;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Rope extends StonesItem implements Listener {
    public Rope() {
        super();
        setMaterial(Material.STRING);
    }
    
    @EventHandler
    public void cancelPlayerPlaceRope(BlockPlaceEvent event){
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        
        if(getItemId(item).equals(new Rope().getTypeId())){
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerRopeEntity(PlayerInteractEntityEvent event){
        Entity e = event.getRightClicked();
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if(e instanceof Animals && event.getHand().equals(EquipmentSlot.HAND) && !item.getType().isAir() && item.hasItemMeta() && getItemId(item).equals(new Rope().getTypeId())){
            final int max = (int) (e.getBoundingBox().getVolume()*12.0);
            new BukkitRunnable(){
                
                private int timer = max;
                
                @Override
                public void run(){
                    if(timer <= 0){
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 1, 1.5f);
                        
                        if(!item.getType().isAir() && item.hasItemMeta() && getItemId(item).equals(new Rope().getTypeId()) && Objects.equals(p.getTargetEntity(4), e)){
                            e.customName(Component.text("Dinnerbone"));
                            Stones.getHideNameTeam().addEntity(e);
                            e.setSilent(true);
                            ((Animals) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000*20,100, false ,false));
                            item.setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
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
        } else {
            // break rope
            Material mat = item.getType();
            if(e instanceof Animals && !e.isInsideVehicle() && (mat.toString().contains("_AXE") || mat.toString().contains("_SWORD") || mat.equals(Material.SHEARS)) && event.getHand().equals(EquipmentSlot.HAND)) {
                if (e.getName().equals("Dinnerbone") && e.isSilent() && ((Animals) e).hasPotionEffect(PotionEffectType.SLOW)) {
                    e.customName(Component.text(""));
                    Stones.getHideNameTeam().removeEntity(e);
                    e.setSilent(false);
                    ((Animals) e).removePotionEffect(PotionEffectType.SLOW);
                    p.getWorld().dropItemNaturally(e.getLocation(), StonesItems.get(SItem.ROPE));
                    e.getWorld().playSound(e.getLocation(), Sound.ENTITY_SHEEP_SHEAR, .5f, 1);
                }
            }
        }
    }
}
