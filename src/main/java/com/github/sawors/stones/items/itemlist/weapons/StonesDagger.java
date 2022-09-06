package com.github.sawors.stones.items.itemlist.weapons;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.items.ItemTag;
import com.github.sawors.stones.items.StonesItem;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public abstract class StonesDagger extends StonesItem implements Listener {

    public StonesDagger() {
        super();

        setMaterial(Material.IRON_SWORD);
    }

    @EventHandler
    public void onPlayerBackStab(EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getDamager() instanceof Player damager && event.getEntity() instanceof LivingEntity receiver && event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            PotionEffect potionslowheavy = new PotionEffect(PotionEffectType.SLOW, 20, 2, false, false, false);
            PotionEffect potionslowlight = new PotionEffect(PotionEffectType.SLOW, 10, 2, false, false, false);
            ItemStack mainitem = damager.getInventory().getItemInMainHand();
            ItemStack offitem = damager.getInventory().getItemInOffHand();


            //straight daggers
            if (UsefulThings.isBehind(damager, receiver) && damager.isSneaking()) {
                if(damager.getLocation().add(0,-0.25,0).getBlock().getType().isSolid()){
                    if(mainitem.hasItemMeta() && StonesItem.getItemTags(mainitem).contains(ItemTag.DAGGER.tagString())){

                        receiver.getWorld().spawnParticle(Particle.BLOCK_CRACK, receiver.getLocation(), (int) event.getDamage()*2, .5, receiver.getHeight(),.5 , 0.1, Material.REDSTONE_BLOCK.createBlockData());
                        receiver.addPotionEffect(potionslowheavy);
                        if (receiver.getEquipment() != null && receiver.getEquipment().getChestplate() != null && receiver.getEquipment().getChestplate().getType() != Material.AIR) {
                            if(offitem.hasItemMeta() && StonesItem.getItemTags(offitem).contains(ItemTag.DAGGER.tagString())){
                                receiver.damage((event.getDamage()*1.5*2)-event.getDamage());
                            } else{
                                receiver.damage((event.getDamage()*1.5)-event.getDamage());
                            }
                            damager.addPotionEffect(potionslowheavy);
                            receiver.getWorld().playSound(receiver.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);

                        } else{
                            if(offitem.hasItemMeta() && StonesItem.getItemTags(offitem).contains(ItemTag.DAGGER.tagString())){
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
            else if (mainitem.hasItemMeta() && StonesItem.getItemTags(mainitem).contains(ItemTag.CURVED_DAGGER.tagString())){
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
                if(offitem.hasItemMeta() && StonesItem.getItemTags(offitem).contains(ItemTag.CURVED_DAGGER.tagString())){
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
