package com.github.sawors.stones.recipes;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.items.SItem;
import com.github.sawors.stones.items.StonesItems;
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
                item = StonesItems.get(SItem.DANDELION_HEAD);
                break;
            case("poppy"):
                item = StonesItems.get(SItem.POPPY_HEAD);
                break;
            case("blue_orchid"):
                item = StonesItems.get(SItem.BLUE_ORCHID_BUDS);
                break;
            case("allium"):
                item = StonesItems.get(SItem.ALLIUM_HEAD);
                break;
            case("azure_bluet"):
                item = StonesItems.get(SItem.AZURE_BLUET_BUDS);
                break;
            case("red_tulip"):
                item = StonesItems.get(SItem.RED_TULIP_PETALS);
                break;
            case("orange_tulip"):
                item = StonesItems.get(SItem.ORANGE_TULIP_PETALS);
                break;
            case("white_tulip"):
                item = StonesItems.get(SItem.WHITE_TULIP_PETALS);
                break;
            case("pink_tulip"):
                item = StonesItems.get(SItem.PINK_TULIP_PETALS);
                break;
            case("oxeye_daisy"):
                item = StonesItems.get(SItem.OXEYE_DAISY_HEAD);
                break;
            case("cornflower"):
                item = StonesItems.get(SItem.CORNFLOWER_HEAD);
                break;
            case("lily_of_the_valley"):
                item = StonesItems.get(SItem.LILY_OF_THE_VALLEY_BUDS);
                break;
            case("grass"):
                item = StonesItems.get(SItem.THATCH);
                break;
        }
        return item;
    }
    
    public static ItemStack sickleCut(Block b){
        ItemStack item = UsefulThings.plantToItem(b);
        return sickleCut(item);
    }
    
    
    
}
