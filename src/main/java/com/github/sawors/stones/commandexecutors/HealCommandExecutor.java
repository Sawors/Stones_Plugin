package com.github.sawors.stones.commandexecutors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class HealCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]){

        if(sender instanceof Player && args.length == 0){
            ((Player) sender).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1 ,100));
        } else if(args.length >= 1 && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))){

            Objects.requireNonNull(Bukkit.getPlayer(args[0])).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1 ,100));
        }
        return false;
    }
}
