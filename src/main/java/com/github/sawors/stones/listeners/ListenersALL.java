package com.github.sawors.stones.listeners;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.core.database.DataHolder;
import com.github.sawors.stones.items.SItem;
import com.github.sawors.stones.items.StonesItems;
import com.github.sawors.stones.magic.MagicExecutor;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Jukebox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ListenersALL implements Listener {

    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    //ALL PRAISE THE XP ORB REMOVER  \i/ MAY HIS POWER CURE US OF THE DISEASE THAT CONSUME OUR WORLD \i/
    

    

    


    @EventHandler
    public void preventForbiddenCrafts(InventoryClickEvent event){
        //System.out.println("Yaaaaaa 1");
        if(event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().isUnbreakable()){
            //System.out.println("Yaaaaaa 2");
            InventoryType itype = event.getInventory().getType();
            if(!( // A list of inventory where custom items (which are not usable in custom crafts) are forbidden
                    itype == InventoryType.PLAYER
                        || itype == InventoryType.BARREL
                        || itype == InventoryType.CHEST
                        || itype == InventoryType.ENDER_CHEST
                        || itype == InventoryType.CREATIVE
                        || itype == InventoryType.DISPENSER
                        || itype == InventoryType.DROPPER
                        || itype == InventoryType.HOPPER
                        || itype == InventoryType.SHULKER_BOX
                        || itype == InventoryType.CRAFTING
                        || itype == InventoryType.ANVIL
            )){
                event.setCancelled(true);
                //System.out.println("Yaaaaaa 3");
            }
        }
        if(event.getCursor() != null && event.getCursor().getEnchantments().containsKey(Enchantment.FIRE_ASPECT) && event.getCursor().getEnchantmentLevel(Enchantment.FIRE_ASPECT) == 1 && Bukkit.getPlayer(event.getWhoClicked().getName()) != null){
            UsefulThings.extinguishItem(event.getCursor(), "normal", Bukkit.getPlayer(event.getWhoClicked().getName()));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        Block b = event.getClickedBlock();
    
        if (item.hasItemMeta() && item.getItemMeta().hasLocalizedName()) {
            String locname = item.getItemMeta().getLocalizedName();
        
            //add Closed Seal item
            if (Objects.equals(locname, "raid_horn") && event.getAction().isRightClick() && event.getPlayer().getCooldown(Material.SHIELD) <= 0) {
                //Location soundloc = p.getLocation();
                net.kyori.adventure.sound.Sound sound = net.kyori.adventure.sound.Sound.sound(Sound.EVENT_RAID_HORN, net.kyori.adventure.sound.Sound.Source.PLAYER, UsefulThings.getVolume(24), 1);
    
                p.getWorld().playSound(sound, net.kyori.adventure.sound.Sound.Emitter.self());
                new BukkitRunnable() {
    
                    final int max = 8;
                    int countdown = max;
    
                    @Override
                    public void run() {
        
                        if (countdown <= 0 || Bukkit.getOnlinePlayers().isEmpty()) {
                            if (!p.getWorld().getNearbyPlayers(p.getLocation(), 24).isEmpty()) {
                                for (Player player : p.getWorld().getNearbyPlayers(p.getLocation(), 24)) {
                                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 2, UsefulThings.randomPitchSimple(0.4, 1));
                                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 2, UsefulThings.randomPitchSimple(0.4, 1));
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (((-p.getLocation().distance(player.getLocation()) / 24 / 1.25) + 1) * 30 * 20), 0, false, false));
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (((-p.getLocation().distance(player.getLocation()) / 24 / 1.25) + 1) * 30 * 20), 0, false, false));
                                    p.setCooldown(Material.SHIELD, 20 * 8);
                                }
                            }
                            this.cancel();
                            return;
                        }
                        if (countdown == max) {
                            if (event.hasItem() && event.getItem().hasItemMeta() && Objects.equals(event.getItem().getItemMeta().getLocalizedName(), "raid_horn")) {
                                p.spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 3.25 + ((float) -countdown / max), 0), 4, 0.1, .25, 0.1, new Particle.DustOptions(Color.fromRGB(0x45171f), 1));
                            } else {
                                p.getWorld().stopSound(sound);
                                this.cancel();
                                return;
                            }
                        } else {
                            if (p.isBlocking() && event.hasItem() && event.getItem().hasItemMeta() && Objects.equals(event.getItem().getItemMeta().getLocalizedName(), "raid_horn")) {
                                p.spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 3.25 + ((float) -countdown / max), 0), 4, 0.1, .25, 0.1, new Particle.DustOptions(Color.fromRGB(0x45171f), 1));
                            } else {
                                p.getWorld().stopSound(sound);
                                this.cancel();
                                return;
                            }
                        }
        
                        countdown--;
                    }
    
    
                }.runTaskTimer(Stones.getPlugin(), 0, 10);
            }
            
        
            if (locname.contains("dice") && item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING) != null && item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING).equals("dice6")) {
                int roll = ((int) (Math.random() * 6) + 1);
    
                p.sendMessage(ChatColor.YELLOW + "You roll a " + ChatColor.GOLD + "" + ChatColor.BOLD + roll);
            }
        }
    }
    
    
    
        

        @EventHandler
        public void onItemSwitch(PlayerItemHeldEvent event){
            Player p = event.getPlayer();
            if(p.getInventory().getItem(event.getPreviousSlot()) != null && p.getInventory().getItem(event.getPreviousSlot()).hasItemMeta() && p.getInventory().getItem(event.getPreviousSlot()).getItemMeta().getEnchants().get(Enchantment.FIRE_ASPECT) != null && p.getInventory().getItem(event.getPreviousSlot()).getItemMeta().getEnchants().get(Enchantment.FIRE_ASPECT) <= 1){
                UsefulThings.extinguishItem(p.getInventory().getItem(event.getPreviousSlot()), "normal", p);
            }
        }

        @EventHandler
        public void onPlayerClickEntity(PlayerInteractEntityEvent event){
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            if(event.getRightClicked() instanceof Player){
                Player rightclicked = (Player) event.getRightClicked();
                if(player.getInventory().getItemInMainHand().hasItemMeta()){
                    switch(item.getItemMeta().getLocalizedName()){
                        case "handcuffs":
                            if(UsefulThings.isBehind(player, rightclicked, 45)){
                                UsefulThings.handcuffPlayer(rightclicked);
                                ItemStack itemtemp = player.getInventory().getItemInMainHand().clone();
                                itemtemp.setAmount(itemtemp.getAmount()-1);
                                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), itemtemp);
                            }
                        case "blindfold":
                            if(UsefulThings.isBehind(player, rightclicked, 45) && rightclicked.getInventory().getHelmet() == null){
                                rightclicked.getInventory().setHelmet(StonesItems.get(SItem.BLINDFOLD));
                                
                            }
                    }
                }
            }
        }
        
    @EventHandler
    public void onEntityBurn(EntityCombustEvent event){
            if(event.getEntity() instanceof Item){
                Item item = (Item) event.getEntity();
                if(item.getItemStack().getType() == Material.GUNPOWDER){
                    UsefulThings.spawnEntity(item.getLocation(),"firefly", item.getItemStack().getAmount());
                }
            }
    }

    @EventHandler
    public void onPlayerBlock(EntityDamageEvent event){
            if(event.getEntity() instanceof Player && ((Player) event.getEntity()).isBlocking()){
                Player p = ((Player) event.getEntity()).getPlayer();
                if(p != null && p.getInventory().getItemInMainHand().hasItemMeta() && Objects.equals(p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING), "horn")){
                    p.setHealth(p.getHealth()-event.getDamage());
//                    event.setCancelled(true);
                }
            }
    }

    @EventHandler
    public void explosionEvent(EntityExplodeEvent event){
            Entity e = event.getEntity();
            EntityType etype= e.getType();


            if(e.getType() == EntityType.PRIMED_TNT){

            } else if(e.getType() == EntityType.ARMOR_STAND){

            }else if(e.getType() == EntityType.CREEPER){

            }else if(e.getType() == EntityType.ENDER_CRYSTAL){

            }else if(e.getType() == EntityType.FIREBALL){

            }else if(e.getType() == EntityType.MINECART_TNT){

            }
    }



    @EventHandler
    public void onPlayerBlock(PlayerInteractEvent event){
            if(event.getAction().isRightClick()){
                Player p = event.getPlayer();
                if(p.getInventory().getItemInMainHand().getType() == Material.SHIELD && (p.getInventory().getItemInOffHand().getType().toString().contains("SWORD") || p.getInventory().getItemInOffHand().getType().toString().contains("AXE") || p.getInventory().getItemInOffHand().getType().toString().contains("STICK")) && p.getCooldown(Material.SHIELD) <= 0){
                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1 ,0.8f);
                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1 ,1);


                    new BukkitRunnable(){



                        int countdown = 2;
                        @Override
                        public void run() {
                            if(countdown > 0 && p.isBlocking()){
                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1 ,0.8f);
                                p.getWorld().playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1 ,1);
                            } else{
                                if(countdown == 1 && !p.isBlocking()){
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*2,0,false, false));
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*2,0,false, false));
                                }else if(countdown == 0){
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*3,0,false, false));
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3,0,false, false));
                                }
                                p.setCooldown(Material.SHIELD, 20);

                                this.cancel();
                                return;
                            }



                            countdown--;
                        }


                    }.runTaskTimer(Stones.getPlugin(), 8, 8);
                }
            }
    }

    @EventHandler
    public void onPlayerPlaceBomb(PlayerInteractEvent event){


            if(event.getAction().isRightClick() && Objects.equals(event.getHand(), EquipmentSlot.HAND) && !event.getPlayer().getInventory().getItemInMainHand().getType().isAir() && event.getClickedBlock() != null && event.getClickedBlock().isSolid() && event.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&  event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("wall_bomb") && UsefulThings.getFaceMidLocation(event.getClickedBlock(), event.getBlockFace()).getNearbyEntities(0.05,0.05,0.05).isEmpty()){

                event.setCancelled(true);

                Player p = event.getPlayer();
                Block b = event.getClickedBlock();
                Location face = UsefulThings.getFaceMidLocation(b, event.getBlockFace(), 0.01, new Vector(0,0.3,0),new Vector(0,0.3,0));
                BlockFace blockface = event.getBlockFace();
                ItemStack playerhand = p.getInventory().getItemInMainHand();

                ItemStack bomb = event.getPlayer().getInventory().getItemInMainHand().clone();
                bomb.setAmount(1);
                ArmorStand stand = UsefulThings.createDisplay(face, bomb);
                stand.setSmall(true);
                stand.setInvulnerable(false);

                if(blockface.equals(BlockFace.NORTH)){
                    stand.setHeadPose(new EulerAngle(0,0, Math.toRadians((Math.random()-0.5)*10)));
                } else if (blockface.equals(BlockFace.SOUTH)){
                    stand.setHeadPose(new EulerAngle(0,0,Math.toRadians((Math.random()-0.5)*10)));
                }else if(blockface.equals(BlockFace.EAST)){
                    stand.setHeadPose(new EulerAngle(0,0,Math.toRadians((Math.random()-0.5)*10)));
                }else if(blockface.equals(BlockFace.WEST)){
                    stand.setHeadPose(stand.getHeadPose().add(0,0,Math.toRadians((Math.random()-0.5)*10)));
                }else if(blockface.equals(BlockFace.UP)){
                    stand.setHeadPose(new EulerAngle(-3.14/2,Math.toRadians((Math.random()-0.5)*360),0));;
                }else if(blockface.equals(BlockFace.DOWN)){
                    stand.setHeadPose(new EulerAngle(3.14/2,Math.toRadians((Math.random()-0.5)*360),0));
                }
    
                stand.getWorld().playSound(stand.getLocation(), Sound.BLOCK_WOOD_PLACE, 0.5f, 1.25f);
                if(event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.ADVENTURE){
                    playerhand.setAmount(playerhand.getAmount()-1);
                }

                //p.getInventory().getItemInMainHand().setAmount(0);

            }


    }

    @EventHandler
    public void onPlayerInteractWithBomb(PlayerInteractAtEntityEvent event){

            if(event.getRightClicked() instanceof ArmorStand && ((ArmorStand) event.getRightClicked()).getEquipment().getHelmet() != null && ((ArmorStand) event.getRightClicked()).getEquipment().getHelmet().hasItemMeta() && ((ArmorStand) event.getRightClicked()).getEquipment().getHelmet().getItemMeta().getLocalizedName().equals("wall_bomb")){
                ArmorStand stand = (ArmorStand) event.getRightClicked();
                ItemStack item = stand.getEquipment().getHelmet();
                ItemStack playerhand = event.getPlayer().getInventory().getItemInMainHand();
    
                if(!UsefulThings.getFaceMidLocation(stand.getLocation().add(0,0.8,0), stand, 0.5).getBlock().isSolid()){
                    ItemStack bomb = stand.getEquipment().getHelmet().clone();
                    stand.getWorld().playSound(stand.getLocation(), Sound.BLOCK_WOOD_BREAK, 0.5f, 1.25f);
                    for(int i = Objects.requireNonNull(bomb.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY))[0]; i > 0; i--){
                        stand.getWorld().dropItem(UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, -2/16f), new ItemStack(Material.GUNPOWDER));
                    }
                    bomb = UsefulThings.setBombCharge(bomb, 0);
                    stand.getEquipment().setHelmet(bomb);
                    stand.getWorld().dropItem(UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, -2/16f), bomb);
                    stand.remove();
                    event.setCancelled(true);
                    return;
                }
                
                if(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY) != null && Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY)).length >=2){
                    int fuse = Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY))[1];
                    int charge = Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY))[0];
                    int[] data = {charge, fuse};
                    ItemStack newbomb = item.clone();
                    

                    if(playerhand.getType() == Material.GUNPOWDER && item.lore().size() != 0){
                        //value setting
                        if(charge >=0 && charge <3){
                            if(event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.ADVENTURE){
                                playerhand.setAmount(playerhand.getAmount()-1);
                            }
                            ((ArmorStand) event.getRightClicked()).getEquipment().setHelmet(UsefulThings.setBombCharge(item, data[0]+1));
                            stand.getWorld().playSound(stand.getLocation(), Sound.BLOCK_SAND_PLACE, .75f, 1.25f);
                        }
                        
                        
                    } else if(playerhand.getType().toString().contains("SHOVEL") || item.getItemMeta().getLocalizedName().contains("spoon")){
                        //value setting
                        if(charge >0){
                            ((ArmorStand) event.getRightClicked()).getEquipment().setHelmet(UsefulThings.setBombCharge(item, data[0]-1));
                            stand.getWorld().dropItem(UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, -2/16f), new ItemStack(Material.GUNPOWDER));
                            stand.getWorld().playSound(stand.getLocation(), Sound.BLOCK_SAND_BREAK, .75f, 1.25f);
                        }
    
    
                    }else if(playerhand.getType().toString().contains("SHEARS")){
                        //value setting
                        if(fuse >20){
                            ((ArmorStand) event.getRightClicked()).getEquipment().setHelmet(UsefulThings.setBombFuse(item, data[1]-20));
                            stand.getWorld().dropItem(UsefulThings.getFaceMidLocation(stand.getLocation().add(0, 0.8, 0), stand, -2/16f), new ItemStack(Material.STRING));
                            stand.getWorld().playSound(stand.getLocation(), Sound.ENTITY_SHEEP_SHEAR, .75f, 1.25f);
                        }
                        //value display
                        ArmorStand stat = UsefulThings.createDisplay(stand.getLocation().clone().add(0,1,0), new ItemStack(Material.AIR));
                        stat.setSmall(true);
                        if(data[1] >20){
                            stat.setCustomName(ChatColor.GRAY + "Fuse : " + (data[1]-20)/20 + "s");
                        } else  {
                            stat.setCustomName(ChatColor.GRAY + "Fuse : " + data[1]/20 + "s");
                        }
                        stat.setCustomNameVisible(true);
                        new BukkitRunnable(){
        
                            @Override
                            public void run(){
                                stat.remove();
                            }
                        }.runTaskLater(Stones.getPlugin(), 5);
    
    
                    } else if(playerhand.getType() == Material.STRING && item.lore().size() != 0){
                        
                        //value setting
                        if(data[1] >=0 && data[1] < 15*20){
                            if(event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.ADVENTURE){
                                playerhand.setAmount(playerhand.getAmount()-1);
                            }
                            ((ArmorStand) event.getRightClicked()).getEquipment().setHelmet(UsefulThings.setBombFuse(item, data[1]+20));
                            stand.getWorld().playSound(stand.getLocation(), Sound.BLOCK_WOOL_PLACE, .75f, 1.25f);
                        }
                        //value display
                        ArmorStand stat = UsefulThings.createDisplay(stand.getLocation().clone().add(0,1,0), new ItemStack(Material.AIR));
                        stat.setSmall(true);
                        if(data[1] >=0 && data[1] < 15*20){
                            stat.setCustomName(ChatColor.GRAY + "Fuse : " + (data[1]+20)/20 + "s");
                        } else  {
                            stat.setCustomName(ChatColor.GRAY + "Fuse : " + data[1]/20 + "s");
                        }
                        
                        stat.setCustomNameVisible(true);
                        new BukkitRunnable(){
        
                            @Override
                            public void run(){
                                stat.remove();
                            }
                        }.runTaskLater(Stones.getPlugin(), 5);

                    } else if(item.lore().size() != 0 && (playerhand.getType() == Material.FLINT_AND_STEEL || (playerhand.hasItemMeta() && playerhand.getItemMeta().getEnchants().containsKey(Enchantment.FIRE_ASPECT)))) {
    
                        if (charge > 0){
                            stand.getWorld().playSound(stand.getLocation().clone().add(0, 0.5, 0), Sound.ITEM_FLINTANDSTEEL_USE, 1, 1);
                            ItemMeta meta = item.getItemMeta().clone();
                            meta.setLocalizedName("wall_bomb_ignited");
                            newbomb.setItemMeta(meta);
                            ((ArmorStand) event.getRightClicked()).getEquipment().setHelmet(newbomb);
                            stand.getWorld().playSound(stand.getLocation().clone().add(0, 0.5, 0), "minecraft:sawors.explosive.fusestart", .5f, 0.75f);
                        new BukkitRunnable() {
                            int timer = data[1];
                            Location moveloc = UsefulThings.getFaceMidLocation(stand.getLocation().add(0,0.8,0), stand, -1/16f).add(0, (9/16f), 0);
    
                            @Override
                            public void run() {
                                if (timer == 0) {
    
                                    //stand.getWorld().createExplosion(stand.getLocation().add(0,1,0), data[0]*2);
                                    UsefulThings.ignite(stand);
                                    this.cancel();
                                    return;
                                }
                                Particle part;
        
                                if (stand.getLocation().add(0, 0.8, 0).getBlock().getType().equals(Material.WATER)) {
                                    part = Particle.WATER_BUBBLE;
                                } else {
                                    part = Particle.FLAME;
                                }
        
                                moveloc = moveloc.subtract(0, (3/16f) / data[1], 0);
        
                                for (int i = 0; i < 4; i++) {
                                    stand.getWorld().spawnParticle(Particle.SMOKE_NORMAL, moveloc, 0, (Math.random() - .5) / 20, .1, (Math.random() - .5) / 20, 2);
            
            
                                    stand.getWorld().spawnParticle(part, moveloc, 0, (Math.random() - 0.5) / 20, .1, (Math.random() - .5) / 20, 2);
                                }
        
                                if (timer >= 10) {
            
                                    float pitch;
            
                                    if (stand.getLocation().add(0, 0.8, 0).getBlock().getType().equals(Material.WATER)) {
                                        pitch = .5f;
                                    } else {
                                        pitch = .8f;
                                    }
                                    float p2 = (((pitch * (data[1] - timer)) / data[1]) / 2) + .75f;
                                    stand.getWorld().playSound(stand.getLocation().clone().add(0, 0.5, 0), "minecraft:sawors.explosive.fuse", .15f, p2);
            
                                }
                                
                                timer--;
                              }
    
                            }.runTaskTimer(Stones.getPlugin(), 1, 1);
                        } else{
                            stand.getWorld().playSound(stand.getLocation().clone().add(0, 0.5, 0), Sound.ITEM_FLINTANDSTEEL_USE, .5f, 1.5f);
                        }
                    }
                }



            }
    }

    @EventHandler
    public void onPlayerDestroyBomb(EntityDamageEvent event){
            if(event.getEntity() instanceof ArmorStand){

                ArmorStand armorstand = (ArmorStand) event.getEntity();

                if(armorstand.getEquipment().getHelmet() != null && armorstand.getEquipment().getHelmet().hasItemMeta() && armorstand.getEquipment().getHelmet().getItemMeta().getLocalizedName().equals("wall_bomb")){

                    event.setCancelled(true);
                    ItemStack bomb = armorstand.getEquipment().getHelmet().clone();
                    armorstand.getWorld().playSound(armorstand.getLocation(), Sound.BLOCK_WOOD_BREAK, 0.5f, 1.25f);
                    
                    for(int i = Objects.requireNonNull(bomb.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY))[0]; i > 0; i--){
                        armorstand.getWorld().dropItem(UsefulThings.getFaceMidLocation(armorstand.getLocation().add(0, 0.8, 0), armorstand, -2/16f), new ItemStack(Material.GUNPOWDER));
                    }
                    bomb = UsefulThings.setBombCharge(bomb, 0);
                    armorstand.getEquipment().setHelmet(bomb);
                    armorstand.getWorld().dropItem(UsefulThings.getFaceMidLocation(armorstand.getLocation().add(0, 0.8, 0), armorstand, -2/16f), bomb);
                    armorstand.remove();
                }
            }

    }
    
    @EventHandler
    public void onPlayerUsesJukebox(PlayerInteractEvent event){
            if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.JUKEBOX && event.getAction().isRightClick()){
                Block b = event.getClickedBlock();
                Jukebox j = (Jukebox) b.getState();
                Location midloc = b.getLocation().clone().add(0.5,0.5,0.5);
                Location uploc = b.getLocation().clone().add(0.5,2,0.5);
                Player p = event.getPlayer();
                
                ItemStack disk;
                
                if(p.getInventory().getItemInMainHand().getType().toString().contains("MUSIC_DISC")){
                    disk = p.getInventory().getItemInMainHand().clone();
                } else if(p.getInventory().getItemInOffHand().getType().toString().contains("MUSIC_DISC")){
                    disk = p.getInventory().getItemInOffHand().clone();
                } else {
                    disk = new ItemStack(Material.AIR);
                }
                
                if(j.isPlaying()){
                    if((p.isSneaking() && p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) || (!p.isSneaking())){
                        for(Entity e : uploc.add(0,0.1,0).getNearbyEntities(0.1,0.25,0.1)){
                            if(e instanceof ArmorStand && ((ArmorStand) e).getEquipment().getHelmet() != null && ((ArmorStand) e).getEquipment().getHelmet().getType().toString().contains("MUSIC_DISC")){
                                e.remove();
                            }
                        }
                    }
                    
                } else if(disk.getType() != Material.AIR){
                    ArmorStand display = UsefulThings.createDisplay(uploc, disk);
                    display.setSmall(true);
                    new BukkitRunnable(){
                        
                        int timer = 3600;
                        Location blockloc = b.getLocation().clone();
                        @Override
                        public void run(){
                            
                            if(timer <= 0){
                                this.cancel();
                            } else{
                                if(blockloc.getBlock().getType() != Material.JUKEBOX){
                                    display.remove();
                                    this.cancel();
                                }
                                display.setRotation(display.getLocation().getYaw()+2, 0);
                                timer--;
                                
                            }
                        }
                        
                    }.runTaskTimer(Stones.getPlugin(), 0, 1);
                }
                
                
                
                
            }
    }
    
    @EventHandler
    public void onPlayerBreakJukebox(BlockBreakEvent event){
            if(event.getBlock().getType() == Material.JUKEBOX){
                for(Entity e : event.getBlock().getLocation().add(0.5,1.1,0.5).getNearbyEntities(0.1,0.25,0.1)){
                    if(e instanceof ArmorStand && ((ArmorStand) e).getEquipment().getHelmet() != null && ((ArmorStand) e).getEquipment().getHelmet().getType().toString().contains("MUSIC_DISC")){
                        e.remove();
                    }
                }
            }
    }
    
    @EventHandler
    public void onWoodBurn(BlockBurnEvent event){
        double rnd = Math.random();
        String bname = event.getBlock().getType().toString();
        Block b = event.getBlock();
        if(b.getType().toString().contains("CRIMSON_")){
            event.setCancelled(true);
        }
        if(b.getLocation().subtract(0,1,0).getBlock().getType().isSolid() ){
            String data = b.getBlockData().getAsString();
            if(bname.contains("_LOG")){
    
                b.getWorld().sendMessage(Component.text(b.getMetadata("minecraft").toString()));
                event.setCancelled(true);
                b.setType(Material.CRIMSON_STEM);
            } else if(bname.contains("_SLAB")){
                event.setCancelled(true);
                b.setType(Material.CRIMSON_SLAB);
                b.getBlockData().merge(Bukkit.createBlockData(data));
            } else if(bname.contains("_STAIRS")){
                event.setCancelled(true);
                b.setType(Material.CRIMSON_STAIRS);
            } else if(bname.contains("_PLANKS")){
                event.setCancelled(true);
                b.setType(Material.CRIMSON_PLANKS);
            } else if(bname.contains("_DOOR")){
                event.setCancelled(true);
                b.setType(Material.CRIMSON_DOOR);
                b.getBlockData().merge(Bukkit.createBlockData(data));
            } else if(bname.contains("_FENCE") && !bname.contains("FENCE_")){
                event.setCancelled(true);
                b.setType(Material.CRIMSON_FENCE);
            }
        }
    }
    
    
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void placedArmorStandsWithArms(CreatureSpawnEvent event){
        if(event.getEntity() instanceof ArmorStand){
            ((ArmorStand) event.getEntity()).setArms(true);
            if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.BREEDING){
                
                // AS RETARDED AS IT SEEMS, IT'S MY WAY TO PATCH INVISIBLE ARMOR STANDS FROM APPEARING FOR A FRACTION OF SECOND WHEN SPAWNED
                // WORKS IN DUO WITH THE RESOURCE PACK (OPTIFINE)
                
                event.getEntity().setCustomName("Armor Stand");
            }
        }
    }
    
    
    
    
    
    
    
    
    
    @EventHandler
    public void timeSkipperUse(PlayerInteractEvent event){
        if(event.getAction().isRightClick() && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.SHIELD){
            MagicExecutor.timeSkipper(event.getPlayer());
        }
    }
    
    @EventHandler
    public void cancelPlayerPlaceBlock(BlockPlaceEvent event){
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        
        if(!item.getType().isAir() && item.hasItemMeta() && item.getItemMeta().hasLocalizedName()){
            if(
                    item.getItemMeta().getLocalizedName().equals("rope")
            ){
                event.setCancelled(true);
            }
        }
    }
    
    
    
    
    
    
    
    
}
