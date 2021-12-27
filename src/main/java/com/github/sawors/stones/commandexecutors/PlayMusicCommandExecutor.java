package com.github.sawors.stones.commandexecutors;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayMusicCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            if(args.length == 2 && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))){
                UsefulThings.playMusic(args[0], Bukkit.getPlayer(args[1]));
                return true;
            } else if(args.length >= 3 && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))){
                int speed = 2;
                if(Integer.parseInt(String.valueOf(args[2].toCharArray()[0])) != 0){
                    speed = Integer.parseInt(String.valueOf(args[2].toCharArray()[0]));
                }
                UsefulThings.playMusic(args[0], Bukkit.getPlayer(args[1]), false, speed);
                return true;
            } else {
                UsefulThings.playMusic(args[0], ((Player) sender).getPlayer());
                return true;
            }
        }
        return false;
    }
}
