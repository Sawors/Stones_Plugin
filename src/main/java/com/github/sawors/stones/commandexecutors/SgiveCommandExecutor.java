package com.github.sawors.stones.commandexecutors;

import com.github.sawors.stones.UsefulThings.DataHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class SgiveCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player && args.length >= 1 && ((Player) sender).getPlayer() != null){
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            ArrayList<Component> lore = new ArrayList<>();
            Player p = ((Player) sender).getPlayer();

            switch(args[0]){
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

                case "hammer":
                    item.setType(Material.STICK);
                    meta.displayName(Component.text(ChatColor.WHITE + "Hammer"));
                    meta.setLocalizedName("hammer");
                    lore.add(Component.text(""));
                    lore.add(Component.text(ChatColor.DARK_GRAY + "" +"Hammer time !"));
                    meta.lore(lore);
                    meta.setUnbreakable(true);
                    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    break;

                case "ring":
                    item.setType(Material.GOLD_NUGGET);
                    meta.displayName(Component.text(ChatColor.GOLD + "Golden Ring"));
                    meta.setLocalizedName("ring_base_gold");
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
                    lore.add(Component.text(ChatColor.DARK_GREEN + "Hehehe, me no abla tacos"));
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
            p.getInventory().addItem(item);

        } else if (sender instanceof Player){
            sender.sendMessage(ChatColor.RED + "- you must specify an item ! (or your 2nd argument is invalid)");
            sender.sendMessage(ChatColor.GREEN + "- Anyway... here's a " + ChatColor.YELLOW + "banana");
            ((Player) sender).getWorld().playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);

            sender.sendMessage(
                    ChatColor.DARK_PURPLE +"\nitem list :" +
                    ChatColor.WHITE + "\n    parch" +
                    "\n    hammer" +
                    "\n    ring" +
                    "\n    crystal" +
                    ChatColor.GRAY + "\nUse /sgive with one of these items");

            ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
            ItemMeta meta = item.getItemMeta();
            ArrayList<Component> lore = new ArrayList<>();

            meta.displayName(Component.text(ChatColor.YELLOW + "" +ChatColor.BOLD + "Banana"));
            meta.setLocalizedName("banana");
            lore.add(Component.text(""));
            lore.add(Component.text(ChatColor.GOLD + "" + ChatColor.ITALIC + "       <3       b a n a n a       <3       "));
            lore.add(Component.text(""));
            meta.lore(lore);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(meta);
            ((Player) sender).getInventory().addItem(item);
            return true;
        }
        return false;
    }


}
