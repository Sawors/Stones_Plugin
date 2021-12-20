package com.github.sawors.stones.recipes;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
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

                ArmorStand visual;

                if(((Directional) event.getClickedBlock().getBlockData()).getFacing().equals(BlockFace.EAST) || ((Directional) event.getClickedBlock().getBlockData()).getFacing().equals(BlockFace.WEST)){
                    visual = UsefulThings.createDisplay(blockloc.clone().subtract(0,5/16f,0.25), input[0]);
                    visual.setHeadPose(new EulerAngle(3.14/2, 0, 0));
                } else {
                    visual = UsefulThings.createDisplay(blockloc.clone().subtract(0.25,5/16f,0), input[0]);
                    visual.setHeadPose(new EulerAngle(0, 3.14/2, 3.14/2));
                }


                ArmorStand value = UsefulThings.createDisplay(blockloc.clone().subtract(0,5/16f,0), new ItemStack(Material.AIR));
                value.setCustomNameVisible(true);
                if(p.isSneaking()){
                    value.setCustomName(String.valueOf(p.getInventory().getItemInMainHand().getAmount()));
                } else {
                    value.setCustomName("1");
                }


                p.getWorld().spawnParticle(Particle.ITEM_CRACK, blockloc.clone().add(0,0.2,0), 16, 0.25, 0.1,0.25, 0, input[0]);


                new BukkitRunnable() {
                    int count = -1;


                    @Override
                    public void run() {
                        if(count == -1){
                            if(p.isSneaking()){
                                count = p.getInventory().getItemInMainHand().getAmount();
                                p.getInventory().getItemInMainHand().setAmount(0);
                            } else{
                                count = 1;
                                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
                            }
                            if(!event.getClickedBlock().getWorld().getBlockAt(blockloc.clone().subtract(0,1,0)).getType().toString().contains("WOOL")){
                                p.getWorld().playSound(blockloc.clone(), "minecraft:sawors.block.saw", 1, 1);
                            } else{
                                p.getWorld().playSound(blockloc.clone(), "minecraft:sawors.block.saw", .1f, .8f);
                            }

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

                                value.remove();
                                visual.remove();
                                this.cancel();
                                return;
                            }

                            output[0] = Bukkit.craftItem(input.clone(), p.getWorld(), p);
                            if(count > 1){
                                if(!event.getClickedBlock().getWorld().getBlockAt(blockloc.clone().subtract(0,1,0)).getType().toString().contains("WOOL")){
                                    p.getWorld().playSound(blockloc.clone(), "minecraft:sawors.block.saw", 1, 1);
                                } else{
                                    p.getWorld().playSound(blockloc.clone(), "minecraft:sawors.block.saw", .1f, .8f);
                                }
                            }
                            p.getWorld().spawnParticle(Particle.ITEM_CRACK, blockloc.clone().add(0,0.2,0), 16, 0.25, 0.1,0.25, 0, output[0]);
                            Item item = p.getWorld().dropItem(blockloc.clone().add(0,0.25,0), output[0]);
                            item.setPickupDelay(0);
                            value.setCustomName(String.valueOf(count-1));
                            if(count == 1){
                                value.remove();
                                visual.remove();
                            }

                        }
                        count--;
                    }
                }.runTaskTimer(Stones.getPlugin(), 0, 10);
            }





        }

    }


}
