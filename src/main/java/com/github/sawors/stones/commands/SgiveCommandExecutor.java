package com.github.sawors.stones.commands;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.items.SItem;
import com.github.sawors.stones.items.StonesItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SgiveCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player && args.length >= 1 && ((Player) sender).getPlayer() != null){
            Player p = ((Player) sender).getPlayer();
            try{
                SItem itemname = SItem.valueOf(args[0].toUpperCase());
                ItemStack item = StonesItems.get(itemname);
                if(p != null){
                    p.getInventory().addItem(item);
                }
                return true;
            } catch (IllegalArgumentException exc){
                if(p != null){
                    p.sendMessage(ChatColor.RED + "this item does not exist in the available items");
                }
                return false;
            }
            
            
            
            
            
            

        } else if (sender instanceof Player player){
            player.openInventory(Stones.getItemListDisplay());
            return true;
        }
        return false;
    }


}
