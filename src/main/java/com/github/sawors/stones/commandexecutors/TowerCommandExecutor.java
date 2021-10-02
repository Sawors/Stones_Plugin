package com.github.sawors.stones.commandexecutors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TowerCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
    if(sender instanceof Player){

        Player p = (Player) sender;

        Location origin = p.getLocation().add(0,3,0);
        for(int i = 0; i < 32; i++){

            origin.getBlock().setType(Material.COBBLESTONE);
            origin.add(0,1,0);
        }

        p.sendMessage("§aRise my beautiful creation ! RISE !!!");
        return true;
    }else{
        System.out.println("this is a big no-no using my beautiful plugin from the command line !");
        return false;
    }


    }
}
