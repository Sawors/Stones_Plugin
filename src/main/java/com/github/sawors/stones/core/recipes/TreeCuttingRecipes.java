package com.github.sawors.stones.core.recipes;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;

public class TreeCuttingRecipes implements Listener {
    @EventHandler
    public void fistbreakLog(BlockDropItemEvent event){
        boolean islog = false;
        if(!event.getPlayer().getInventory().getItemInMainHand().getType().toString().contains("_AXE") && event.getItems().size() <= 4 && event.getItems().size() > 0){
            
                for(Item i : event.getItems()){
                    if(i.getItemStack().getType().toString().contains("_LOG")){
                        islog = true;
                        break;
                    }
                }
            if(islog){
                for(Item item : event.getItems()){
                    if(item.getItemStack().getType().toString().contains("_LOG")){
                        try{
                            item.getItemStack().setType(Material.valueOf(item.getItemStack().getType().toString().replaceAll("STRIPPED_", "").replaceAll("LOG", "PLANKS")));
                            item.getItemStack().setAmount(4);
                        }catch(IllegalStateException ex){
                            return;
                        }
                    }
                }
            }
            
        }
    }
}
