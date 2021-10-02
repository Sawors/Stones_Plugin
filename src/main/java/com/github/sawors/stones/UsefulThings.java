package com.github.sawors.stones;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.logging.Level;

public class UsefulThings {

    /**
     * Return if the fire is eternal (on netherrack) or not
     * @param  block  the block to check (must be fire)
     * @return      true of false depending if the fire is on netherrack or not
     */
    public static boolean isEternalFire(Block block){
        return block != null && block.getType() == Material.FIRE && block.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.NETHERRACK;
    }

    /**
     * Used to apply some subtle random pitch variations to a sound
     * @return      a random pitch between 0.8 and 1.2 (float)
     */
    public static float randomPitchSimple(){
        return (float) ((new Random().nextDouble() * 0.4) + 0.8);
    }

    /**
     * Used to apply some subtle random pitch variations to a sound (formula is ((random_number)*amplitude + displacement))
     * Default values for amplitude and displacement are respectively 0.4 and 0.8
     * @param amplitude how much the pitch will vary in range (default 0.4). Example : 0.4 will give values between 0.0 and 0.8 (float)
     * @param displacement how much the value is displaced along the y-axis (default 0.8). Example : 1 will set the output of the pitch to be 1 higher
     * @return      a random pitch in the range defined
     */
    public static float randomPitchSimple(float amplitude, float displacement){
        return (new Random().nextFloat() * amplitude) + displacement;
    }

    /**
     * Used to apply some subtle random pitch variations to a sound (formula is ((random_number)*amplitude + displacement))
     * Default values for amplitude and displacement are respectively 0.4 and 0.8
     * @param amplitude how much the pitch will vary in range (default 0.4). Example : 0.4 will give values between 0.0 and 0.8 (float)
     * @param displacement how much the value is displaced along the y-axis (default 0.8). Example : 1 will set the output of the pitch to be 1 higher
     * @return      a random pitch in the range defined
     */
    public static float randomPitchSimple(double amplitude, double displacement){
        return (float) ((new Random().nextDouble() * amplitude) + displacement);
    }

    /**
     * Adds the Fire Aspect enchant to the specified item (must be a sword or a "fireaspectable" item (I don't know any except swords)
     * @param item the item to enchant (MUST BE PROVIDED YOU GENIUS)
     * @param level how high you want the enchant. Possible values are : 1; 2; 0 to add 1 to the existing enchant (if lv = 0 makes lv = 1); -1 to remove enchant
     * @param player a player where the sound effects and particles will be played (can be null normally (pro code))
     */
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

    /**
     * Extinguish the item provided (removes Fire Aspect enchant or any flame-related attribute (torches to unlit torches for example))
     * @param item the item to extinguish (MUST BE PROVIDED YOU GENIUS)
     * @param reason why did the item has been extinguished. Possible values are : "water" for water bases extinguish; "normal" for normal; "fake" to just play particles and not actually extinguish the item (in rain for instance)
     * @param player a player where the sound effects and particles will be played
     */
    public static void extinguishItem(@NotNull ItemStack item, String reason, Player player){
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

    public static String getBlockMaterialCategory(@NotNull Block block){
        String blockname = block.getType().toString();
        // || blockname.contains("")
        if(blockname.contains("WOOD") || blockname.contains("SPRUCE") || blockname.contains("BIRCH") || blockname.contains("OAK") || blockname.contains("ACACIA") || blockname.contains("JUNGLE")){
            return "WOOD";
        }
        else if(blockname.contains("STONE") || blockname.contains("ANDSEITE") || blockname.contains("PRISMARINE") || blockname.contains("GRANITE") || blockname.contains("DIORITE")){
            return "STONE";
        }
        else if(blockname.contains("IRON") || blockname.contains("GOLD") || blockname.contains("COPPER") || blockname.contains("NETHERITE")){
            return "METAL";
        }
        else if(blockname.contains("GLASS")){
            return "GLASS";
        }
        else if(blockname.contains("GRASS") || blockname.contains("LEAVES") || blockname.contains("FLOWER")){
            return "PLANT";
        }
        else if(blockname.contains("SAND")){
            return "GROUND";
        } else {
            return "UNKNOWN";
        }
    }

    /**
     * Used to check if an entity is behind another, generally while attacking (mainly for backstabs)
     * the default angle for this method is +-22.5 degrees (considering the back of the entity to be 0 degree)
     * @param attacker the entity performing the attack (typically EntityDamageByEntityEvent.getDamager())
     * @param victim the entity which is being attacked (typically EntityDamageByEntityEvent.getEntity())
     * @return true if the attacker is behind the victim, false otherwise
     */
    public static boolean isBehind(Player attacker, Entity victim){
        return Math.abs(Math.abs(victim.getLocation().getYaw()) - Math.abs(attacker.getLocation().getYaw())) <= 22.5;
    }

    /**
     * Used to check if an entity is behind another, generally while attacking (mainly for backstabs)
     * the default angle for this method is +-22.5 degrees (considering the back of the entity to be 0 degree)
     * @param attacker the entity performing the attack (typically EntityDamageByEntityEvent.getDamager())
     * @param victim the entity which is being attacked (typically EntityDamageByEntityEvent.getEntity())
     * @param range used to specify the "width" of the cone behind the victim where this method returns true (example : 90 will return true when attacker is behind the entity at +-90 degrees)
     * @return true if the attacker is behind the victim, false otherwise
     */
    public static boolean isBehind(Player attacker, Entity victim, double range){
        return Math.abs(Math.abs(victim.getLocation().getYaw()) - Math.abs(attacker.getLocation().getYaw())) <= range;
    }
}
