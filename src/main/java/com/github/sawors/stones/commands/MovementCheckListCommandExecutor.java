package com.github.sawors.stones.commands;

import com.github.sawors.stones.core.database.DataHolder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MovementCheckListCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(sender instanceof Player) {
            sender.sendMessage(ChatColor.YELLOW + "Movement Checklist :");
            for(Player player : DataHolder.getMovementCheckList()){
                sender.sendMessage("\n" + player.getName());
            }
            return true;
        }
        return false;
    }
}
