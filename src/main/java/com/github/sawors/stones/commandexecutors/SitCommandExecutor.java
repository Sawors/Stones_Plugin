package com.github.sawors.stones.commandexecutors;


import com.github.sawors.stones.UsefulThings.UsefulThings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SitCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player ){
            LivingEntity p = ((Player) sender).getPlayer();
            if(p != null && args.length == 0){
                UsefulThings.sitEntity(p);
                return true;
            } else if(args.length == 1 && Bukkit.getPlayer(args[0]) != null && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))){
                UsefulThings.sitEntity(Bukkit.getPlayer(args[0]));
                return true;
            }

        }
        return false;
    }
}
