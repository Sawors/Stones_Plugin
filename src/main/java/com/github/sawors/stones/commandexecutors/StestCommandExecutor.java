package com.github.sawors.stones.commandexecutors;

import com.github.sawors.stones.UsefulThings.DataHolder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class StestCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            ItemStack ii = new ItemStack(Material.RAW_IRON);
            ItemMeta meta = ii.getItemMeta();
            meta.setUnbreakable(true);
            meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "bite");
            ii.setItemMeta(meta);
            p.getInventory().addItem(ii);
        }
        return true;
    }
}
