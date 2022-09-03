package com.github.sawors.stones.items.itemlist.wearable;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.events.PlayerWearHatEvent;
import com.github.sawors.stones.items.ItemTag;
import com.github.sawors.stones.items.StonesItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
        addTag(ItemTag.HAT);
    
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
                if (taglist.contains(ItemTag.HAT.tagString())) {
                    p.getInventory().setItemInMainHand(p.getInventory().getHelmet());
                    if(getItemTags(p.getInventory().getHelmet()).contains(ItemTag.HAT.tagString())){
                        // calling wear hat event when removing the previous hat is there is one
                        PlayerWearHatEvent hatevent = new PlayerWearHatEvent(p, item, false);
                        Bukkit.getPluginManager().callEvent(hatevent);
                    }
                    p.getInventory().setHelmet(item);
                    p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
    
                    // calling wear hat event
                    Stones.adminLog("wearing");
                    PlayerWearHatEvent hatevent = new PlayerWearHatEvent(p, item, true);
                    Bukkit.getPluginManager().callEvent(hatevent);
                }
            }
        } else if (item.getType() == Material.AIR) {
            //"unwear" hat
            ItemStack helmet = p.getInventory().getHelmet();
            if (p.getInventory().getItemInMainHand().getType() == Material.AIR && helmet != null && getItemTags(helmet).contains(ItemTag.HAT.tagString()) && p.isSneaking() && p.getLocation().getPitch() >= 85) {
                if (!taglist.contains(ItemTag.UNMOVABLE.tagString())) {
                    p.getInventory().setItemInMainHand(helmet);
                    p.getInventory().setHelmet(null);
                    p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1.5F);
                    // calling wear hat event
                    PlayerWearHatEvent hatevent = new PlayerWearHatEvent(p, item, false);
                    Bukkit.getPluginManager().callEvent(hatevent);
                }
        
            }
        }
    }
}
