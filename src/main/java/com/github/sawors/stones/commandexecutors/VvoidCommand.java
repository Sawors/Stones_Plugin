package com.github.sawors.stones.commandexecutors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VvoidCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender).getPlayer();
            assert p != null;
            p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 120, 200, false));
            p.damage(p.getHealth()-1);
            return true;
        }



        return false;
    }
}
