package com.github.sawors.stones.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.github.sawors.stones.Stones;
import com.github.sawors.stones.siege.SExplosionPattern;
import com.github.sawors.stones.siege.StonesExplosions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class StestCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = ((Player) sender).getPlayer();
            //
            //  TEST CODE
            //
            for(Entity e : p.getNearbyEntities(64,64,64)){
                PacketContainer p2 = Stones.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
                p2.getIntegers().write(0, e.getEntityId());
                WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
                watcher.setEntity(p);
                watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class),(byte) (0x40)); //Set status to glowing, found on protocol page
                p2.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
                try{
                    Stones.getProtocolManager().sendServerPacket(p, p2);
                } catch (InvocationTargetException exception) {
                    exception.printStackTrace();
                }
            }
    
            StonesExplosions.doDirectionalExplosion(p.getEyeLocation(), 128, SExplosionPattern.CANNON_BALL, p.getEyeLocation().getDirection());
            //
            //  END OF TEST CODE
            //
            return true;
        }
        return false;
    }
}
