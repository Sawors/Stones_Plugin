package com.github.sawors.stones.commandexecutors;

import com.github.sawors.stones.enums.StoneItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StestCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            
            Player p = (Player) sender;
            p.sendMessage(StoneItem.ALLIUM_HEAD.toString());
            p.sendMessage(String.valueOf(StoneItem.ALLIUM_HEAD));
            
            
            
            
            return true;
        }
        return false;
    }
}
