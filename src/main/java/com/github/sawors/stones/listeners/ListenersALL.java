package com.github.sawors.stones.listeners;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.github.sawors.stones.SerializeInventory;
import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.DataHolder;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Chain;
import org.bukkit.block.data.type.Door;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
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
import java.util.*;
import java.util.logging.Level;

public class ListenersALL implements Listener {

    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

  /*  @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player p = event.getPlayer();

        Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);

        if(b.getType() == Material.GRASS_BLOCK){
            World w = p.getWorld();

            w.createExplosion(p.getLocation(), 5);
        }
    }*/

    /*@EventHandler
    public void onMobSpawn(CreatureSpawnEvent event){
        LivingEntity entity = event.getEntity();
        World w = event.getEntity().getWorld();
        w.spawnParticle(Particle.FLAME, event.getLocation(), 10);

        if(entity instanceof Cow){
            entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 400,1));
        }
    }*/





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
            block.getWorld().playSound(block.getLocation(), block.getSoundGroup().getPlaceSound(), 1f, UsefulThings.randomPitchSimple()+0.2f);
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
    public void onPlayerInteract(PlayerInteractEvent event){
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        Block b = event.getClickedBlock();
        /*
        if(item != null && item.getType() == Material.RAW_GOLD && b != null){
            Location b2 = b.getLocation().clone();
            //p.sendMessage("");
                for (float i = 0; i <= 0.9; i += 0.05) {
                    b2.getWorld().spawnParticle(Particle.SMALL_FLAME, b2.clone().add(0.5, ((-3 * Math.pow(i - 0.5, 2)) + 0.75) + 1, i+0.5), 1, 0, 0, 0, 0);
                    //p.sendMessage("(" + (Math.round(bLoca.getZ() * 1000)/1000) + ";" + (Math.round(bLoca.getY() * 1000)/1000) + ")");
                }
        }*/

        if(item != null && item.hasItemMeta() && item.getItemMeta().hasLocalizedName()) {


   //add Closed Seal item
           if (item.getItemMeta().getLocalizedName().equals("blankparchment") && p.isSneaking()) {
              item.setAmount(item.getAmount()-1);
              ItemStack newitem = new ItemStack(Material.PAPER);
              ItemMeta meta = newitem.getItemMeta();
              meta.displayName(Component.text(ChatColor.DARK_PURPLE + "Signed Parchment"));
              meta.setLocalizedName("signedparchment");
              ArrayList<Component> lore = new ArrayList<>();
              lore.add(Component.text(""));
              lore.add(Component.text(ChatColor.GOLD + "" + "This is signed in the name of :"));
              if(p.getCustomName() != null) {
                  lore.add(Component.text(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + p.getCustomName()));
              } else{
                  lore.add(Component.text(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + p.getName()));
              }
              meta.lore(lore);
              meta.setUnbreakable(true);
              meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
              newitem.setItemMeta(meta);
              p.getInventory().addItem(newitem);
              p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_PLACE, 2, 1);
              p.getWorld().playSound(p.getLocation(), Sound.ENTITY_COD_FLOP, 1, 1);
              p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, p.getLocation(), 12);
           }


   //ACTIVATE RESONANT CRYSTAL
           if(Objects.equals(item.getItemMeta().getLocalizedName(), "resonantcrystal") && p.isSneaking()){
               p.playSound(p.getLocation(), "minecraft:sawors.resonantcrystal_on", 1, (float) (Math.sin(new Random().nextFloat()) / 4 + 0.9));

           }

            if(Objects.equals(item.getItemMeta().getLocalizedName(), "raid_horn") && event.getAction().isRightClick() && event.getPlayer().getCooldown(Material.SHIELD) <= 0){
                //Location soundloc = p.getLocation();
                net.kyori.adventure.sound.Sound sound = net.kyori.adventure.sound.Sound.sound(Sound.EVENT_RAID_HORN, net.kyori.adventure.sound.Sound.Source.PLAYER, UsefulThings.getVolume(24), 1);

                p.getWorld().playSound(sound, net.kyori.adventure.sound.Sound.Emitter.self());
                new BukkitRunnable(){

                    final int max = 8;
                    int countdown = max;

                    @Override
                    public void run(){
                        
                        if(countdown <= 0 || Bukkit.getOnlinePlayers().isEmpty()){
                            if(!p.getWorld().getNearbyPlayers(p.getLocation(), 24).isEmpty()){
                                for (Player player : p.getWorld().getNearbyPlayers(p.getLocation(), 24)){
                                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 2, UsefulThings.randomPitchSimple(0.4, 1));
                                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 2, UsefulThings.randomPitchSimple(0.4, 1));
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (((-p.getLocation().distance(player.getLocation())/24/1.25)+1)*30*20), 0, false, false));
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (((-p.getLocation().distance(player.getLocation())/24/1.25)+1)*30*20), 0, false, false));
                                    player.setCooldown(Material.SHIELD, 20*8);
                                }
                            }
                            this.cancel();
                            return;
                        }
                        if(countdown == max){
                            if(event.hasItem() && event.getItem().hasItemMeta() && Objects.equals(event.getItem().getItemMeta().getLocalizedName(), "raid_horn")){
                                p.spawnParticle(Particle.REDSTONE, p.getLocation().add(0,3.25+((float) -countdown/max),0), 4, 0.1, .25,0.1, new Particle.DustOptions(Color.fromRGB(0x45171f), 1));
                            } else {
                                p.getWorld().stopSound(sound);
                                this.cancel();
                                return;
                            }
                        } else {
                            if(p.isBlocking() && event.hasItem() && event.getItem().hasItemMeta() && Objects.equals(event.getItem().getItemMeta().getLocalizedName(), "raid_horn")){
                                p.spawnParticle(Particle.REDSTONE, p.getLocation().add(0,3.25+((float) -countdown/max),0),4, 0.1, .25,0.1, new Particle.DustOptions(Color.fromRGB(0x45171f), 1));
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


   //ROLL D6
            if(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING) != null && item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING).equals("dice6")){
                int roll = ((int) (Math.random()*6)+1);

                p.sendMessage(ChatColor.YELLOW + "You roll a " + ChatColor.GOLD + "" +ChatColor.BOLD + roll);


                /*int a1 = 0;
                int a2 = 0;
                int a3 = 0;
                int a4 = 0;
                int a5 = 0;
                int a6 = 0;
                int astrange = 0;
                int tests = 100000;


                for(int i = 0; i<=tests; i++){
                    int a = (int) (Math.random()*6)+1;
                    switch(a){
                        case 1:
                            a1++;
                            break;
                        case 2:
                            a2++;
                            break;
                        case 3:
                            a3++;
                            break;
                        case 4:
                            a4++;
                            break;
                        case 5:
                            a5++;
                            break;
                        case 6:
                            a6++;
                            break;
                        default:
                            astrange++;
                            break;

                    }
                }
                int atot = a1 + a2 + a3 + a4 + a5 + a6;
                p.sendMessage("\n" + "1 : " + ((a1*100)/atot) + "%" +"\n" + "2 : " + ((a2*100)/atot) + "%" + "\n" + "3 : " + ((a3*100)/atot) + "%"  +"\n" + "4 : " + ((a4*100)/atot) + "%"  +"\n" + "5 : " + ((a5*100)/atot) + "%"  +"\n" + "6 : " + ((a6*100)/atot) + "%" +"\n" + "target : " + ((1F/6)*100) + "%" +"\n" + "out of bounds : " + astrange +"\n" + "tests : " + tests);
                   */
            }


   //WEAR HAT
           if(p.isSneaking() && p.getLocation().getPitch() <= -85)
           {
               if(Objects.equals(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING), "hat"))
               {
                   p.getInventory().setItemInMainHand(p.getInventory().getHelmet());
                   p.getInventory().setHelmet(item);
                   p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
               }


           }





            } else if (item == null) {
            //NEVER USE ITEM HERE

            //"unwear" hat
            if (p.getInventory().getItemInMainHand().getType() == Material.AIR && p.getInventory().getHelmet() != null &&  p.getInventory().getHelmet().hasItemMeta() && p.getInventory().getHelmet().getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING).equals("hat") && p.isSneaking() && p.getLocation().getPitch() >= 85) {
                if(!p.getInventory().getHelmet().getItemMeta().getLocalizedName().equals("blindfold")){
                    p.getInventory().setItemInMainHand(p.getInventory().getHelmet());
                    p.getInventory().setHelmet(null);
                    p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1.5F);
                }

                      }//UNWEAR HAT




                }// ITEM == null
        if(event.getClickedBlock() != null) // CHECK FOR BLOCK REGARDLESS OF THE ITEM
        {
            if(UsefulThings.isEternalFire(event.getClickedBlock())){
                p.sendMessage(ChatColor.DARK_PURPLE + "yes it is");
            }
        }

        }// INTERACT EVENT !!
        /*@EventHandler
        public void onHandSwitch(PlayerInteractEvent event) throws IOException {
            Player p = event.getPlayer();
            ItemStack itemmain = event.getItem();
            ItemStack itemoff = p.getInventory().getItemInOffHand();



            if(itemmain != null && itemmain.hasItemMeta() && Objects.equals(itemmain.getItemMeta().getPersistentDataContainer().get(SgiveCommandExecutor.getKey("type"), PersistentDataType.STRING), "instrument")){
                ItemMeta itemmainmeta = itemmain.getItemMeta().clone();
                ItemStack[] hotbarsave = {p.getInventory().getItem(0), p.getInventory().getItem(1), p.getInventory().getItem(2), p.getInventory().getItem(3), p.getInventory().getItem(4), p.getInventory().getItem(5), p.getInventory().getItem(6), p.getInventory().getItem(7), p.getInventory().getItem(8)};
                String ss = SerializeInventory.itemStackArrayToBase64(hotbarsave);

                if(Objects.equals(itemmain.getItemMeta().getPersistentDataContainer().get(SgiveCommandExecutor.getKey("ison"), PersistentDataType.INTEGER), 0)) {

                        //ItemStack[] neo = SerializeInventory.itemStackArrayFromBase64(ss);
                        itemmainmeta.getPersistentDataContainer().set(SgiveCommandExecutor.getKey("inv"), PersistentDataType.STRING, ss);
                        itemmainmeta.getPersistentDataContainer().set(SgiveCommandExecutor.getKey("ison"), PersistentDataType.INTEGER, 1);
                        itemmain.setItemMeta(itemmainmeta);
                        for (int i = 0; i <= 7; i++) {
                            p.getInventory().addItem(new ItemStack(Material.NOTE_BLOCK));
                        }
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                        p.sendMessage("case goodswitch");
                    } else if (Objects.equals(itemmain.getItemMeta().getPersistentDataContainer().get(SgiveCommandExecutor.getKey("ison"), PersistentDataType.INTEGER), 1)){
                    p.sendMessage("it is effectively on");
                }


            }
        }*/

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
                                ItemStack itemtemp = new ItemStack(Material.PAPER);
                                ItemMeta meta = itemtemp.getItemMeta();
                                ArrayList<Component> lore = new ArrayList<>();
                                meta.displayName(Component.text(ChatColor.GRAY + "Blindfold"));
                                meta.setLocalizedName("blindfold");
                                lore.add(Component.text(""));
                                lore.add(Component.text(ChatColor.GREEN + "Right-click at someone or wear it to blind the wearer"));
                                meta.lore(lore);
                                meta.setUnbreakable(true);
                                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat"); // if item is a wearable. Possible wearables : head, chest, legs, feet, ring
                                itemtemp.setItemMeta(meta);
                                rightclicked.getInventory().setHelmet(itemtemp);
                            }
                    }
                }
            } else if(event.getRightClicked() instanceof Bee && player.getInventory().getItemInMainHand().getType() == Material.GLASS_BOTTLE && event.getRightClicked().getPersistentDataContainer().get(DataHolder.getSpecialEntity(), PersistentDataType.STRING) == null){
                Bee bee = (Bee) event.getRightClicked();
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
                player.playSound(player.getLocation(), Sound.ITEM_BOTTLE_FILL, 1 ,1.25f);
                player.getInventory().addItem(UsefulThings.getItem("bee_bottle"));

                bee.remove();

            } else if(event.getRightClicked() instanceof Bat && player.getInventory().getItemInMainHand().getType() == Material.GLASS_BOTTLE && event.getRightClicked().getPersistentDataContainer().get(DataHolder.getSpecialEntity(), PersistentDataType.STRING) == null){
                Bat bat = (Bat) event.getRightClicked();
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
                player.playSound(player.getLocation(), Sound.ITEM_BOTTLE_FILL, 1 ,1.25f);
                player.getInventory().addItem(UsefulThings.getItem("bat_bottle"));

                bat.remove();
            } else if(event.getRightClicked() instanceof Bat && player.getInventory().getItemInMainHand().getType() == Material.GLASS_BOTTLE && Objects.equals(event.getRightClicked().getPersistentDataContainer().get(DataHolder.getSpecialEntity(), PersistentDataType.STRING), "firefly")){
                Bat bat = (Bat) event.getRightClicked();
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
                player.playSound(player.getLocation(), Sound.ITEM_BOTTLE_FILL, 1 ,1.25f);
                player.getInventory().addItem(UsefulThings.getItem("firefly_bottle"));

                bat.remove();
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


            if(event.getAction().isRightClick() && Objects.equals(event.getHand(), EquipmentSlot.HAND) && !event.getPlayer().getInventory().getItemInMainHand().getType().isAir() && event.getClickedBlock() != null && event.getClickedBlock().isSolid() && event.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&  event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("wall_bomb")){

                Player p = event.getPlayer();
                Block b = event.getClickedBlock();
                Location face = b.getLocation().clone().add(0.5,0.75,0.5);
                BlockFace blockface = event.getBlockFace();

                if(blockface.equals(BlockFace.NORTH)){
                    face = face.add(0,0,-0.51);
                    p.sendMessage("NORTH");
                } else if (blockface.equals(BlockFace.SOUTH)){
                    face = face.add(0,0,0.51);
                    p.sendMessage("SOUTH");
                }else if(blockface.equals(BlockFace.EAST)){
                    face = face.add(0.51,0,0);
                    p.sendMessage("EAST");
                }else if(blockface.equals(BlockFace.WEST)){
                    face = face.add(-0.51,0,0);
                    p.sendMessage("WEST");
                }else if(blockface.equals(BlockFace.UP)){
                    face = face.add(0,0.61,0);
                    p.sendMessage("UP");
                }else if(blockface.equals(BlockFace.DOWN)){
                    face = face.add(0,-0.41,0);
                    p.sendMessage("DOWN");
                }

                ItemStack bomb = event.getPlayer().getInventory().getItemInMainHand().clone();
                bomb.setAmount(1);
                ArmorStand stand = UsefulThings.createDisplay(face, bomb);

                stand.setSmall(true);
                stand.setCustomName("_hide");
                stand.setCustomNameVisible(false);

                if(blockface.equals(BlockFace.NORTH)){
                    stand.setHeadPose(new EulerAngle(0,3.14,0));
                } else if (blockface.equals(BlockFace.SOUTH)){
                    stand.setHeadPose(new EulerAngle(0,0,0));
                }else if(blockface.equals(BlockFace.EAST)){
                    stand.setHeadPose(new EulerAngle(0,-3.14/2,0));
                }else if(blockface.equals(BlockFace.WEST)){
                    stand.setHeadPose(new EulerAngle(0,3.14/2,0));
                }else if(blockface.equals(BlockFace.UP)){
                    stand.setHeadPose(new EulerAngle(-3.14/2,0,0));;
                }else if(blockface.equals(BlockFace.DOWN)){
                    stand.setHeadPose(new EulerAngle(3.14/2,0,0));
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
                if(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY) != null && Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY)).length >=2){
                    int fuse = Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY))[1];
                    int charge = Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY))[0];
                    int[] data = {charge, fuse};
                    List<Component> lore = item.getItemMeta().lore();

                    if(playerhand.getType() == Material.GUNPOWDER && item.lore().size() != 0){
                        event.getPlayer().sendMessage("Charge : "+data[0]);
                        if(charge >=0 && charge <3){
                            data[0] = data[0] + 1;
                            event.getPlayer().sendMessage(Arrays.toString(data));
                            try{
                                item.getItemMeta().getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY, data);
                            } catch(Exception e){
                                event.getPlayer().sendMessage("fail");
                            }

                            event.getPlayer().sendMessage("Charge : "+Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY))[0]);
                            lore.set(1, Component.text(ChatColor.GRAY + "Charge + " + data[0]));
                            item.lore(lore);
                        }

                    } else if(playerhand.getType() == Material.STRING && item.lore().size() != 0){
                        event.getPlayer().sendMessage("Fuse : "+data[1]);
                        if(data[1] >=0 && data[1] <= 160){
                            data[1] = data[1]+10;
                            event.getPlayer().sendMessage("Fuse : "+data[1]);
                            item.getItemMeta().getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY, data);
                            lore.set(2, Component.text(ChatColor.GRAY + "Fuse + " + (data[1]/20) + "s"));
                            item.lore(lore);
                        }

                    } else if(item.lore().size() != 0 && (playerhand.getType() == Material.FLINT_AND_STEEL || (playerhand.hasItemMeta() && playerhand.getItemMeta().getEnchants().containsKey(Enchantment.FIRE_ASPECT)))){
                        stand.getWorld().playSound(stand.getLocation().clone().add(0,0.5,0), Sound.ITEM_FLINTANDSTEEL_USE, 1,1);
                        ItemMeta meta = item.getItemMeta().clone();
                        ItemStack newbomb = item.clone();
                        meta.setLocalizedName("wall_bomb_ignited");
                        newbomb.setItemMeta(meta);
                        ((ArmorStand) event.getRightClicked()).getEquipment().setHelmet(newbomb);
                        event.getPlayer().sendMessage(((ArmorStand) event.getRightClicked()).getEquipment().getHelmet().getItemMeta().getLocalizedName());
                        stand.getWorld().playSound(stand.getLocation().clone().add(0,0.5,0), "minecraft:sawors.explosive.fusestart", 1,0.75f);
                        new BukkitRunnable(){
                            int timer = data[1];
                            Location moveloc = stand.getLocation().clone().add(0,1+(3/16f)+(2/16f),0);

                            @Override
                            public void run(){
                                if(timer == 0){

                                    stand.getWorld().createExplosion(stand.getLocation().add(0,1,0), data[0]*2);
                                    new BukkitRunnable(){
                                        @Override
                                        public void run(){
                                            stand.getWorld().spawnParticle(Particle.SMOKE_LARGE, stand.getLocation().add(0,.5,0), 32, data[0]*.5, data[0]*.5, data[0]*.5, 0);
                                        }

                                    }.runTaskLater(Stones.getPlugin(), 10);
                                    stand.remove();
                                    this.cancel();
                                    return;
                                }
                                Particle part;

                                if(stand.getLocation().add(0,0.8,0).getBlock().getType().equals(Material.WATER)){
                                    part = Particle.WATER_BUBBLE;
                                } else {
                                    part = Particle.FLAME;
                                }

                                moveloc=moveloc.subtract(0,(3/16f)/data[1],0);

                                for(int i = 0; i<4; i++){
                                    stand.getWorld().spawnParticle(Particle.SMOKE_NORMAL, moveloc, 0, (Math.random()-.5)/20, .1, (Math.random()-.5)/20, 2);
                                    stand.getWorld().spawnParticle(part, moveloc, 0, (Math.random()-.5)/20, .1, (Math.random()-.5)/20, 2);
                                }

                                if(timer >= 10){

                                    float pitch;

                                    if(stand.getLocation().add(0,0.8,0).getBlock().getType().equals(Material.WATER)){
                                        pitch = .5f;
                                    } else {
                                        pitch = .8f;
                                    }
                                    float p2 = (((pitch*(data[1] - timer))/data[1])/2)+.75f;
                                    stand.getWorld().playSound(stand.getLocation().clone().add(0,0.5,0), "minecraft:sawors.explosive.fuse", .2f, p2);

                                }



                                timer--;
                            }

                        }.runTaskTimer(Stones.getPlugin(), 1,1);
                    }
                }



            }
    }

    @EventHandler
    public void onPlayerDestroyBomb(EntityDamageEvent event){
            if(event.getEntity() instanceof ArmorStand){
                event.getEntity().getWorld().sendMessage(Component.text("triggeredv1"));

                ArmorStand armorstand = (ArmorStand) event.getEntity();

                if(armorstand.getEquipment().getHelmet() != null && armorstand.getEquipment().getHelmet().hasItemMeta() && armorstand.getEquipment().getHelmet().getItemMeta().getLocalizedName().equals("wall_bomb")){
                    event.getEntity().getWorld().sendMessage(Component.text("      sexe+"));
                    event.getEntity().getWorld().sendMessage(Component.text(armorstand.getEquipment().getHelmet().getItemMeta().getLocalizedName()));

                    event.getEntity().getWorld().sendMessage(Component.text(" triggeredv2"));
                    event.setCancelled(true);
                    event.getEntity().getWorld().sendMessage(Component.text("  triggeredv3"));
                    ItemStack bomb = armorstand.getEquipment().getHelmet().clone();


                    armorstand.getWorld().dropItem(armorstand.getLocation(), bomb);
                    armorstand.remove();
                }
            }

    }




    }
