package com.github.sawors.stones.commandexecutors;

import com.github.sawors.stones.UsefulThings.UsefulThings;
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
            UsefulThings.createDisplay(p.getLocation().add(0,1,0), p.getInventory().getItemInMainHand(), false).setSmall(true);
            return true;
        }
        return false;
    }
}
