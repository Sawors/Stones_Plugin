package com.github.sawors.stones.listeners;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.DataHolder;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

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
        Entity e = event.getEntity();
        if(e instanceof ExperienceOrb){
            e.remove();
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
            Block block = event.getHitBlock();
            block.getWorld().playSound(block.getLocation(), block.getSoundGroup().getPlaceSound(), 1f, UsefulThings.randomPitchSimple()+0.2f);
            // switch for sound
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
                net.kyori.adventure.sound.Sound sound = net.kyori.adventure.sound.Sound.sound(Key.key("minecraft:sawors.raid_horn"), net.kyori.adventure.sound.Sound.Source.PLAYER, UsefulThings.getVolume(24), 1);

                p.playSound(sound, net.kyori.adventure.sound.Sound.Emitter.self());
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
            if(p.getHealth() != 0){
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
            }

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
                    //event.setCancelled(true);
                }
            }
    }




    }
