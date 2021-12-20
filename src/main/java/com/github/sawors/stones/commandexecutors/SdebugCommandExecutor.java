package com.github.sawors.stones.commandexecutors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SdebugCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player p = ((Player) sender).getPlayer();
            if(p != null && p.getTargetEntity(16) != null){
                Objects.requireNonNull(p.getTargetEntity(16)).remove();
                return true;
            }


        }

        return false;
    }
}
