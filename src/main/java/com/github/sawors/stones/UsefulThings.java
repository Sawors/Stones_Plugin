package com.github.sawors.stones;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.logging.Level;

public class UsefulThings {
    public static boolean isEternalFire(Block b){
        return b != null && b.getType() == Material.FIRE && b.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.NETHERRACK;
    }
    //case void -> ()
    public static float randomPitchSimple(){
        return (float) ((new Random().nextDouble() * 0.4) + 0.8);
    }
    //case float -> (0.5f)
    public static float randomPitchSimple(float amplitude, float displacement){
        return (new Random().nextFloat() * amplitude) + displacement;
    }
    //case double -> (0.5)
    public static float randomPitchSimple(double amplitude, double displacement){
        return (float) ((new Random().nextDouble() * amplitude) + displacement);
    }

    public static void igniteItem(@NotNull ItemStack item, int level, Player player){
        try{
            if((!item.getEnchantments().containsKey(Enchantment.FIRE_ASPECT)) && level == 1){
                item.addEnchantment(Enchantment.FIRE_ASPECT, level);
                item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                if(player != null){
                    player.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1,0.9F);
                    player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0,1,0), 16, 0.25, 0.5, 0.25, 0.01);

                }

            }else if(level == 2){
                if(item.getEnchantments().containsKey(Enchantment.FIRE_ASPECT)){
                    item.removeEnchantment(Enchantment.FIRE_ASPECT);
                    item.addEnchantment(Enchantment.FIRE_ASPECT, level);
                } else {
                    item.addEnchantment(Enchantment.FIRE_ASPECT, level);
                }

                item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                if(player != null){
                    player.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1,0.75F);
                    player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0,1,0), 16, 0.5, 0.5, 0.5, 0.01);
                }
            } else if(level == 0){
                if(item.getEnchantments().containsKey(Enchantment.FIRE_ASPECT)){
                    item.addEnchantment(Enchantment.FIRE_ASPECT, item.getEnchantmentLevel(Enchantment.FIRE_ASPECT)+1);
                } else {
                    item.addEnchantment(Enchantment.FIRE_ASPECT, 1);
                }
                item.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                if(player != null){
                    player.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1,0.75F);
                    player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0,1,0), 16, 0.5, 0.5, 0.5, 0.01);
                }
            } else if(level == -1){
                UsefulThings.extinguishItem(item, "hehehe", player);
            }
            else {
                throw new IllegalArgumentException("Your must hold a not null item which isn't already at max fire level");
            }
        } catch (IllegalArgumentException e){
            Stones.getPlugin(Stones.class).getLogger().log(Level.INFO, "an error occurred while igniting this item");
        }
    }
    public static void extinguishItem(ItemStack item, String reason, Player player){
        if(item.getEnchantments().containsKey(Enchantment.FIRE_ASPECT)){
            if(reason != null && player != null) {
                switch (reason) {
                    case "normal":
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1F, randomPitchSimple());
                        player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation().add(0,1,0), 16, 0.5, 0.5, 0.5, 0.01);
                        break;
                    case "water":
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.8F, randomPitchSimple()-0.2f);
                        player.playSound(player.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1, randomPitchSimple());
                        player.getWorld().spawnParticle(Particle.BUBBLE_COLUMN_UP, player.getLocation().add(0,1,0), 16, 0.5, 1, 0.5, 0.01);
                        break;
                    case "hehehe":
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, randomPitchSimple()*2);
                        break;
                    case "fake":
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.1F, randomPitchSimple() + 0.1f);
                        player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation().add(0,1,0), 8, 0.25, 0.5, 0.25, 0.01);
                        return;
                                 }
                item.removeEnchantment(Enchantment.FIRE_ASPECT);

            }
        }
    }
}
