package com.github.sawors.stones.combat;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.database.DataHolder;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class AttackListeners implements Listener {
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity && event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            Player damager = (Player) event.getDamager();
            LivingEntity receiver = (LivingEntity) event.getEntity();
            PotionEffect potionslowheavy = new PotionEffect(PotionEffectType.SLOW, 20, 2, false, false, false);
            PotionEffect potionslowlight = new PotionEffect(PotionEffectType.SLOW, 10, 2, false, false, false);

            //straight daggers
            if (UsefulThings.isBehind(damager, receiver) && damager.isSneaking()) {
                if(damager.getLocation().add(0,-0.25,0).getBlock().getType().isSolid()){
                    if(damager.getInventory().getItemInMainHand().hasItemMeta() && damager.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING) != null && Objects.equals(damager.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING), "dagger")){

                        receiver.getWorld().spawnParticle(Particle.BLOCK_CRACK, receiver.getLocation(), (int) event.getDamage()*2, .5, receiver.getHeight(),.5 , 0.1, Material.REDSTONE_BLOCK.createBlockData());
                        receiver.addPotionEffect(potionslowheavy);
                        if (receiver.getEquipment() != null && receiver.getEquipment().getChestplate() != null && receiver.getEquipment().getChestplate().getType() != Material.AIR) {
                            if(damager.getInventory().getItemInOffHand().hasItemMeta() && Objects.equals(damager.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING), "dagger")){
                                receiver.damage((event.getDamage()*1.5*2)-event.getDamage());
                            } else{
                                receiver.damage((event.getDamage()*1.5)-event.getDamage());
                            }
                            damager.addPotionEffect(potionslowheavy);
                            receiver.getWorld().playSound(receiver.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);

                        } else{
                            if(damager.getInventory().getItemInOffHand().hasItemMeta() && Objects.equals(damager.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING), "dagger")){
                                receiver.damage((event.getDamage()*2*2)-event.getDamage());
                            } else{
                                receiver.damage((event.getDamage()*2)-event.getDamage());
                            }
                            damager.addPotionEffect(potionslowlight);
                            receiver.getWorld().playSound(receiver.getLocation(), Sound.ENTITY_COD_HURT, 1, 1.2f);
                        }
                    }
                }
            }

            //recurved daggers
            else if (damager.getInventory().getItemInMainHand().hasItemMeta() && damager.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING) != null && Objects.equals(damager.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING), "curveddagger")){
                double multiplier = 1;
                if(damager.isSprinting()){
                    multiplier += 0.5;
                }
                if(damager.isSneaking()){
                    multiplier -= 0.5;
                }
                if(damager.isSwimming()){
                    multiplier -= 0.5;

                }
                if(damager.isJumping() || !damager.getLocation().add(0,-0.25,0).getBlock().getType().isSolid()){
                    multiplier += 0.5;
                }
                damager.getInventory().getItemInOffHand();

                if(receiver.getEquipment() != null && receiver.getEquipment().getChestplate() != null && receiver.getEquipment().getChestplate().getType() != Material.AIR){
                    if(receiver.getEquipment().getChestplate().getType() == Material.LEATHER_CHESTPLATE){
                        multiplier -= 1;
                        receiver.getWorld().playSound(receiver.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, .5f);
                    }else {
                        multiplier = 0;

                    }
                }
                if(damager.getInventory().getItemInOffHand().hasItemMeta() && damager.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING) != null && Objects.equals(damager.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING), "curveddagger")){
                    multiplier *= 2;
                }
                if(multiplier > 0){
                    if(damager.hasPotionEffect(PotionEffectType.SPEED)){
                        receiver.damage(((event.getDamage()*multiplier)-event.getDamage())+(damager.getPotionEffect(PotionEffectType.SPEED).getAmplifier()*0.2*((event.getDamage()*multiplier)-event.getDamage())));
                    }else{
                        receiver.damage((event.getDamage()*multiplier)-event.getDamage());
                    }

                    receiver.addPotionEffect(potionslowlight);
                    damager.addPotionEffect(potionslowlight);
                    receiver.getWorld().playSound(receiver.getLocation(), Sound.ENTITY_COD_HURT, 1, 1.2f);
                } else{
                    damager.getWorld().playSound(damager.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1.25f);
                    receiver.addPotionEffect(potionslowlight);
                    damager.addPotionEffect(potionslowheavy);
                    event.setCancelled(true);
                }

            }
        }
    }
}
