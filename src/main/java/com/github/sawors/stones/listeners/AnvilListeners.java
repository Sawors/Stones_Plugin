package com.github.sawors.stones.listeners;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class AnvilListeners implements Listener{

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        Block b = event.getClickedBlock();
    if (item != null && item.getType() == Material.STICK && b != null && item.getItemMeta().getLocalizedName().equals("hammer")) {
        if(b.getType()==Material.CHISELED_POLISHED_BLACKSTONE && b.getLocation().add(0,0,1).getBlock().getType() == Material.BLACKSTONE_STAIRS && b.getLocation().add(0,0,-1).getBlock().getType() == Material.BLACKSTONE_STAIRS) {

            ArrayList<Entity> listslot1 = new ArrayList<>(b.getLocation().add(0.5,1.5,1.5).getNearbyEntities(0.5,0.5,0.5));
            ArrayList<Entity> listslot2 = new ArrayList<>(b.getLocation().add(0.5,1.5,-0.5).getNearbyEntities(0.5,0.5,0.5));

            System.out.println(listslot1);
            System.out.println(listslot2);

            if(listslot1.size() == 1 && listslot1.get(0) instanceof Item){

                Entity entity1 = listslot1.get(0);
                Entity entity2 = listslot2.get(0);

                System.out.println(entity1.getType());
                System.out.println(entity2.getType());


                if(     ((Item) entity1).getItemStack().getType() == Material.DIAMOND_SWORD
                        && !((Item) entity1).getItemStack().getItemMeta().hasLocalizedName()
                        && ((Item) entity2).getItemStack().getType() == Material.NETHERITE_INGOT
                        && !((Item) entity1).getItemStack().getItemMeta().hasLocalizedName())
                {


                    float pitch = (float) (Math.sin(new Random().nextFloat()) / 4 + 0.8);
                    ItemMeta meta = ((Item) entity1).getItemStack().getItemMeta().clone();
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_VILLAGER_WORK_TOOLSMITH, 1, pitch);
                    ((Item) entity1).getItemStack().setAmount(((Item) entity1).getItemStack().getAmount() - 1);
                    ((Item) entity2).getItemStack().setAmount(((Item) entity2).getItemStack().getAmount() - 1);
                    entity1.getWorld().spawnParticle(Particle.SMOKE_NORMAL, entity1.getLocation().add(0,0.1,-0.4), 8, 0, 0, 0.25, 0);
                    entity2.getWorld().spawnParticle(Particle.SMOKE_NORMAL, entity2.getLocation().add(0,0.1,0.4), 8, 0, 0, 0.25, 0);
                    ItemStack result = new ItemStack(Material.NETHERITE_SWORD);
                    result.setItemMeta(meta);
                    b.getWorld().dropItem(b.getLocation().add(0.5,1,0.5), result).setVelocity(new Vector(0,0,0));
                    b.getWorld().spawnParticle(Particle.SMALL_FLAME, b.getLocation().add(0.5, 1.1, 0.5), 16, 0.05, 0.2, 0.05, 0);
                 }
                }
         }
        }
    }
}

