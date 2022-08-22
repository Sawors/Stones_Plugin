package com.github.sawors.stones.magic;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatController implements Listener {
    
    @EventHandler
    public void onChat(AsyncChatEvent event){
        String content = UsefulThings.getContent(event.message());
        Player sender = event.getPlayer();
        if(!event.isCancelled() && content.length() < 64 && Character.isAlphabetic(content.toCharArray()[0])){
            switch(content){
                case"shadowfax":
                case"Shadowfax":
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            MagicExecutor.invokeShadowfax(sender);
                        }
                    }.runTask(Stones.getPlugin());
                    event.message(Component.text(ChatColor.MAGIC+""+ChatColor.BOLD+content));
                    break;
                case"light":
                
            }
            
        }
        
    }
}
