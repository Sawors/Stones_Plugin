package com.github.sawors.stones.recipes;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.items.StoneItem;
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
                item = StonesItems.get(StoneItem.DANDELION_HEAD);
                break;
            case("poppy"):
                item = StonesItems.get(StoneItem.POPPY_HEAD);
                break;
            case("blue_orchid"):
                item = StonesItems.get(StoneItem.BLUE_ORCHID_BUDS);
                break;
            case("allium"):
                item = StonesItems.get(StoneItem.ALLIUM_HEAD);
                break;
            case("azure_bluet"):
                item = StonesItems.get(StoneItem.AZURE_BLUET_BUDS);
                break;
            case("red_tulip"):
                item = StonesItems.get(StoneItem.RED_TULIP_PETALS);
                break;
            case("orange_tulip"):
                item = StonesItems.get(StoneItem.ORANGE_TULIP_PETALS);
                break;
            case("white_tulip"):
                item = StonesItems.get(StoneItem.WHITE_TULIP_PETALS);
                break;
            case("pink_tulip"):
                item = StonesItems.get(StoneItem.PINK_TULIP_PETALS);
                break;
            case("oxeye_daisy"):
                item = StonesItems.get(StoneItem.OXEYE_DAISY_HEAD);
                break;
            case("cornflower"):
                item = StonesItems.get(StoneItem.CORNFLOWER_HEAD);
                break;
            case("lily_of_the_valley"):
                item = StonesItems.get(StoneItem.LILY_OF_THE_VALLEY_BUDS);
                break;
            case("grass"):
                item = StonesItems.get(StoneItem.THATCH);
                break;
        }
        return item;
    }
    
    public static ItemStack sickleCut(Block b){
        ItemStack item = UsefulThings.plantToItem(b);
        return sickleCut(item);
    }
    
    
    
}
