package com.github.sawors.stones.recipes;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class Recipes {
    
    
    
    public static ItemStack sickleCut(ItemStack item){
    
        String type = item.getItemMeta().getLocalizedName().toLowerCase(Locale.ENGLISH);
        if(!item.hasItemMeta() || (item.hasItemMeta() && !item.getItemMeta().hasLocalizedName()) || (item.hasItemMeta() && item.getItemMeta().getLocalizedName().length()<1)){
            type = item.getType().toString().toLowerCase(Locale.ENGLISH);
        }
        switch(type){
            case("dandelion"):
                item = UsefulThings.getItem("dandelion_head");
                break;
            case("poppy"):
                item = UsefulThings.getItem("poppy_head");
                break;
            case("blue_orchid"):
                item = UsefulThings.getItem("blue_orchid_buds");
                break;
            case("allium"):
                item = UsefulThings.getItem("allium_head");
                break;
            case("azure_bluet"):
                item = UsefulThings.getItem("azure_bluet_buds");
                break;
            case("red_tulip"):
                item = UsefulThings.getItem("red_tulip_petals");
                break;
            case("orange_tulip"):
                item = UsefulThings.getItem("orange_tulip_petals");
                break;
            case("white_tulip"):
                item = UsefulThings.getItem("white_tulip_petals");
                break;
            case("pink_tulip"):
                item = UsefulThings.getItem("pink_tulip_petals");
                break;
            case("oxeye_daisy"):
                item = UsefulThings.getItem("oxeye_daisy_head");
                break;
            case("cornflower"):
                item = UsefulThings.getItem("cornflower_head");
                break;
            case("lily_of_the_valley"):
                item = UsefulThings.getItem("lily_of_the_valley_buds");
                break;
            case("grass"):
                item = UsefulThings.getItem("thatch");
                break;
        }
        return item;
    }
    
    public static ItemStack sickleCut(Block b){
        ItemStack item = UsefulThings.plantToItem(b);
        return sickleCut(item);
    }
    
    
    
}
