package com.github.sawors.stones.features;

import com.github.sawors.stones.UsefulThings.DataHolder;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class StonesBooks implements Listener {
    
    @EventHandler
    public void playerUseBook(PlayerInteractEvent event) {
        if (event.getAction().isRightClick() && event.getClickedBlock() != null && event.getClickedBlock().isSolid() && event.getBlockFace().equals(BlockFace.UP) && (event.getPlayer().isSneaking() || event.getPlayer().isInsideVehicle()) && event.getPlayer().getInventory().getItemInMainHand().getType().toString().contains("BOOK")) {
            event.setCancelled(true);
            Player p = event.getPlayer();
            ItemStack item = p.getInventory().getItemInMainHand().clone();
            item.setAmount(1);
            Location blockloc = event.getInteractionPoint().add(0,1,0);
            blockloc.setY(blockloc.getY()+(Math.random()/100));
            /*Location blockloc = event.getClickedBlock().getLocation().add(0.5, 0, 0.5);
            blockloc.setY(event.getClickedBlock().getBoundingBox().getMaxY() + 1);*/
            
            if(!(item.hasItemMeta() && item.getItemMeta().hasLocalizedName())){
                blockloc.setYaw(p.getLocation().getYaw()-90);
            } else {
                blockloc.setYaw(p.getLocation().getYaw());
            }
            
            ArmorStand stand = UsefulThings.createDisplay(blockloc, item, false);
            stand.setSmall(true);
            stand.setCustomName("_book");
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
            
            
        } else if (event.getAction().isRightClick() && (event.getClickedBlock() == null || !event.getBlockFace().equals(BlockFace.UP)) && event.getPlayer().isSneaking() && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BOOK)) {
            event.setCancelled(true);
            Player p = event.getPlayer();
            ItemStack item = p.getInventory().getItemInMainHand();
            switchOpenClose(item, p);
            
            
        } else if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BOOK) && event.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY) != null){
            event.setCancelled(true);
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            int[] data = item.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY);
            if(data != null && data.length >=2 && item.getItemMeta().lore().get(0).toString().contains("Open")){
                if(event.getAction().isRightClick()){
                    if(data[0]+1 <= data[1]){
                        addPage(item);
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, .5f, 1);
                        event.getPlayer().sendActionBar(item.getItemMeta().lore().get(2));
                    }
                    
                } else if(event.getAction().isLeftClick()){
                    if(data[0]-1 >= 1){
                        backPage(item);
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, .5f, 1);
                        event.getPlayer().sendActionBar(item.getItemMeta().lore().get(2));
                    }
                }
            }
            
        }
    }
    
    @EventHandler
    public void playerRetrievesBook(PlayerInteractAtEntityEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType().isAir() && event.getRightClicked() instanceof ArmorStand && ((ArmorStand) event.getRightClicked()).getEquipment().getHelmet() != null && ((ArmorStand) event.getRightClicked()).getEquipment().getHelmet().getType().toString().contains("BOOK")) {
            if((event.getPlayer().isSneaking() || event.getPlayer().isInsideVehicle())){
                event.getPlayer().getInventory().setItemInMainHand(((ArmorStand) event.getRightClicked()).getEquipment().getHelmet());
                event.getRightClicked().remove();
            } else if(((ArmorStand) event.getRightClicked()).getEquipment().getHelmet().getType().equals(Material.BOOK)){
                ItemStack item = ((ArmorStand) event.getRightClicked()).getEquipment().getHelmet().clone();
                assert item != null;
                switchOpenClose(item, event.getRightClicked());
                ((ArmorStand) event.getRightClicked()).getEquipment().setHelmet(item);
            }
            
        }
    }
    
    @EventHandler
    public void playerRotatesBook(EntityDamageByEntityEvent event){
        Entity e = event.getEntity();
        Entity p = event.getDamager();
        if(e instanceof ArmorStand && ((ArmorStand) e).getEquipment().getHelmet() != null && ((ArmorStand) e).getEquipment().getHelmet().getType().toString().contains("BOOK") && e.getCustomName().contains("_book") && p instanceof Player){
            
            Location rot = e.getLocation();
            if(((Player) p).isSneaking()){
                rot.setYaw(rot.getYaw()-10);
            } else {
                rot.setYaw(rot.getYaw()+10);
            }
            
            e.teleport(rot);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void changePage(ItemStack book, int targetpage) {
        if (book.getType().equals(Material.BOOK) && book.hasItemMeta()) {
            ItemMeta meta = book.getItemMeta().clone();
            int[] data = meta.getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY);
            if (data != null && data.length >= 2) {
                
                List<Component> lore = meta.lore();
                data[0] = targetpage;
                if (lore != null) {
                    lore.set(2, Component.text(ChatColor.GOLD + " " + data[0] + "/" + data[1] + " pages"));
                }
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY, data);
                meta.lore(lore);
                book.setItemMeta(meta);
            }
        }
    }
    
    public void addPage(ItemStack book) {
        if (book.getType().equals(Material.BOOK) && book.hasItemMeta() && book.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY) != null) {
            changePage(book, book.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY)[0]+1);
        }
    }
    
    public void backPage(ItemStack book) {
        if (book.getType().equals(Material.BOOK) && book.hasItemMeta() && book.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY) != null) {
            changePage(book, book.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY)[0]-1);
        }
    }
    
    public void switchOpenClose(ItemStack book, Entity p) {
        ItemMeta meta = book.getItemMeta();
        List<Component> lore = meta.lore();
        if (book.getType().equals(Material.BOOK) && book.hasItemMeta() && book.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY) != null) {
            if (lore != null) {
                if (lore.get(0).toString().contains("Open")) {
                    lore.set(0, Component.text(ChatColor.GRAY + "Close"));
                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, .5f, 0.75f);
                } else {
                    lore.set(0, Component.text(ChatColor.GRAY + "Open"));
                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, .5f, 1.25f);
                    if (p instanceof Player) {
                        p.sendActionBar(lore.get(2));
                    }
                }
                meta.lore(lore);
                book.setItemMeta(meta);
            } else {
                if (p instanceof Player) {
                    p.sendActionBar(Component.text(ChatColor.GRAY + "there is no text to be read in this book"));
                }
            }
        }
    }
    
    public void switchOpenClose(ItemStack book) {
        ItemMeta meta = book.getItemMeta();
        List<Component> lore = meta.lore();
        if (book.getType().equals(Material.BOOK) && book.hasItemMeta() && book.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY) != null) {
            if (lore != null) {
                if (lore.get(0).toString().contains("Open")) {
                    lore.set(0, Component.text(ChatColor.GRAY + "Close"));
                } else {
                    lore.set(0, Component.text(ChatColor.GRAY + "Open"));
                }
                meta.lore(lore);
                book.setItemMeta(meta);
            }
        }
    }
    
    public void setOpen(ItemStack book, boolean open) {
        ItemMeta meta = book.getItemMeta();
        List<Component> lore = meta.lore();
        if (book.getType().equals(Material.BOOK) && book.hasItemMeta() && book.getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY) != null) {
            if (lore != null) {
                if (open) {
                    lore.set(0, Component.text(ChatColor.GRAY + "Open"));
                } else {
                    lore.set(0, Component.text(ChatColor.GRAY + "Close"));
                }
                meta.lore(lore);
                book.setItemMeta(meta);
            }
        }
    }
    
    
    
    
}