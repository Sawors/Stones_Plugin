package com.github.sawors.stones.commandexecutors;

import com.github.sawors.stones.features.StonesItems;
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

import java.util.ArrayList;

public class SgiveCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player && args.length >= 1 && ((Player) sender).getPlayer() != null){
            Player p = ((Player) sender).getPlayer();

            ItemStack item = StonesItems.get(args[0]);
            if(p != null){
                p.getInventory().addItem(item);
            }
            return true;

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
