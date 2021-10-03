package com.github.sawors.stones.listeners;

import com.github.sawors.stones.UsefulThings;
import com.github.sawors.stones.commandexecutors.SgiveCommandExecutor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;

public class AttackListeners implements Listener {
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player damager = (Player) event.getDamager();
            LivingEntity receiver = (LivingEntity) event.getEntity();
            if (UsefulThings.isBehind(damager, receiver) && damager.isSneaking()) {
                if(damager.getLocation().add(0,-0.25,0).getBlock().getType().isSolid()){
                    if(damager.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(SgiveCommandExecutor.getKey("type"), PersistentDataType.STRING) != null && Objects.equals(damager.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(SgiveCommandExecutor.getKey("type"), PersistentDataType.STRING), "dagger")){
                        PotionEffect potionslowheavy = new PotionEffect(PotionEffectType.SLOW, 20, 2, false, false, false);
                        PotionEffect potionslowlight = new PotionEffect(PotionEffectType.SLOW, 10, 2, false, false, false);

                        receiver.getWorld().spawnParticle(Particle.BLOCK_CRACK, receiver.getLocation(), (int) event.getDamage()*2, .5, receiver.getHeight(),.5 , 0.1, Material.REDSTONE_BLOCK.createBlockData());
                        receiver.addPotionEffect(potionslowheavy);
                        if (receiver.getEquipment() != null && receiver.getEquipment().getChestplate() != null && receiver.getEquipment().getChestplate().getType() != Material.AIR) {
                            if(damager.getInventory().getItemInOffHand().hasItemMeta() && Objects.equals(damager.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(SgiveCommandExecutor.getKey("type"), PersistentDataType.STRING), "dagger")){
                                receiver.damage((event.getDamage()*1.5*2)-event.getDamage());
                            } else{
                                receiver.damage((event.getDamage()*1.5)-event.getDamage());
                            }
                            damager.addPotionEffect(potionslowheavy);
                            receiver.getWorld().playSound(receiver.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);

                        } else{
                            if(damager.getInventory().getItemInOffHand().hasItemMeta() && Objects.equals(damager.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(SgiveCommandExecutor.getKey("type"), PersistentDataType.STRING), "dagger")){
                                receiver.damage((event.getDamage()*2*2)-event.getDamage());
                            } else{
                                receiver.damage((event.getDamage()*2)-event.getDamage());
                            }
                            damager.addPotionEffect(potionslowlight);
                            receiver.getWorld().playSound(receiver.getLocation(), Sound.ENTITY_COD_HURT, 1, 1.2f);
                        }
                    }
                }
            } else if (!damager.getVelocity().equals(new Vector(0, 0, 0)) && damager.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(SgiveCommandExecutor.getKey("type"), PersistentDataType.STRING) != null && Objects.equals(damager.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(SgiveCommandExecutor.getKey("type"), PersistentDataType.STRING), "curveddagger")){

            }
        }
    }
}
