package com.github.sawors.stones.commands;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HandcuffCommandExecutor implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(args.length == 0 && sender instanceof Player){

            Player p = ((Player) sender).getPlayer();
            if(p != null) {UsefulThings.handcuffPlayer(p);}
            return true;

        } else if(args.length >= 1 && args[0] != null){

            Player p = Bukkit.getPlayer(args[0]);
            if(p != null && Bukkit.getOnlinePlayers().contains(p)){UsefulThings.handcuffPlayer(p);}
            return true;

        }
        return false;
    }
}
