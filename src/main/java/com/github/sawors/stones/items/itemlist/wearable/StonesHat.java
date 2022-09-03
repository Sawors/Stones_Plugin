package com.github.sawors.stones.items.itemlist.wearable;

import com.github.sawors.stones.items.ItemType;
import com.github.sawors.stones.items.StonesItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StonesHat extends StonesItem implements Listener {
    
    public StonesHat() {
        super();
    
        setMaterial(Material.BROWN_DYE);
        addTag(ItemType.HAT);
    
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        lore.add(Component.text("Shift Click on air above your head to wear it"));
        setLore(lore);
    }
    
    @EventHandler
    public static void hatWearAction(PlayerInteractEvent event){
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        Block b = event.getClickedBlock();
        String id = StonesItem.getItemId(item);
        List<String> taglist = StonesItem.getItemTags(item);
    
        if (item.hasItemMeta() && taglist.size() > 0) {
            //WEAR HAT
            if (p.getLocation().getPitch() <= -85 && b == null) {
                if (taglist.contains(ItemType.HAT.tagString())) {
                    p.getInventory().setItemInMainHand(p.getInventory().getHelmet());
                    p.getInventory().setHelmet(item);
                    p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
                }
            }
        } else if (item.getType() == Material.AIR) {
            //"unwear" hat
            ItemStack helmet = p.getInventory().getHelmet();
            if (p.getInventory().getItemInMainHand().getType() == Material.AIR && helmet != null && getItemTags(helmet).contains(ItemType.HAT.tagString()) && p.isSneaking() && p.getLocation().getPitch() >= 85) {
                if (!taglist.contains(ItemType.UNMOVABLE.tagString())) {
                    p.getInventory().setItemInMainHand(helmet);
                    p.getInventory().setHelmet(null);
                    p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1.5F);
                }
        
            }
        }
    }
}
