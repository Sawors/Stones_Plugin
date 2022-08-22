package com.github.sawors.stones.core.character;

import com.github.sawors.stones.Stones;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;

public class CharacterSwitchCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        if(sender instanceof Player && args.length>0){
            Player p = ((Player) sender);
            
            try{
                StonesCharacter ch = Stones.getCharacterManager().loadCharacter(args[0]);
                ItemStack[] inventory;
                ItemStack[] armor;
                p.displayName(Component.text(ch.getName()));
                p.setHealth(ch.getHp());
                p.setFoodLevel(ch.getFood());
                p.getInventory().setContents(ch.getInventory());
                p.getInventory().setArmorContents(ch.getArmor());
                p.getInventory().setItemInOffHand(ch.getOffhand());
                p.teleport(ch.getLocation());
                p.addPotionEffects(ch.getPotions());
                p.setExp(ch.getXp());
                p.setCompassTarget(ch.getCompasstarget());
                p.setBedSpawnLocation(ch.getSpawnpoint());
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
