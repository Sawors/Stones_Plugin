package com.github.sawors.stones.commands;

import com.github.sawors.stones.items.itemlist.MusicParchment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StestCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = ((Player) sender).getPlayer();
            //
            //  TEST CODE
            //
            assert p != null;
            String musicsheet = MusicParchment.buildMusicSheet("Never Gonna Give You Up","00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz");
            p.sendMessage(musicsheet);
            p.sendMessage("Music name : "+MusicParchment.getMusicName(musicsheet));
            p.sendMessage("Music notes : "+MusicParchment.getMusicNotes(musicsheet));
            //
            //  END OF TEST CODE
            //
            return true;
        }
        return false;
    }
}
