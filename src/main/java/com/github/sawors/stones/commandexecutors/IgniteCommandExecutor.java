package com.github.sawors.stones.commandexecutors;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IgniteCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(sender instanceof Player && ((Player) sender).getInventory().getItemInMainHand().getType() != Material.AIR) {
            Player p = ((Player) sender).getPlayer();
            ItemStack item = p.getInventory().getItemInMainHand();
            if (args.length == 0) {args = new String[1]; args[0] = "0";}
            int i = Integer.parseInt(args[0]);
            UsefulThings.igniteItem(item, i, p);
            return true;
        }
        return false;
    }
}
