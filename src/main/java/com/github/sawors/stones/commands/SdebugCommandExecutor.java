package com.github.sawors.stones.commands;

import com.github.sawors.stones.core.database.DataHolder;
import com.github.sawors.stones.items.StonesItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SdebugCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player p = ((Player) sender).getPlayer();
            if(p != null && p.getTargetEntity(4) != null && args.length == 0){
                Objects.requireNonNull(p.getTargetEntity(4)).remove();
                return true;
            }
        }
    
        if(args.length >= 1){
            String arg1 = args[0];
            switch(arg1){
                case "purge":
                    int size = DataHolder.getRemoveList().size();
                    DataHolder.triggerRemoveList();
                    if(size > 1){
                        sender.sendMessage(ChatColor.GREEN + "Successfully removed " + size + " entities");
                    } else if(size == 1){
                        sender.sendMessage(ChatColor.GREEN + "Successfully removed " + size + " entity");
                    } else {
                        sender.sendMessage(ChatColor.RED + "There is no display entity to remove");
                    }
                    return true;
                    
                case "purgeALL":
                    int number = 0;
                    for(World w : Bukkit.getWorlds()){
                        for(Entity e : w.getEntities()){
                            if(e instanceof ArmorStand && e.getCustomName()!= null && e.getCustomName().equals("_display")){
                                e.remove();
                                number ++;
                            }
                        }
                    }
                    if(number > 1){
                        sender.sendMessage(ChatColor.GREEN + "Successfully removed " + number + " entities using ALL method");
                    } else if(number == 1){
                        sender.sendMessage(ChatColor.GREEN + "Successfully removed " + number + " entity using ALL method");
                    } else {
                        sender.sendMessage(ChatColor.RED + "There is no display entity to remove");
                    }
                    return true;
                case "visibility":
                case "visi":
                    if(sender instanceof Player && ((Player) sender).getTargetEntity(4) != null && ((Player) sender).getTargetEntity(4) instanceof ArmorStand){
                        ArmorStand e = (ArmorStand) ((Player) sender).getTargetEntity(4);
                        if(e!=null && e.isVisible()){
                            e.setVisible(false);
                            e.setCustomName("_hide");
                            return true;
                        } else if(e!=null && !e.isVisible()){
                            e.setVisible(true);
                            e.setCustomName("Armor Stand");
                            return true;
                        }
                        
                        
                    }
                    break;
                case "effects":
                    if(sender instanceof Player){
                        sender.sendMessage(DataHolder.effectmapGetEntry(((Player) sender).getUniqueId()).toString());
                        return true;
                    } else {
                        return false;
                    }
                case"item":
                    if(sender instanceof Player player){
                        player.sendMessage(ChatColor.GOLD + "ID : "+ ChatColor.YELLOW + StonesItem.getItemId(player.getInventory().getItemInMainHand()));
                        return true;
                    }
            }
        }

        return false;
    }
}
