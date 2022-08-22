package com.github.sawors.stones.combat;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import org.bukkit.inventory.ItemStack;

public class WeaponUtils {
    public static SWeapon getWeaponType(ItemStack item){
        String itemtype = UsefulThings.getItemType(item);
        
        //  SWORDS
        if(itemtype.contains("_SWORD")){
            
            return SWeapon.SWORD;
        }
        
        //  DAGGERS
        else if(itemtype.contains("_DAGGER")){
            
            if(itemtype.contains("_CURVED_DAGGER")){
                return SWeapon.CURVED_DAGGER;
            }
            return SWeapon.DAGGER;
        }
        
        //  AXES
        else if(itemtype.contains("_AXE")){
            
            return SWeapon.AXE;
        }
        
        //  UNKNOWN
        else {
            
            return SWeapon.UNDEFINED;
        }
    }
    
    static boolean canParry(ItemStack item){
        return getWeaponType(item).toString().contains("SWORD");
    }
}
