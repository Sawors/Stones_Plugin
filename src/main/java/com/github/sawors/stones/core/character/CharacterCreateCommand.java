package com.github.sawors.stones.core.character;

import com.github.sawors.stones.Stones;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CharacterCreateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        if(sender instanceof Player){
            StonesCharacter newch = new StonesCharacter((Player) sender);
            Stones.getCharacterManager().saveCharacter(newch);
            return true;
        }
        
        return false;
    }
}
