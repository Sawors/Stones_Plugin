package com.github.sawors.stones.commandexecutors;

import com.github.sawors.stones.FishSpawnRunnable;
import com.github.sawors.stones.Stones;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class WorldspawnCommandExecutor implements CommandExecutor {
    private Plugin plugin = Stones.getPlugin(Stones.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
    if(args.length == 2){

            Long initialDelay = (Long.parseLong(args[0]))*20;
            Long spawnDelay = (Long.parseLong(args[1]))*20;     //transform seconds in MC ticks

        new FishSpawnRunnable(plugin).runTaskTimer(plugin, initialDelay, spawnDelay);
            return true;
    }
        return false;
    }
}
//https://www.section.io/engineering-education/minecraft-plugin-development-a-hands-on-crash-course/