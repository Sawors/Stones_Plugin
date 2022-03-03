package com.github.sawors.stones.commands;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SspawnCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length >= 1 && sender instanceof Player){
            switch(args[0]){
                case("firefly"):
                    UsefulThings.spawnEntity(((Player) sender).getLocation(), "firefly");
            }
        }

        return false;
    }
}
