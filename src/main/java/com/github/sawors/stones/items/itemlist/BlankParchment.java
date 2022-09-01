package com.github.sawors.stones.items.itemlist;

import com.github.sawors.stones.items.StonesItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;

public class BlankParchment extends StonesItem {
    
    public BlankParchment() {
        super();
    
        setMaterial(Material.PAPER);
    
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.GOLD +"To sign it, crouch and use"));
    
        setLore(lore);
    }
}
