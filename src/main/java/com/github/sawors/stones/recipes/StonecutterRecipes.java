package com.github.sawors.stones.recipes;

import com.github.sawors.stones.Stones;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class StonecutterRecipes implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.STONECUTTER)){

            Player p = event.getPlayer();
            final ItemStack mainitem = p.getInventory().getItemInMainHand().clone();
            final ItemStack[] input = new ItemStack[9];
            input[0] = mainitem;
            input[0].setAmount(1);
            ItemStack[] output = new ItemStack[9];
            Location blockloc = event.getClickedBlock().getLocation().add(0.5,0.5,0.5);
            BlockData bdata = event.getClickedBlock().getBlockData();

            if(mainitem.getType().toString().contains("LOG")){

                event.setCancelled(true);

                ArmorStand visual = (ArmorStand) p.getWorld().spawnEntity(blockloc.clone().subtract(0,1+((1f/16)*6)+((Math.random()-0.5)/8),0.25+(Math.random()-0.5)/8), EntityType.ARMOR_STAND);
                visual.setInvulnerable(true);
                visual.setGravity(false);
                visual.setVisible(false);
                visual.setHeadPose(new EulerAngle(3.14/2, 0, 0));
                visual.teleport(visual.getLocation().add(1,0,0));
                visual.setDisabledSlots(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
                visual.getEquipment().setHelmet(input[0]);

                p.getWorld().spawnParticle(Particle.ITEM_CRACK, blockloc.clone().add(0,0.2,0), 16, 0.25, 0.1,0.25, 0, input[0]);


                new BukkitRunnable() {
                    int count = -1;


                    @Override
                    public void run() {
                        if(count == -1){
                            if(p.isSneaking()){
                                count = p.getInventory().getItemInMainHand().getAmount();
                                p.sendMessage(String.valueOf(count));
                                p.getInventory().getItemInMainHand().setAmount(0);
                            } else{
                                count = 1;
                                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
                            }
                            p.getWorld().playSound(blockloc.clone(), "minecraft:sawors.block.saw", 1, 1);
                            return;
                        }
                        if(count <= 0){
                            this.cancel();
                            return;
                        } else{

                            if(blockloc.getBlock().getType() != Material.STONECUTTER){

                                ItemStack breakdrop = input[0].clone();
                                breakdrop.setAmount(count);
                                p.getWorld().dropItem(blockloc.clone().add(0,0.25,0), breakdrop);

                                visual.remove();
                                this.cancel();
                                return;
                            }

                            output[0] = Bukkit.craftItem(input.clone(), p.getWorld(), p);
                            if(count > 1){
                                p.getWorld().playSound(blockloc.clone(), "minecraft:sawors.block.saw", 1, 1);
                            }
                            p.getWorld().spawnParticle(Particle.ITEM_CRACK, blockloc.clone().add(0,0.2,0), 16, 0.25, 0.1,0.25, 0, output[0]);
                            Item item = p.getWorld().dropItem(blockloc.clone().add(0,0.25,0), output[0]);
                            item.setPickupDelay(0);
                            if(count == 1){
                                visual.remove();
                            }
                            /*Vector itempath = p.getLocation().toVector().subtract(item.getLocation().toVector()).multiply(0.25);
                            if(itempath.multiply(4).length() < 4.5){
                                item.setVelocity(itempath);
                            }*/

                        }
                        count--;
                    }
                }.runTaskTimer(Stones.getPlugin(), 0, 10);
            }





        }

    }


}
