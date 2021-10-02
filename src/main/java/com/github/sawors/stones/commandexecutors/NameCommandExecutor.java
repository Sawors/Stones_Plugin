package com.github.sawors.stones.commandexecutors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class NameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player && ((Player) sender).getInventory().getItemInMainHand().getType() != Material.AIR) {
            Player p = ((Player) sender).getPlayer();
            ItemStack item = p.getInventory().getItemInMainHand();
            p.sendMessage(ChatColor.DARK_PURPLE + "Display Name :");
            p.sendMessage(item.displayName());
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Display Name in Meta:");
            p.sendMessage(Objects.requireNonNull(item.getItemMeta().displayName()));
            p.sendMessage(ChatColor.YELLOW + "Localized Name :");
            p.sendMessage(item.getItemMeta().getLocalizedName());
            return true;
        }
        return false;
    }
}
//https://www.section.io/engineering-education/minecraft-plugin-development-a-hands-on-crash-course/