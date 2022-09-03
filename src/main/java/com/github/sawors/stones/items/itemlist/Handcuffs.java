package com.github.sawors.stones.items.itemlist;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.items.ItemType;
import com.github.sawors.stones.items.StonesItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Handcuffs extends StonesItem implements Listener {
    public Handcuffs() {
        super();

        setMaterial(Material.IRON_NUGGET);
        setDisplayName(Component.translatable(ChatColor.GRAY + "Handcuffs"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.GREEN + "Right Click at someone to prevent him from using items"));

        setLore(lore);
    }


    @EventHandler
    public static void onPlayerUseHandcuffs(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(event.getRightClicked() instanceof Player rightclicked){
            if(item.hasItemMeta() && StonesItem.getItemId(item).equals(new Handcuffs().getTypeId()) && UsefulThings.isBehind(player, rightclicked, 45)){
                UsefulThings.handcuffPlayer(rightclicked);
                ItemStack itemtemp = player.getInventory().getItemInMainHand().clone();
                itemtemp.setAmount(itemtemp.getAmount()-1);
                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), itemtemp);
            }
        }
    }

    public static void handcuffPlayer(Player p){

        Handcuffs replacement = new Handcuffs();
        replacement.addTag(ItemType.ACTIVATED);
        replacement.addTag(ItemType.UNMOVABLE);
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.RED + "You are now handcuffed, you are prevented from :"));
        lore.add(Component.text(ChatColor.DARK_RED + "- Changing your item in main hand"));
        lore.add(Component.text(ChatColor.DARK_RED + "- Changing your armor"));
        lore.add(Component.text(ChatColor.DARK_RED + "- Interacting with blocks"));
        lore.add(Component.text(ChatColor.DARK_RED + "- Interacting with chest/furnace etc..."));
        lore.add(Component.text(ChatColor.DARK_RED + "- Interacting with horses/donkeys/mules"));
        lore.add(Component.text(ChatColor.DARK_RED + "- Dropping items"));
        replacement.setLore(lore);


        if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getItemInMainHand());
        }

        if (p.getInventory().getItemInOffHand().getType() != Material.AIR) {
            p.getWorld().dropItemNaturally(p.getLocation(), p.getInventory().getItemInOffHand());
        }
        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 0.75f);
        p.getInventory().setItem(p.getInventory().getHeldItemSlot(), replacement.get());
        p.getInventory().setItem(EquipmentSlot.OFF_HAND, replacement.get());
    }

    public static void uncuffPlayer(Player p){
        ItemStack itemmain = p.getInventory().getItemInMainHand();
        ItemStack itemoff = p.getInventory().getItemInOffHand();
        if(itemmain.hasItemMeta() && itemoff.hasItemMeta()){

            if(StonesItem.getItemTags(itemmain).contains(ItemType.UNMOVABLE.tagString()) && StonesItem.getItemTags(itemoff).contains(ItemType.UNMOVABLE.tagString())){
                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), null);
                p.getInventory().setItem(EquipmentSlot.OFF_HAND, null);
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1f, 1.5f);
            }
        }
    }

}
