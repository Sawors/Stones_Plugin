package com.github.sawors.stones.UsefulThings;

import com.github.sawors.stones.Stones;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public static Location getFaceMidLocation(Location loc, BlockFace blockface, double padding, Vector verticaledit, Vector horizontaledit){
        if(blockface.equals(BlockFace.NORTH)){
            loc = loc.add(0,0,-0.50-padding).add(horizontaledit);
            loc.setYaw(180);
        } else if (blockface.equals(BlockFace.SOUTH)){
            loc = loc.add(0,0,0.50+padding).add(horizontaledit);
            loc.setYaw(0);
        }else if(blockface.equals(BlockFace.EAST)){
            loc = loc.add(0.50+padding,0,0).add(horizontaledit);
            loc.setYaw(270);
        }else if(blockface.equals(BlockFace.WEST)){
            loc = loc.add(-0.50-padding,0,0).add(horizontaledit);
            loc.setYaw(90);
        }else if(blockface.equals(BlockFace.UP)){
            loc = loc.add(0,0.50+padding,0).add(verticaledit);
            loc.setPitch(-90);
        }else if(blockface.equals(BlockFace.DOWN)){
            loc = loc.add(0,-0.50-padding,0).add(verticaledit);
            loc.setPitch(90);

        }
        return loc;
    }
    public static Location getFaceMidLocation(Block block, BlockFace blockface, double padding, Vector verticaledit, Vector horizontaledit){
        return getFaceMidLocation(block.getLocation().add(0.5,0.5,0.5), blockface, padding, verticaledit, horizontaledit);
    }
    public static Location getFaceMidLocation(Block block, BlockFace blockface){
        return getFaceMidLocation(block.getLocation().add(0.5,0.5,0.5), blockface, 0.0, new Vector(0,0,0), new Vector(0,0,0));
    }
    public static Location getFaceMidLocation(Location loc, BlockFace blockface){
        return getFaceMidLocation(loc, blockface, 0.0, new Vector(0,0,0), new Vector(0,0,0));
    }
    
    public static Location getFaceMidLocation(Location loc, Entity e, double padding){
        loc=loc.clone();
        float yaw = e.getLocation().getYaw();
        BlockFace face = BlockFace.NORTH;
        if(yaw >-45 && yaw <=45){
            loc.setZ(loc.getZ()+0.5);
        } else if(yaw >45 && yaw <=135){
            face = BlockFace.EAST;
            loc.setX(loc.getX()-0.5);
        } else if(yaw >135 && yaw <=225){
            face = BlockFace.SOUTH;
            loc.setZ(loc.getZ()-0.5);
        } else if(yaw >225 && yaw <=315){
            face = BlockFace.WEST;
            loc.setX(loc.getX()+0.5);
        }
        
        if(e.getLocation().getPitch() > 10){
            loc.setY(loc.getY()+0.5);
        } else if(e.getLocation().getPitch() < -10){
            loc.setY(loc.getY()-0.5);
        }
        
        Block b = loc.getBlock();
        
        return getFaceMidLocation(b, face, padding, new Vector(0,0,0), new Vector(0,0,0));
    }
    
    public static Location getFaceMidLocation(Entity e, double padding){
        Location loc = e.getLocation().clone();
        return getFaceMidLocation(loc, e, padding);
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


    /**
     * Adds the Fire Aspect enchant to the specified item (must be a sword or a "fireaspectable" item (I don't know any except swords)
     * @param item the item to enchant (MUST BE PROVIDED YOU GENIUS)
     * @param level how high you want the enchant. Possible values are : 1; 2; 0 to add 1 to the existing enchant (if lv = 0 makes lv = 1); -1 to remove enchant
     * @param player a player where the sound effects and particles will be played (can be null normally (pro code))
     */
    public static void ignite(@NotNull ItemStack item, int level, Player player){
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

    public static void ignite(Entity entity, float multiplier){
        if(entity instanceof ArmorStand) {
            int charge = 0;
            float power = 0;
            ArmorStand stand = (ArmorStand) entity;
    
    
            // wall bomb case
            if (((ArmorStand) entity).getEquipment().getHelmet() != null && ((ArmorStand) entity).getEquipment().getHelmet().getType() == Material.FIREWORK_STAR && ((ArmorStand) entity).getEquipment().getHelmet().hasItemMeta() && ((ArmorStand) entity).getEquipment().getHelmet().getItemMeta().getLocalizedName().contains("wall_bomb")) {
                charge = Objects.requireNonNull(stand.getEquipment().getHelmet().getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY))[0];
                power = (float) ((-0.25*Math.pow(charge, 2) + 3*charge)*multiplier);
                if(!UsefulThings.getFaceMidLocation(stand.getLocation().add(0,0.8,0), stand, 0.5).getBlock().isSolid()){
                    ItemStack bomb = stand.getEquipment().getHelmet().clone();
                    stand.getWorld().playSound(stand.getLocation(), Sound.BLOCK_WOOD_BREAK, 0.5f, 1.25f);
                    for(int i = Objects.requireNonNull(bomb.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY))[0]; i > 0; i--){
                        stand.getWorld().dropItem(UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, -2/16f), new ItemStack(Material.GUNPOWDER));
                    }
                    bomb = UsefulThings.setBombCharge(bomb, 0);
                    stand.getEquipment().setHelmet(bomb);
                    stand.getWorld().dropItem(UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, -2/16f), bomb);
                    for (Entity e : UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, 0.5).getNearbyEntities(charge * 4, charge * 4, charge * 4)) {
        
                        if (e instanceof ArmorStand) {
                            if (((ArmorStand) e).getEquipment().getHelmet() != null && ((ArmorStand) e).getEquipment().getHelmet().hasItemMeta() && ((ArmorStand) e).getEquipment().getHelmet().getItemMeta().getLocalizedName().contains("_bomb")) {
                                if(!UsefulThings.getFaceMidLocation(stand.getLocation().add(0,0.8,0), stand, 0.5).getBlock().isSolid()){
                                    ItemStack bomb2 = stand.getEquipment().getHelmet().clone();
                                    stand.getWorld().playSound(stand.getLocation(), Sound.BLOCK_WOOD_BREAK, 0.5f, 1.25f);
                                    for(int i = Objects.requireNonNull(bomb2.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY))[0]; i > 0; i--){
                                        stand.getWorld().dropItem(UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, -2/16f), new ItemStack(Material.GUNPOWDER));
                                    }
                                    bomb2 = UsefulThings.setBombCharge(bomb2, 0);
                                    stand.getEquipment().setHelmet(bomb2);
                                    stand.getWorld().dropItem(UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, -2/16f), bomb2);
                                    stand.remove();
                                    return;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                    stand.remove();
                    return;
                }
            }
    
            if (charge > 0) {
                
                final int finalcharge = charge;
                new BukkitRunnable() {
            
                    @Override
                    public void run() {
                        stand.getWorld().spawnParticle(Particle.SMOKE_LARGE, stand.getLocation().add(0, .5, 0), 32, finalcharge * .5, finalcharge * .5, finalcharge * .5, 0);
                    }
                }.runTaskLater(Stones.getPlugin(), 10);
                
        
                new BukkitRunnable() {
            
                    @Override
                    public void run() {
                        for (Entity e : UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, 0.5).getNearbyEntities(finalcharge * 1.5, finalcharge * 1.5, finalcharge * 1.5)) {
                    
                            if (e instanceof ArmorStand) {
                                if (((ArmorStand) e).getEquipment().getHelmet() != null && ((ArmorStand) e).getEquipment().getHelmet().hasItemMeta() && ((ArmorStand) e).getEquipment().getHelmet().getItemMeta().getLocalizedName().contains("_bomb")) {
                                    UsefulThings.ignite(e);
                                }
                            } else {
                                break;
                            }
                    
                    
                        }
                    }
                }.runTaskLater(Stones.getPlugin(), 4);
        
        stand.getWorld().createExplosion(stand, UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, 0.5), power , false);
                
                stand.remove();
        
            }
        }

    };

    public static void ignite(Entity e){
        UsefulThings.ignite(e, 1);
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
    public static boolean isBehind(Player attacker, LivingEntity victim){
        if(Math.abs(victim.getLocation().getYaw()) > 180){
            return Math.abs((Math.abs(victim.getLocation().getYaw()-360)) - Math.abs(attacker.getLocation().getYaw())) <= 22.5;
        } else{
            return Math.abs(Math.abs(victim.getLocation().getYaw()) - Math.abs(attacker.getLocation().getYaw())) <= 22.5;
        }
    }

    /**
     * Used to check if an entity is behind another, generally while attacking (mainly for backstabs)
     * the default angle for this method is +-22.5 degrees (considering the back of the entity to be 0 degree)
     * @param attacker the entity performing the attack (typically EntityDamageByEntityEvent.getDamager())
     * @param victim the entity which is being attacked (typically EntityDamageByEntityEvent.getEntity())
     * @param range used to specify the "width" of the cone behind the victim where this method returns true (example : 90 will return true when attacker is behind the entity at +-90 degrees)
     * @return true if the attacker is behind the victim, false otherwise
     */
    public static boolean isBehind(Player attacker, LivingEntity victim, double range){
        if(Math.abs(victim.getLocation().getYaw()) > 180){
            return Math.abs((Math.abs(victim.getLocation().getYaw()-360)) - Math.abs(attacker.getLocation().getYaw())) <= range;
        } else{
            return Math.abs(Math.abs(victim.getLocation().getYaw()) - Math.abs(attacker.getLocation().getYaw())) <= range;
        }
    }

    /**
     * Used to sit an entity at it's location. Conditions : entity must : be on ground, not swimming, not jumping
     * @param entity the entity to sit
     */
    public static void sitEntity(LivingEntity entity){
        if(entity.getLocation().subtract(0,0.1,0).getBlock().isSolid() && !entity.isJumping() && !entity.isSwimming()){
            ArmorStand e = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation().subtract(0,1,0), EntityType.ARMOR_STAND);
            e.setGravity(false);
            e.setVisible(false);
            e.setInvulnerable(true);
            e.setSmall(true);
            e.setCustomName("seat");
            e.setCustomNameVisible(false);
            e.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);

            e.addPassenger(entity);

        } else {
            if(entity instanceof Player){
                entity.sendActionBar(Component.text(ChatColor.RED + "you must be on ground to sit"));
            }
        }
    }



    /**
     * Used to sit an entity at it's location. Conditions : entity must : be on ground, not swimming, not jumping
     * @param entity the entity to sit
     * @param forcesit if the sit should be forced or not (ignore Conditions)
     */
    public static void sitEntity(LivingEntity entity, boolean forcesit){
        if(forcesit){
            ArmorStand e = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation().subtract(0,1,0), EntityType.ARMOR_STAND);
            e.setGravity(false);
            e.setVisible(false);
            e.setInvulnerable(true);
            e.setSmall(true);
            e.setCustomName("seat");
            e.setCustomNameVisible(false);
            e.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);

            e.addPassenger(entity);
        } else {
            UsefulThings.sitEntity(entity);
        }
    }

    /**
     * Used to sit an entity at it's location. Conditions : entity must : be on ground, not swimming, not jumping
     * @param entity the entity to sit
     * @param location where to sit the entity
     */
    public static void sitEntity(LivingEntity entity, Location location){
        if(entity.getLocation().subtract(0,0.1,0).getBlock().isSolid() && !entity.isJumping() && !entity.isSwimming()){
            ArmorStand e = (ArmorStand) entity.getWorld().spawnEntity(location.subtract(0,1,0), EntityType.ARMOR_STAND);
            e.setGravity(false);
            e.setVisible(false);
            e.setInvulnerable(true);
            e.setSmall(true);
            e.setCustomName("seat");
            e.setCustomNameVisible(false);
            e.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);

            e.addPassenger(entity);

        } else {
            if(entity instanceof Player){
                entity.sendActionBar(Component.text(ChatColor.RED + "you must be on ground to sit"));
            }
        }
    }

    /**
     * Used to sit an entity at it's location. Conditions : entity must : be on ground, not swimming, not jumping
     * @param entity the entity to sit
     * @param location where to sit the entity
     * @param forcesit if the sit should be forced or not (ignore Conditions)
     */
    public static void sitEntity(LivingEntity entity, Location location, boolean forcesit){
        if(forcesit){
            ArmorStand e = (ArmorStand) entity.getWorld().spawnEntity(location.subtract(0,1,0), EntityType.ARMOR_STAND);
            e.setGravity(false);
            e.setVisible(false);
            e.setInvulnerable(true);
            e.setSmall(true);
            e.setCustomName("seat");
            e.setCustomNameVisible(false);
            e.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
            e.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);

            e.addPassenger(entity);
        } else {
            UsefulThings.sitEntity(entity, location);
        }
    }

    public static boolean isDead(Player player){
        return player.isInvulnerable() && player.isInvisible() && player.getHealth() <= 1;

    }

                            //imported

    public static int getWorldDay(@NotNull World w){
        return (int) Math.floor((double) (w.getFullTime()/24000));
    }

    public static boolean isPlayerCrawling(@NotNull Player p){
        return p.getEyeLocation().getY() - p.getLocation().getY() <= 0.5 && !p.isGliding() && !p.isInWater() && !p.isRiptiding() && !p.isFlying() && !p.isSleeping();
    }

    // Items Stats

    public static void setItemImmovable(@NotNull ItemMeta meta, boolean state){
        if(state){
            meta.getPersistentDataContainer().set(DataHolder.getImmovablekey(), PersistentDataType.INTEGER, 1);
        }else{
            meta.getPersistentDataContainer().set(DataHolder.getImmovablekey(), PersistentDataType.INTEGER, 0);
        }

    }

    public static boolean isItemImmovable(ItemStack item){
        if(item != null && item.hasItemMeta()){
            return Objects.equals(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getImmovablekey(), PersistentDataType.INTEGER), 1);
        } else{
            return false;
        }

    }

    public static void setPlayerRole(@NotNull Player player, String role){
        player.getPersistentDataContainer().set(DataHolder.getRoleKey(), PersistentDataType.STRING, role);
    }

    public static @Nullable String getPlayerRole(@NotNull Player player){
        if(player.getPersistentDataContainer().get(DataHolder.getRoleKey(), PersistentDataType.STRING) != null ){
            return player.getPersistentDataContainer().get(DataHolder.getRoleKey(), PersistentDataType.STRING);
        } else{
            return null;
        }
    }

    public static void setItemType(@NotNull ItemStack item, String itemtype){
        item.getItemMeta().getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, itemtype);
    }

    public static @Nullable String getItemType(@NotNull ItemStack item){
        if(item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING) != null ){
            return item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING);
        } else{
            return null;
        }
    }

    public static void handcuffPlayer(Player p){

        ItemStack item = new ItemStack(Material.IRON_NUGGET);
        ItemMeta meta = item.getItemMeta();
        ArrayList<Component> lore = new ArrayList<>();

        item.setType(Material.IRON_NUGGET);
        meta.displayName(Component.text(ChatColor.GRAY + "Handcuffs"));
        meta.setUnbreakable(true);
        meta.setLocalizedName("handcuffsON");
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.RED + "You are now handcuffed, you are prevented from :"));
        lore.add(Component.text(ChatColor.DARK_RED + "- Changing your item in main hand"));
        lore.add(Component.text(ChatColor.DARK_RED + "- Changing your armor"));
        lore.add(Component.text(ChatColor.DARK_RED + "- Interacting with blocks"));
        lore.add(Component.text(ChatColor.DARK_RED + "- Interacting with chest/furnace etc..."));
        lore.add(Component.text(ChatColor.DARK_RED + "- Interacting with horses/donkeys/mules"));
        lore.add(Component.text(ChatColor.DARK_RED + "- Dropping items"));
        UsefulThings.setItemImmovable(meta, true);
        meta.lore(lore);

        item.setItemMeta(meta);

        if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getItemInMainHand());
        }

        if (p.getInventory().getItemInOffHand().getType() != Material.AIR) {
            p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getItemInOffHand());
        }
        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 0.75f);
        p.getInventory().setItem(p.getInventory().getHeldItemSlot(), item);
        p.getInventory().setItem(EquipmentSlot.OFF_HAND, item);
    }

    public static void uncuffPlayer(Player p){
        if(p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInOffHand().hasItemMeta()){
            ItemMeta metamain = p.getInventory().getItemInMainHand().getItemMeta();
            ItemMeta metaoff = p.getInventory().getItemInOffHand().getItemMeta();

            if(UsefulThings.isItemImmovable(p.getInventory().getItemInMainHand()) && UsefulThings.isItemImmovable(p.getInventory().getItemInOffHand())){
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), null);
                p.getInventory().setItem(EquipmentSlot.OFF_HAND, null);
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1f, 1.5f);
            }
        }


    }

    public static void spawnEntity(Location l, String entityname, int count){
        for(int i = 1; i<= count; i++){
            spawnEntity(l, entityname);
        }
    }

    public static void spawnEntity(Location l, String entityname){
            switch(entityname){
                case("firefly"):
                    Bat e = (Bat) l.getBlock().getWorld().spawnEntity(l, EntityType.BAT);
                    e.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100000,1, false, false));
                    e.setInvisible(true);
                    e.getPersistentDataContainer().set(DataHolder.getSpecialEntity(), PersistentDataType.STRING, "firefly");
                    e.setCustomName("Fire Fly");
                    e.setSilent(true);

            }
    }

    public static ItemStack getItem(String itemname){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        ArrayList<Component> lore = new ArrayList<>();
        switch(itemname){
            case "parch":
                item.setType(Material.PAPER);
                meta.displayName(Component.text(ChatColor.WHITE + "Blank Parchment"));
                meta.setLocalizedName("blankparchment");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GOLD +"To sign it, crouch and use"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case "music_parchment":
                item.setType(Material.PAPER);
                meta.displayName(Component.text(ChatColor.WHITE + "Music Parchment"));
                meta.setLocalizedName("music_parchment");
                // LOTR : kp0000000000nn000000nnnnpg000000000000000000uuwwx0000000wwuuss000000uuwwru0000000000ds000000rb00pk0000000000nb000000kknnkp000000000000000000uuw0xa0000000wwuuks000000uu00pw0000000000ww
                // NGGYU : gillp00p00n00000gilin00n00l00ki00gilil000n0k00igg00n000lz
                // THE SHIRE : ikm000p000m000kki000000000k000p000r000u0t00000p0m00000nmk00000ikm000p000mk00i00ki000000000m000p000r00000p000m000k0000000k000000iki
                
                
                String notes = "gillp00p00n00000gilin00n00l00ki00gilil000n0k00igg00n000lz";
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, notes);
                lore = UsefulThings.noteToLore("Concerning Hobbits", notes.toCharArray(),10);
                lore.add(Component.text(""));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case "hammer":
                item.setType(Material.STICK);
                meta.displayName(Component.text(ChatColor.WHITE + "Hammer"));
                meta.setLocalizedName("hammer");
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case "ring":
                item.setType(Material.GOLD_NUGGET);
                meta.displayName(Component.text(ChatColor.GOLD + "Golden Ring"));
                meta.setLocalizedName("ring_base_gold");
                lore.add(Component.text(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "unique : " + ChatColor.MAGIC + (int)((Math.random()*10)-1) + (int)((Math.random()*10)-1)));
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GOLD+ ""+ ChatColor.ITALIC +"classic, stylish, never gets old"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "ring");
                break;
            case "crystal":
                item.setType(Material.AMETHYST_SHARD);
                meta.displayName(Component.text(ChatColor.LIGHT_PURPLE + "Resonant Crystal"));
                meta.setLocalizedName("resonantcrystal");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.DARK_GRAY + "I MUST FIND A WAY TO CREATE CHANNELS IN LORE !"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case "strawhat":
                item.setType(Material.WHEAT);
                meta.displayName(Component.text(ChatColor.YELLOW + "Straw Hat"));
                meta.setLocalizedName("strawhat");
                lore.add(Component.text(""));
                lore.add(Component.text("Shift Click on air above your head to wear it"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat");
                break;
            case "fez":
                item.setType(Material.RED_DYE);
                meta.displayName(Component.text(ChatColor.RED + "Fez"));
                meta.setLocalizedName("fez");
                lore.add(Component.text(""));
                lore.add(Component.text("Greetings traveler !"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat");
                break;
            case "kirby":
                item.setType(Material.PINK_DYE);
                meta.displayName(Component.text(ChatColor.LIGHT_PURPLE + "Kirby"));
                meta.setLocalizedName("kirby");
                lore.add(Component.text(""));
                lore.add(Component.text("A very special friend"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat");
                break;
            case "olapapi":
                item.setType(Material.GREEN_DYE);
                meta.displayName(Component.text(ChatColor.DARK_RED + "Hola ¿Que Tal?"));
                meta.setLocalizedName("olapapi");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.DARK_PURPLE + "Hehehe, me no abla tacos"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat");
                break;
            case "monocle":
                item.setType(Material.GOLD_NUGGET);
                meta.displayName(Component.text(ChatColor.GOLD + "Monocle"));
                meta.setLocalizedName("monocle");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GOLD + "" + ChatColor.ITALIC + "Oh, hello !"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat"); // if item is a wearable. Possible wearables : head, chest, legs, feet, ring
                break;
            case "dice6":
                item.setType(Material.FLINT);
                meta.displayName(Component.text(ChatColor.GOLD + "Dice 6"));
                meta.setLocalizedName("dice6_black");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.LIGHT_PURPLE + "1D6 yeah baby !"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "dice6");
                break;
            case "iron_dagger":
                item.setType(Material.IRON_SWORD);
                meta.displayName(Component.text(ChatColor.GRAY +  "Iron Dagger"));
                meta.setLocalizedName("daggeriron");
                lore.add(Component.text(""));
                lore.add(Component.text(""));
                meta.lore(lore);
                //meta.setUnbreakable(false);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "dagger"); // set item type if it's needed (optional) (useful for instruments etc...)meta.getPersistentDataContainer().set(ison, PersistentDataType.INTEGER, 0); // set if item is "on" (1) or "off" (0), useful if you want to make an actionable item (flashlight, resonant crystal, instrument)
                break;
            case "iron_curved_dagger":
                item.setType(Material.IRON_SWORD);
                meta.displayName(Component.text(ChatColor.GRAY +  "Iron Curved Dagger"));
                meta.setLocalizedName("curveddaggeriron");
                lore.add(Component.text(""));
                lore.add(Component.text(""));
                meta.lore(lore);
                //meta.setUnbreakable(false);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "curveddagger"); // set item type if it's needed (optional) (useful for instruments etc...)meta.getPersistentDataContainer().set(ison, PersistentDataType.INTEGER, 0); // set if item is "on" (1) or "off" (0), useful if you want to make an actionable item (flashlight, resonant crystal, instrument)
                break;
            case "handcuffs":
                item.setType(Material.IRON_NUGGET);
                meta.displayName(Component.text(ChatColor.GRAY + "Handcuffs"));
                meta.setUnbreakable(true);
                meta.setLocalizedName("handcuffs");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GREEN + "Right Click at someone to prevent him from using items"));
                meta.lore(lore);
                break;
            case "blindfold":
                item.setType(Material.PAPER);
                meta.displayName(Component.text(ChatColor.GRAY + "Blindfold"));
                meta.setLocalizedName("blindfold");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GREEN + "Right-click at someone or wear it to blind the wearer"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat"); // if item is a wearable. Possible wearables : head, chest, legs, feet, ring
                break;
            case "bouboule":
                item.setType(Material.GOLD_NUGGET);
                meta.displayName(Component.text(ChatColor.RED + "La Bouboule"));
                meta.setLocalizedName("bouboule");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.LIGHT_PURPLE + "BAH ALORS PETIT COQUINOU !"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat"); // if item is a wearable. Possible wearables : head, chest, legs, feet, ring
                break;
            case "bat_bottle":
                item.setType(Material.QUARTZ);
                meta.displayName(Component.text(ChatColor.WHITE + "Bat in a Bottle"));
                meta.setLocalizedName("batbottle");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY + "Right click to release it"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case "bee_bottle":
                item.setType(Material.QUARTZ);
                meta.displayName(Component.text(ChatColor.WHITE + "Bee in a Bottle"));
                meta.setLocalizedName("beebottle");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY + "Right click to release it"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case "firefly_bottle":
                item.setType(Material.QUARTZ);
                meta.displayName(Component.text(ChatColor.WHITE + "Firefly in a Bottle"));
                meta.setLocalizedName("fireflybottle");
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY + "Right click in air to release it"));
                lore.add(Component.text(ChatColor.GRAY + "or Right click on ground to make a fire burst"));
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "note : FIRE BURNS, BE CAREFUL"));
                meta.lore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case "spoon":
                item.setType(Material.STICK);
                meta.displayName(Component.text(ChatColor.WHITE + "Spoon"));
                meta.setLocalizedName("spoon");
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case "raid_horn":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.GRAY + "Raid Horn"));
                meta.setLocalizedName("raid_horn");
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "horn");
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case "iron_spear":
                item.setType(Material.STICK);
                meta.displayName(Component.text(ChatColor.WHITE + "Iron Spear"));
                meta.setLocalizedName("iron_spear");
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case "wall_bomb":
                item.setType(Material.FIREWORK_STAR);
                meta.displayName(Component.text(ChatColor.WHITE + "Wall Bomb"));
                meta.setLocalizedName("wall_bomb");
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY + "Charge : 0"));
                lore.add(Component.text(ChatColor.GRAY + "Fuse : 1s"));
                int[] values = {0, 20};
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY, values);
                meta.lore(lore);
                break;
            case "oak_flute":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.WHITE + "Oak Flute"));
                meta.setLocalizedName("oak_flute");
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
                
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.lore(lore);
                break;
            case "oak_lyre":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.WHITE + "Oak Lyre"));
                meta.setLocalizedName("oak_lyre");
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
        
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.lore(lore);
                break;
            case "oak_guitar":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.WHITE + "Oak Guitar"));
                meta.setLocalizedName("oak_guitar");
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
        
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.lore(lore);
                break;
            case "oak_doublebass":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.WHITE + "Oak Double-Bass"));
                meta.setLocalizedName(itemname);
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
        
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.lore(lore);
                break;
            case "oak_harp":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.WHITE + "Oak Harp"));
                meta.setLocalizedName(itemname);
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
        
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.lore(lore);
                break;
            case "oak_koto":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.WHITE + "Oak Koto"));
                meta.setLocalizedName(itemname);
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
        
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.lore(lore);
                break;
            case "oak_oud":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.WHITE + "Oak Oud"));
                meta.setLocalizedName(itemname);
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
        
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.lore(lore);
                break;
            case "oak_panflute":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.WHITE + "Oak Pan Flute"));
                meta.setLocalizedName("oak_panflute");
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
        
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.lore(lore);
                break;
            case "oak_sitar":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.WHITE + "Oak Sitar"));
                meta.setLocalizedName(itemname);
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
        
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.lore(lore);
                break;
            case "oak_banjo":
                item.setType(Material.SHIELD);
                meta.displayName(Component.text(ChatColor.WHITE + "Oak Banjo"));
                meta.setLocalizedName(itemname);
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
        
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.lore(lore);
                break;
                /*

                case "":
                    item.setType();
                    meta.displayName(Component.text(""));
                    meta.setLocalizedName("");
                    lore.add(Component.text(""));
                    lore.add(Component.text(""));
                    meta.lore(lore);
                    meta.setUnbreakable(true);
                    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    //meta.getPersistentDataContainer().set(keywear, PersistentDataType.STRING, ""); // if item is a wearable. Possible wearables : head, chest, legs, feet, ring
                    //add item
                    item.setItemMeta(meta);
                    p.getInventory().addItem(item);
                    return true;


                */

        }
        item.setItemMeta(meta);
        return item;
    }

    public static float getVolume(int distance){
        return distance*6.4f;
    }

    public static float getVolume(float basevolume, Biome biome){
        float modifier = 1;
        String biomename = biome.toString();
        if(biomename.contains("MOUNTAINS") || biomename.contains("HILLS")) {
            modifier = 2;
        } else if(biomename.contains("OCEAN")){
            modifier = 1.5f;
        } else if(biomename.contains("JUNGLE") || biomename.contains("FOREST")) {
            modifier = 0.75f;
        }

        return modifier;
    }

    public static ArmorStand createDisplay(Location loc, ItemStack head){
        ArmorStand arm = (ArmorStand) loc.getWorld().spawnEntity(loc.subtract(0,17/16f,0), EntityType.ARMOR_STAND);
        arm.setVisible(false);
        arm.setInvulnerable(true);
        arm.setGravity(false);
        arm.setDisabledSlots(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
        arm.getEquipment().setHelmet(head);
        arm.setCustomName("_hide");
        arm.setCustomNameVisible(false);

        return arm;
    }

    public static boolean isDisplay(Entity e){
        return e instanceof ArmorStand && !e.hasGravity() && !((ArmorStand) e).getDisabledSlots().isEmpty() && ((ArmorStand) e).isInvisible();
        //

    }
    
    public static ItemStack setBombCharge(ItemStack item, int amount){
        ItemMeta meta = item.getItemMeta();
        item = item.clone();
        List<Component> lore = meta.lore();
        lore.set(1, Component.text(ChatColor.GRAY + "Charge : " + amount));
        meta.lore(lore);
        int[] data = meta.getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY);
        if(data != null){
            if(data.length >= 2){
                data[0] = amount;
            }
            meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY, data);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack setBombFuse(ItemStack item, int amount){
        item = item.clone();
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore();
        lore.set(2, Component.text(ChatColor.GRAY + "Fuse : " + (amount/20f) + "s"));
        meta.lore(lore);
        int[] data = meta.getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY);
        if(data != null){
            if(data.length >= 2){
                data[1] = amount;
            }
            meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY, data);
        }
    
        item.setItemMeta(meta);
        return item;
    }
    
    public static float noteToPitch(char note){
        note = Character.toLowerCase(note);
        switch(note){
            case('a'):
                return 0.5f;
            case('b'):
                return 0.529732f;
            case('c'):
                return 0.561231f;
            case('d'):
                return 0.594604f;
            case('e'):
                return 0.629961f;
            case('f'):
                return 0.667420f;
            case('g'):
                return 0.707107f;
            case('h'):
                return 0.749154f;
            case('i'):
                return 0.793701f;
            case('j'):
                return 0.840896f;
            case('k'):
                return 0.890899f;
            case('l'):
                return 0.943874f;
            case('m'):
                return 1.0f;
            case('n'):
                return 1.059463f;
            case('o'):
                return 1.122462f;
            case('p'):
                return 1.189207f;
            case('q'):
                return 1.259921f;
            case('r'):
                return 1.334840f;
            case('s'):
                return 1.414214f;
            case('t'):
                return 1.498307f;
            case('u'):
                return 1.587401f;
            case('v'):
                return 1.681793f;
            case('w'):
                return 1.781797f;
            case('x'):
                return 1.887749f;
            case('y'):
                return 2.0f;
            case('z'):
                return -1f;
            case('0'):
                return 0f;
            default:
                return -2f;
        }
    }
    
    public static float[] noteToPitch(char[] note){
        float[] pitch = new float[note.length];
        for(int i = 0; i < note.length; i++){
            pitch[i] = noteToPitch(note[i]);
        }
        return pitch;
    }
    
    public static float[] noteToPitch(char[] note, int forcedlength){
        float[] pitch = new float[forcedlength];
        
        for(int i = 0; i < forcedlength; i++){
            if(i >= note.length){
                pitch[i] = -1;
            }else{
                pitch[i] = noteToPitch(note[i]);
            }
            
        }
        return pitch;
    }
    
    public static Color noteToColor(char note){
        note = Character.toLowerCase(note);
        int color = 0x000000;
        switch(note){
            case('a'):
                color = 0x77D700;
                break;
            case('b'):
                color = 0x95C000;
                break;
            case('c'):
                color = 0xB2A500;
                break;
            case('d'):
                color = 0xCC8600;
                break;
            case('e'):
                color = 0xE26500;
                break;
            case('f'):
                color = 0xF34100;
                break;
            case('g'):
                color = 0xFC1E00;
                break;
            case('h'):
                color = 0xFE000F;
                break;
            case('i'):
                color = 0xF70033;
                break;
            case('j'):
                color = 0xE8005A;
                break;
            case('k'):
                color = 0xCF0083;
                break;
            case('l'):
                color = 0xAE00A9;
                break;
            case('m'):
                color = 0x8600CC;
                break;
            case('n'):
                color = 0x5B00E7;
                break;
            case('o'):
                color = 0x2D00F9;
                break;
            case('p'):
                color = 0x020AFE;
                break;
            case('q'):
                color = 0x0037F6;
                break;
            case('r'):
                color = 0x0068E0;
                break;
            case('s'):
                color = 0x009ABC;
                break;
            case('t'):
                color = 0x00C68D;
                break;
            case('u'):
                color = 0x00E958;
                break;
            case('v'):
                color = 0x00FC21;
                break;
            case('w'):
                color = 0x1FFC00;
                break;
            case('x'):
                color = 0x59E800;
                break;
            case('y'):
                color = 0x94C100;
                break;
        }
    
        return Color.fromRGB(color);
    }
    
    public static Color[] noteToColor(char[] note){
        Color[] colors = new Color[note.length];
        for(int i = 0; i < note.length; i++){
            colors[i] = noteToColor(note[i]);
        }
        return colors;
    }
    
    public static Color[] noteToColor(char[] note, int forcedlength){
        Color[] colors = new Color[forcedlength];
        
        for(int i = 0; i < forcedlength; i++){
            if(i >= note.length){
                colors[i] = Color.BLACK;
            }else{
                colors[i] = noteToColor(note[i]);
            }
            
        }
        return colors;
    }
    
    public static ArrayList<Component> noteToLore(String title, char[] note, int linelength){
        
        ArrayList<Component> lore = new ArrayList<>();
        if(title.length() > 1){
            char[] titlechar = title.toCharArray();
            StringBuilder linetitle = new StringBuilder();
            int titlelength = linelength + linelength + 3;
    
            lore.add(Component.text(""));
            linetitle.append("   ");
            for(int i0=0; i0<titlelength; i0++){
                if(titlechar.length > i0){
                    linetitle.append(titlechar[i0]);
                }
            }
            lore.add(Component.text(ChatColor.GOLD + ""+linetitle));
            lore.add(Component.text(""));
        }
        
        for(int i = 1; i<=(note.length/linelength)+1; i++){
            StringBuilder line = new StringBuilder();
            for(int i2 = 0; i2 < linelength; i2++){
                if(i2*i < note.length){
                    line.append(Character.toUpperCase(note[i2 * i]));
                }
                line.append(" ");
            }
            
            lore.add(Component.text(ChatColor.GOLD + " "+i+"    "+ChatColor.BLUE+line+" "));
        }
        return lore;
    }
    
    public static ArrayList<Component> noteToLore(char[] note, int linelength){
        return noteToLore("", note, linelength);
    }
    
    public static void playMusic(float[] pitch, Player p, boolean shouldhold, int speed, Sound sound){
        if(speed <= 0){
            return;
        }
        new BukkitRunnable(){
            int timer = 0;
            
            @Override
            public void run(){
                if((timer >= pitch.length) || (!p.isBlocking() && shouldhold && timer > 2) || (timer >= 512 || pitch[timer] < 0)) {
                    this.cancel();
                    return;
                }
                if(pitch[timer] > 0){
                    Location loc = p.getLocation();
                    if(p.isSneaking()){
                        loc = loc.subtract(0,0.5,0);
                    }
                    loc.getWorld().playSound(loc.clone().add(0,1.5,0), sound, 1, pitch[timer]);
                    loc.getWorld().spawnParticle(Particle.NOTE, loc.clone().add(0,1.5+pitch[timer],0), 1,.1,0,.1,pitch[timer]/10);
                }
                timer++;
            }
        }.runTaskTimer(Stones.getPlugin(), 0,speed);
        
    }
    
    public static void playMusic(String note, Player p, boolean shouldhold, int speed, Sound sound){
        char[] notes = note.toCharArray();
        float[] pitch = UsefulThings.noteToPitch(notes);
        playMusic(pitch, p, shouldhold, speed, sound);
    }
    
    public static void playMusic(String note, Player p){
        playMusic(note, p, false, 2, Sound.BLOCK_NOTE_BLOCK_HARP);
    }
    
    public static void playMusic(float[] pitch, Player p, boolean shouldhold, int speed, String sound){
        if(speed <= 0){
            return;
        }
        new BukkitRunnable(){
            int timer = 0;
            
            @Override
            public void run(){
                if((timer >= pitch.length) || (!p.isBlocking() && shouldhold && timer > 2) || (timer >= 1800 || pitch[timer] < 0)) {
                    this.cancel();
                    return;
                }
                if(pitch[timer] > 0){
                    Location loc = p.getLocation();
                    if(p.isSneaking()){
                        loc = loc.subtract(0,0.5,0);
                    }
                    loc.getWorld().playSound(loc.clone().add(0,1.5,0), sound, 1, pitch[timer]);
                    loc.getWorld().spawnParticle(Particle.NOTE, loc.clone().add(0,1.5+pitch[timer],0), 1,.1,0,.1,pitch[timer]/10);
                }
                timer++;
            }
        }.runTaskTimer(Stones.getPlugin(), 0,speed);
        
    }
    
    public static void playMusic(String note, Player p, boolean shouldhold, int speed, String sound){
        char[] notes = note.toCharArray();
        float[] pitch = UsefulThings.noteToPitch(notes);
        playMusic(pitch, p, shouldhold, speed, sound);
    }


}
