package com.github.sawors.stones.items.itemlist;

import com.github.sawors.stones.items.ItemType;
import com.github.sawors.stones.items.StonesItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;

public class GoldRing extends StonesItem{
    
    public GoldRing() {
        super();
        
        setMaterial(Material.GOLD_NUGGET);
        setDisplayName(Component.translatable(ChatColor.GOLD + "Gold Ring"));
        addTag(ItemType.RING);
        
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "unique : " + ChatColor.MAGIC + (int)((Math.random()*10)-1) + (int)((Math.random()*10)-1)));
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.GOLD+ ""+ ChatColor.ITALIC +"classic, stylish, never gets old"));
        
        setLore(lore);
    }
}
