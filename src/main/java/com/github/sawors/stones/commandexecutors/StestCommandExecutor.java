package com.github.sawors.stones.commandexecutors;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
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
           
            String t = ComponentSerializer.toString(Component.text(ChatColor.YELLOW+ "lol"));
            p.sendMessage(Component.text(ChatColor.YELLOW+ "lol"));
        }
        return true;
    }
}
