package com.github.sawors.stones.listeners;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.SerializeInventory;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.database.DataHolder;
import com.github.sawors.stones.items.StoneItem;
import com.github.sawors.stones.items.StonesItems;
import com.github.sawors.stones.magic.MagicExecutor;
import com.github.sawors.stones.recipes.Recipes;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Chain;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class ListenersALL implements Listener {

    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    //ALL PRAISE THE XP ORB REMOVER  \i/ MAY HIS POWER CURE US OF THE DISEASE THAT CONSUME OUR WORLD \i/
    @EventHandler
    public void onXpSpawn(EntityAddToWorldEvent event){
        if(event.getEntity() instanceof ExperienceOrb){
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event){
        Player p = event.getPlayer();
        ItemStack consumed_item = event.getItem();

        if(consumed_item.getType() == Material.COOKED_MUTTON || consumed_item.getType() == Material.COOKED_PORKCHOP || consumed_item.getType() == Material.COOKED_BEEF){
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2, false));
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event){
        if(event.getHitBlock() != null && event.getEntity() instanceof Arrow){
            Arrow arrow = (Arrow) event.getEntity();
            Block block = event.getHitBlock();
            if(block.getType().getBlastResistance() > 1){
                if(Math.random() < block.getType().getBlastResistance()/10){
                    arrow.getWorld().spawnParticle(Particle.ITEM_CRACK, arrow.getLocation(),2,.1,.1,.1,.1,new ItemStack(Material.STICK));
                    arrow.getWorld().spawnParticle(Particle.ITEM_CRACK, arrow.getLocation(),4,.1,.1,.1,.1,new ItemStack(Material.IRON_INGOT));
                    block.getWorld().playSound(block.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, UsefulThings.randomPitchSimple()+2f);
                    arrow.remove();
                } else {
                    block.getWorld().playSound(block.getLocation(), block.getSoundGroup().getPlaceSound(), 1f, UsefulThings.randomPitchSimple()+0.5f);
                }
            } else {
                arrow.getWorld().spawnParticle(Particle.BLOCK_CRACK, arrow.getLocation(),6,.1,.1,.1,.1,block.getBlockData());
                block.getWorld().playSound(block.getLocation(), block.getSoundGroup().getPlaceSound(), 1f, UsefulThings.randomPitchSimple()+0.2f);
            }
            
            // switch for sound



            //arrow break reaction
            if(block.getType().toString().contains("GLASS_PANE")){
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1,1.2f);
                        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getX()+0.5, block.getY()+0.5, block.getZ()+0.5,16, 0, 0,0, block.getBlockData());
                        block.getWorld().spawnParticle(Particle.FLAME, block.getLocation(), 1);
                        block.breakNaturally();
                    }
                }.runTaskLater(Stones.getPlugin(), 1);
            }
        }
    }


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
        
            if(event.getAction().isRightClick() && item.getType().equals(Material.SHIELD) && event.getPlayer().getCooldown(Material.SHIELD) <= 0 && Objects.equals(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING), "instrument")){
                String music = "";
                Sound sound = null;
                String soundstr = "";
                if(locname.contains("_flute")){
                    sound = Sound.BLOCK_NOTE_BLOCK_FLUTE;
                } else if(locname.contains("_lyre")){
                    sound = Sound.BLOCK_NOTE_BLOCK_HARP;
                } else if(locname.contains("_guitar")){
                    sound = Sound.BLOCK_NOTE_BLOCK_GUITAR;
                } else if(locname.contains("_banjo")){
                    sound = Sound.BLOCK_NOTE_BLOCK_BANJO;
                } else if(locname.contains("_doublebass")){
                    soundstr = "minecraft:sawors.instrument.doublebass";
                } else if(locname.contains("_harp")){
                    soundstr = "minecraft:sawors.instrument.harp";
                } else if(locname.contains("_koto")){
                    soundstr = "minecraft:sawors.instrument.koto";
                } else if(locname.contains("_oud")){
                    soundstr = "minecraft:sawors.instrument.oud";
                } else if(locname.contains("_panflute")){
                    soundstr = "minecraft:sawors.instrument.panflute";
                } else if(locname.contains("_sitar")){
                    soundstr = "minecraft:sawors.instrument.sitar";
                } else if(locname.contains("molophone")){
                    soundstr = "minecraft:sawors.instrument.molophone";
                }
                
                
                if(p.getInventory().getItemInOffHand().getType().equals(Material.PAPER) && p.getInventory().getItemInOffHand().hasItemMeta() && p.getInventory().getItemInOffHand().getItemMeta().getLocalizedName().equals("music_parchment") && p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING) != null){
                    music = p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING);
                }
                
                
                if(music != null && music.length() > 0){
                    if(sound != null){
                        UsefulThings.playMusic(music, p, true, 2, sound);
                    } else if(soundstr.length() != 0){
                        UsefulThings.playMusic(music, p, true, 2, soundstr);
                    } else{
                        UsefulThings.playMusic(music, p, true, 2, Sound.ENTITY_VILLAGER_YES);
                    }
                } else {
                    p.sendActionBar(Component.text(ChatColor.RED + "no music selected"));
                    float[] pitch = {(-p.getLocation().getPitch()/180)+1};
                    if(sound != null){
                        UsefulThings.playMusic(pitch, p, true, 2, sound);
                    } else if(soundstr.length() != 0){
                        UsefulThings.playMusic(pitch, p, true, 2, soundstr);
                    } else{
                        UsefulThings.playMusic(pitch, p, true, 2, Sound.ENTITY_VILLAGER_YES);
                    }
                }
                
            }
            
        
            if (locname.contains("dice") && item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING) != null && item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING).equals("dice6")) {
                int roll = ((int) (Math.random() * 6) + 1);
    
                p.sendMessage(ChatColor.YELLOW + "You roll a " + ChatColor.GOLD + "" + ChatColor.BOLD + roll);
            }
        
            
            
            
            
            //WEAR HAT
            if (p.getLocation().getPitch() <= -85 && b == null) {
                    if (Objects.equals(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING), "hat")) {
                        p.getInventory().setItemInMainHand(p.getInventory().getHelmet());
                        p.getInventory().setHelmet(item);
                        p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
                    }
            }
        
        } else if (item.getType() == Material.AIR) {
            //"unwear" hat
            if (p.getInventory().getItemInMainHand().getType() == Material.AIR && p.getInventory().getHelmet() != null && p.getInventory().getHelmet().hasItemMeta() && p.getInventory().getHelmet().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING).equals("hat") && p.isSneaking() && p.getLocation().getPitch() >= 85) {
                if (!p.getInventory().getHelmet().getItemMeta().getLocalizedName().equals("blindfold")) {
                    p.getInventory().setItemInMainHand(p.getInventory().getHelmet());
                    p.getInventory().setHelmet(null);
                    p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1.5F);
                }
        
            }
        }
    }
    
    
    
        @EventHandler
        public void onPlayerLeaveSit(EntityDismountEvent event){
        if(event.getEntity() instanceof Player && event.getDismounted() instanceof ArmorStand){
            Player player = (Player) event.getEntity();
            ArmorStand seat = (ArmorStand) event.getDismounted();
            if(seat.getCustomName() != null && seat.getCustomName().contains("seat")){
                seat.remove();
                player.teleport(player.getLocation().add(0,1,0));
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
                                rightclicked.getInventory().setHelmet(StonesItems.get(StoneItem.BLINDFOLD));
                                
                            }
                    }
                }
            }
        }

        @EventHandler
    public void onPlayerBreakLogEvent(BlockBreakEvent event){
            Player p = event.getPlayer();
            Block b = event.getBlock();








            //just in case the event is triggered by magic or else
            /*if(p.getHealth() != 0){
                if(String.valueOf(b.getType()).contains("LOG")){
                    for(int i = 1; i<128; i++){
                        if(String.valueOf(b.getLocation().add(0.5,i,0.5).getBlock().getType()).contains("LOG")){
                            b.getWorld().spawnFallingBlock(b.getLocation().add(0.5,i,0.5), b.getLocation().add(0.5,i,0.5).getBlock().getBlockData());
                            b.getLocation().add(0.5,i,0.5).getBlock().setType(Material.AIR);
                        } else{
                        break;
                        }
                    }
                }
            }*/

    }


    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event){
        if(event.getEntity().getItemStack().getType() == Material.TORCH && Math.random() < 0.5){
            event.getEntity().getLocation().getBlock().setType(Material.FIRE);
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event){
        if(event.getEntity().getItemStack().getType() == Material.TORCH){
           event.getEntity().setTicksLived(4800);
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

//    @EventHandler
//    public void onPlayerSneak(PlayerToggleSneakEvent event){
//            Player p = event.getPlayer();
//            if(event.isSneaking()){
//                p.sendMessage("sneak");
//            } else{
//                p.sendMessage("unsneak");
//            }
//    }

    @EventHandler
    public void onPlayerOpenDoor(PlayerInteractEvent event){
            Block b = event.getClickedBlock();
            if(b != null && b.getType().toString().contains("_DOOR") && event.getAction().isRightClick()){
                //event.setCancelled(true);
                Door door = (Door) b.getBlockData().clone();



                //DOUBLE DOOR LOGIC
                if(!event.useInteractedBlock().equals(Event.Result.DENY) && !event.getPlayer().isSneaking()) {
                    Block b1;
                    Block b2;
                    if (door.getFacing().equals(BlockFace.NORTH) || door.getFacing().equals(BlockFace.SOUTH)) {
                        b1 = b.getLocation().add(1, 0, 0).getBlock();
                        b2 = b.getLocation().add(-1, 0, 0).getBlock();


                    } else {
                        b1 = b.getLocation().add(0, 0, 1).getBlock();
                        b2 = b.getLocation().add(0, 0, -1).getBlock();
                    }
                    if (b1.getType().toString().contains("_DOOR") && ((Door) b1.getBlockData()).getHinge() != door.getHinge()) {
                        Door d1 = (Door) b1.getBlockData().clone();
                        if (door.isOpen()) {
                            d1.setOpen(false);
                            b1.getWorld().playSound(b1.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);
                        } else {
                            d1.setOpen(true);
                            b1.getWorld().playSound(b1.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_DOOR_OPEN, 1, 1);
                        }
                        b1.setBlockData(d1);
                        b1.getState().update();

                    }
                    if (b2.getType().toString().contains("_DOOR") && ((Door) b2.getBlockData()).getHinge() != door.getHinge()) {
                        Door d2 = (Door) b2.getBlockData().clone();
                        if (door.isOpen()) {
                            d2.setOpen(false);
                            b2.getWorld().playSound(b1.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);
                        } else {
                            d2.setOpen(true);
                            b2.getWorld().playSound(b1.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_DOOR_OPEN, 1, 1);
                        }
                        b2.setBlockData(d2);
                        b2.getState().update();
                    }
                }
            }
        if(b != null && b.getType().toString().contains("_DOOR") && event.getAction().isLeftClick() && event.getPlayer().getInventory().getItemInMainHand().getType().isAir()){
            if(event.getPlayer().isSneaking()){
                b.getWorld().playSound(b.getLocation().add(.5,0,.5), "minecraft:sawors.door.knock", .25f, UsefulThings.randomPitchSimple()-0.5f);
            }else{
                b.getWorld().playSound(b.getLocation().add(.5,0,.5), "minecraft:sawors.door.knock", 1, UsefulThings.randomPitchSimple());
            }
        }
    }

    @EventHandler
    public void chainClimber(PlayerInteractEvent event){
            Player p = event.getPlayer();

            if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHAIN && p.getLocation().add(0,1,0).getBlock().getType().equals(Material.CHAIN) && ((Chain) p.getLocation().add(0,1,0).getBlock().getBlockData()).getAxis().equals(Axis.Y)){
                if(p.isSneaking()){
                    p.setVelocity(new Vector(0,.25,0));
                    p.getWorld().playSound(p.getLocation().add(0,1,0), Sound.BLOCK_CHAIN_STEP, .25f, 1);
                }else{
                    p.setVelocity(new Vector(0,.33,0));
                    p.getWorld().playSound(p.getLocation().add(0,1,0), Sound.BLOCK_CHAIN_STEP, 1, 1);
                }
            }
    }

    @EventHandler
    public void playerOpenBackpack(PlayerInteractEvent event){
        Player p = event.getPlayer();

        if(p.getInventory().getItemInMainHand().getType().equals(Material.RAW_IRON) && p.isSneaking()){
            ItemStack mainhand = event.getPlayer().getInventory().getItemInMainHand();
            Inventory inv = Bukkit.createInventory(event.getPlayer(), 45, Component.text("Backpack"));
            for(int i = 0; i<45; i++){
                inv.setItem(i, new ItemStack(Material.OAK_BOAT));
            }
            String s = mainhand.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING);
            p.sendMessage(s);
            //if(mainhand.hasItemMeta() && mainhand.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING) != null){
                try{

                    ItemStack[] itemlist = SerializeInventory.itemStackArrayFromBase64(s);
                    for(int i = 0; i<45; i++){
                        inv.setItem(i, itemlist[i]);
                    }
                }catch(IOException exception){
                    Bukkit.getLogger().log(Level.WARNING, "backpack from player " + event.getPlayer().getName() + " failed to open (no data found on item or IOException)");
                }

            //}
            event.getPlayer().openInventory(inv);
        }

    }

    @EventHandler
    public void playerCloseBackpack(InventoryCloseEvent event){
            if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.RAW_IRON)){
                ItemStack mainhand = event.getPlayer().getInventory().getItemInMainHand();
                ItemStack[] itemlist = event.getInventory().getContents();

                for(int i = 0; i<itemlist.length; i++){
                    event.getPlayer().sendMessage("i: "+i);
                   if(itemlist[i] == null){
                       itemlist[i] = new ItemStack(Material.STONE);
                   }
                }


//                event.getPlayer().sendMessage(Arrays.toString(itemlist));
                String s = SerializeInventory.itemStackArrayToBase64(itemlist);
                event.getPlayer().sendMessage(s);
                mainhand.getItemMeta().getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, s);

                //event.getPlayer().sendMessage(SerializeInventory.itemStackArrayToBase64(event.getInventory().getContents()));
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
    
    
    @EventHandler
    public void onPlayerCutPlant(PlayerInteractEvent event){
        if(event.getClickedBlock() != null && !event.getClickedBlock().getType().isAir()){
            Block b = event.getClickedBlock();
            Player p = event.getPlayer();
            ItemStack item = p.getInventory().getItemInMainHand();
            byte globaltimer = 5;
            
            if(UsefulThings.isShortPlant(b) && event.getAction().isLeftClick()){
                for(Entity e : b.getLocation().add(0.5,1,0.5).getNearbyEntities(1,1,1)){
                    if(e.getType().equals(EntityType.ARMOR_STAND) && e.getCustomName() != null && (e.getCustomName().toLowerCase().contains("shovel") || e.getCustomName().toLowerCase().contains("sickle"))){
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            
            
            if(UsefulThings.isFlower(b.getLocation().add(0,1,0).getBlock()) && b.isSolid() && (b.getType().toString().contains("DIRT") || b.getType().toString().contains("GRASS_") || b.getType().toString().contains("PODZOL")) && event.getAction().isRightClick() && item.getType().toString().contains("SHOVEL")){
                Location mid = b.getLocation();
                mid.setX(b.getLocation().add(0,1,0).getBlock().getBoundingBox().getCenter().getX());
                mid.setY(b.getLocation().add(0,1,0).getBlock().getBoundingBox().getCenter().getY() +13/16f);
                mid.setZ(b.getLocation().add(0,1,0).getBlock().getBoundingBox().getCenter().getZ());
                for(Entity e : mid.getNearbyEntities(.5,.5,.5)){
                    if(e.getType().equals(EntityType.ARMOR_STAND)){
                        return;
                    }
                }
                event.setCancelled(true);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) ((globaltimer-1)/0.2), 2, false, false));
                ArmorStand stand2 = UsefulThings.createDisplay(mid.add((p.getLocation().getX() - b.getBoundingBox().getCenter().getX())/10,-2/16f,(p.getLocation().getZ()-b.getBoundingBox().getCenter().getZ())/10), item.clone(), true);
                stand2.setSmall(true);
                stand2.setCustomName("_shovel");
                stand2.setHeadPose(new EulerAngle(0, Math.toRadians(p.getLocation().getYaw()+180), 0));
                new BukkitRunnable(){
                    byte timer = (byte) ((globaltimer-1)/2);
        
        
                    @Override
                    public void run(){
                        if(timer <= -1){
                            stand2.remove();
                            this.cancel();
                            return;
                        }
                        stand2.setHeadPose(new EulerAngle(stand2.getHeadPose().getX()+Math.toRadians(10), stand2.getHeadPose().getY(), 0));
                        b.getWorld().spawnParticle(Particle.BLOCK_DUST, mid, 8,0,0,0, 0, b.getBlockData());
                        b.getWorld().playSound(mid, Sound.BLOCK_GRAVEL_BREAK, .5f, 1 + 1f/(4*timer));
                        if(timer == 0){
                            Material baseb = b.getType();
                            b.setType(Material.DIRT);
                            b.getWorld().spawnParticle(Particle.BLOCK_DUST, mid, 16,0,0,0, 0, b.getBlockData());
                            b.getWorld().playSound(mid, Sound.BLOCK_GRAVEL_BREAK, .5f, 1.25f);
                            b.setType(baseb);
                            b.getLocation().add(0,1,0).getBlock().breakNaturally();
                            if(b.getType().toString().contains("GRASS_")){
                                b.setType(Material.DIRT);
                            }
                            
                        }
                        timer --;
                    }
                }.runTaskTimer(Stones.getPlugin(), 1, 10);
            }
            
            
            
            
            if(UsefulThings.isShortPlant(b) && item.hasItemMeta() && item.getItemMeta().getLocalizedName().contains("sickle")){
    
                Location mid = b.getLocation();
                mid.setX(b.getBoundingBox().getCenter().getX());
                mid.setY(b.getBoundingBox().getCenter().getY() +13/16f);
                mid.setZ(b.getBoundingBox().getCenter().getZ());
                
                
                    for(Entity e : mid.getNearbyEntities(.5,.5,.5)){
                        if(e.getType().equals(EntityType.ARMOR_STAND)){
                            return;
                        }
                    }
                if(event.getAction().isRightClick()){
                    
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, globaltimer+5, 2, false, false));
    
                    final ArmorStand stand = UsefulThings.createDisplay(mid.add((p.getLocation().getX() - b.getBoundingBox().getCenter().getX())/10,0,(p.getLocation().getZ()-b.getBoundingBox().getCenter().getZ())/10), item.clone(), true);
                    stand.setSmall(true);
                    stand.setCustomName("_sickle");
                    Location standloc = stand.getLocation();
                    stand.setHeadPose(new EulerAngle(0, Math.toRadians(p.getLocation().getYaw()+225), 0));
    
                    new BukkitRunnable(){
                        byte timer = globaltimer;
        
        
                        @Override
                        public void run(){
                            if(timer <= -5){
                                stand.remove();
                                this.cancel();
                                return;
                            }
                            if(timer > 0){
                                standloc.setYaw(standloc.getYaw()-125f/globaltimer);
                                stand.teleport(standloc);
                            }
                            if(timer == 0){
                                Material baseb = b.getType();
                                b.setType(Material.GRASS);
                                b.getWorld().spawnParticle(Particle.BLOCK_DUST, mid, 8,0.2,0.2,0.2, 0, b.getBlockData());
                                b.getWorld().playSound(mid, Sound.ENTITY_SNOW_GOLEM_SHEAR, .25f, 1f);
                                b.getWorld().playSound(mid, Sound.BLOCK_GRASS_BREAK, .5f, 1.5f);
                                b.setType(baseb);
                                if(!UsefulThings.plantToItem(b).getType().isAir()){
                                    b.getWorld().dropItem(mid, Recipes.sickleCut(b));
                                }
                                b.setType(Material.AIR);
                            }
                            timer --;
                        }
                    }.runTaskTimer(Stones.getPlugin(), 1, 1);
                } else {
                    event.setCancelled(true);
                    b.setType(Material.GRASS);
                    b.getWorld().spawnParticle(Particle.BLOCK_DUST, mid, 8,0.2,0.2,0.2, 0, b.getBlockData());
                    b.getWorld().dropItem(mid, StonesItems.get(StoneItem.THATCH));
                    b.setType(Material.AIR);
                    
                    
                    
                    
                }
    
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void placedArmorStandsWithArms(CreatureSpawnEvent event){
        if(event.getEntity() instanceof ArmorStand){
            ((ArmorStand) event.getEntity()).setArms(true);
            if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.BREEDING){
                
                // AS RETARDED AS IT SEEMS, IT IS MY WAY TO PATCH INVISIBLE ARMOR STANDS FROM APPEARING FOR A FRACTION OF SECOND WHEN SPAWNED
                // WORKS IN DUO WITH THE RESOURCE PACK (OPTIFINE)
                
                event.getEntity().setCustomName("Armor Stand");
            }
        }
    }
    
    @EventHandler
    public void playerSit(PlayerInteractEvent event){
        if(event.getClickedBlock() != null && !event.getPlayer().isSneaking() && event.getAction().isRightClick() && ((event.getClickedBlock().getType().toString().contains("STAIRS") && ((Stairs) event.getClickedBlock().getBlockData()).getShape().equals(Stairs.Shape.STRAIGHT) && ((Stairs) event.getClickedBlock().getBlockData()).getHalf().equals(Bisected.Half.BOTTOM)) || (event.getClickedBlock().getType().toString().contains("SLAB") && ((Slab) event.getClickedBlock().getBlockData()).getType().equals(Slab.Type.BOTTOM))) && event.getPlayer().getInventory().getItemInMainHand().getType().isAir()){
            Block b = event.getClickedBlock();
            Location loc = b.getLocation().add(0.5,0.5,0.5);
            Player p = event.getPlayer();
            if(
                    loc.clone().add(1,0,0).getBlock().getType().toString().contains("TRAPDOOR") || loc.clone().add(1,0,0).getBlock().getType().toString().contains("SIGN") ||
                    loc.clone().add(-1,0,0).getBlock().getType().toString().contains("TRAPDOOR") || loc.clone().add(1,0,0).getBlock().getType().toString().contains("SIGN") ||
                    loc.clone().add(0,0,1).getBlock().getType().toString().contains("TRAPDOOR") || loc.clone().add(1,0,0).getBlock().getType().toString().contains("SIGN") ||
                    loc.clone().add(0,0,-1).getBlock().getType().toString().contains("TRAPDOOR") || loc.clone().add(1,0,0).getBlock().getType().toString().contains("SIGN")
            ){
                loc.setYaw(p.getLocation().getYaw()+180);
                UsefulThings.sitEntity(p, loc);
            }
            
            
        }
    }
    
    @EventHandler
    public void playerUsesCrayon(PlayerInteractEvent event){
        if(event.getAction().isRightClick() && Objects.equals(event.getPlayer().getInventory().getItemInMainHand().getType(), Material.STICK) && event.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName().contains("crayon")){
            Block b = event.getClickedBlock();
            Player p = event.getPlayer();
            if(b != null){
                if(b.getType().toString().contains("SIGN")){
                    org.bukkit.block.Sign sign = ((org.bukkit.block.Sign) b.getState());
                    p.openSign(sign);
                }
            }
        }
    }
    
    @EventHandler
    public void playerExitSign(SignChangeEvent event){
        Location loc = event.getBlock().getLocation().add(0.5,0.5,0.5);
        loc.getWorld().playSound(loc, Sound.ENTITY_VILLAGER_WORK_CARTOGRAPHER, .5f, 1);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void disableRespawnAnchors(PlayerInteractEvent event){
        if(event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.RESPAWN_ANCHOR) && event.getAction().isRightClick() && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GLOWSTONE)){
            event.setCancelled(true);
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
    
    @EventHandler
    public void onFireEnd(BlockFadeEvent event){
    
    }
    
    @EventHandler
    public void setCompassNorth(PlayerChangedWorldEvent event){
        new BukkitRunnable(){
            @Override
            public void run() {
                Player p = event.getPlayer();
                p.setCompassTarget(new Location(p.getWorld(), 0,0,-1000000));
                p.updateInventory();
            }
        }.runTask(Stones.getPlugin());
    
    }
    @EventHandler
    public void setCompassNorth(PlayerJoinEvent event){
        new BukkitRunnable(){
            @Override
            public void run() {
                Player p = event.getPlayer();
                p.setCompassTarget(new Location(p.getWorld(), 0,0,-1000000));
                p.updateInventory();
            }
        }.runTask(Stones.getPlugin());
    }
    
    @EventHandler
    public void sprintBuff(PlayerToggleSprintEvent event){
        if(event.isSprinting()){
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000,1,false,false));
        } else {
            if(event.getPlayer().hasPotionEffect(PotionEffectType.SPEED)){
                event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }
    
    
    
    
    
    
}
