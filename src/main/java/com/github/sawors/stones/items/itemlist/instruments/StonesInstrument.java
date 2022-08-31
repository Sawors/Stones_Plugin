package com.github.sawors.stones.items.itemlist.instruments;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.items.ItemType;
import com.github.sawors.stones.items.StonesItem;
import com.github.sawors.stones.items.itemlist.MusicParchment;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StonesInstrument extends StonesItem implements Listener {
    
    public StonesInstrument() {
        super();
        
        setMaterial(Material.SHIELD);
        addType(ItemType.INSTRUMENT);
        
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.GRAY+ ""+ ChatColor.ITALIC +"hold a Music Parchment in your offhand to play music"));
        
        setLore(lore);
    }
    
    @EventHandler
    public static void onPlayerPlaysMusic(PlayerInteractEvent event){
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        
    
        if (item.hasItemMeta()) {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            
            String id = container.get(StonesItem.getItemIdKey(), PersistentDataType.STRING);
    
            ArrayList<String> tags = new ArrayList<>();
            String typesstr = container.get(StonesItem.getItemTagsKey(), PersistentDataType.STRING);
            if(typesstr != null){
                if(typesstr.contains(":")){
                    tags.addAll(List.of(typesstr.split(":")));
                } else {
                    tags.add(typesstr);
                }
            }
           
            // actually attempting to play the sound
            if(id != null && event.getAction().isRightClick() && tags.contains(ItemType.INSTRUMENT.tagString()) && item.getType().equals(Material.SHIELD) && event.getPlayer().getCooldown(Material.SHIELD) <= 0 ){
                String music = "";
                Sound sound = null;
                String soundstr = "";
                if(id.contains("_flute")){
                    sound = Sound.BLOCK_NOTE_BLOCK_FLUTE;
                } else if(id.contains("_lyre")){
                    sound = Sound.BLOCK_NOTE_BLOCK_HARP;
                } else if(id.contains("_guitar")){
                    sound = Sound.BLOCK_NOTE_BLOCK_GUITAR;
                } else if(id.contains("_banjo")){
                    sound = Sound.BLOCK_NOTE_BLOCK_BANJO;
                } else if(id.contains("_doublebass")){
                    soundstr = "minecraft:sawors.instrument.doublebass";
                } else if(id.contains("_harp")){
                    soundstr = "minecraft:sawors.instrument.harp";
                } else if(id.contains("_koto")){
                    soundstr = "minecraft:sawors.instrument.koto";
                } else if(id.contains("_oud")){
                    soundstr = "minecraft:sawors.instrument.oud";
                } else if(id.contains("_panflute")){
                    soundstr = "minecraft:sawors.instrument.panflute";
                } else if(id.contains("_sitar")){
                    soundstr = "minecraft:sawors.instrument.sitar";
                } else if(id.contains("molophone")){
                    soundstr = "minecraft:sawors.instrument.molophone";
                }
    
                // checks for music parchment
                if(p.getInventory().getItemInOffHand().getType().equals(Material.PAPER)){
                    // && p.getInventory().getItemInOffHand().hasItemMeta() && p.getInventory().getItemInOffHand().getItemMeta().getLocalizedName().equals("music_parchment") &&
                    // p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING) != null
                    ItemStack offitem = p.getInventory().getItemInOffHand();
    
    
                    if (offitem.hasItemMeta()) {
                        PersistentDataContainer offcontainer = offitem.getItemMeta().getPersistentDataContainer();
    
                        String offid = container.get(StonesItem.getItemIdKey(), PersistentDataType.STRING);
                        
                        if(Objects.equals(offid, new MusicParchment().getId())){
                            music = container.get(MusicParchment.getMusicSheetKey(), PersistentDataType.STRING);
                        }
                        
                    }
                   
                }
    
    
                if(music != null && music.length() > 0){
                    if(sound != null){
                        UsefulThings.playMusic(music, p, true, 2, sound);
                    } else if(soundstr.length() != 0){
                        UsefulThings.playMusic(music, p, true, 2, soundstr);
                    } else{
                        UsefulThings.playMusic(music, p, true, 2, Sound.ENTITY_VILLAGER_YES);
                    }
                } else {
                    p.sendActionBar(Component.text(ChatColor.RED + "no music selected"));
                    float[] pitch = {(-p.getLocation().getPitch()/180)+1};
                    if(sound != null){
                        UsefulThings.playMusic(pitch, p, true, 2, sound);
                    } else if(soundstr.length() != 0){
                        UsefulThings.playMusic(pitch, p, true, 2, soundstr);
                    } else{
                        UsefulThings.playMusic(pitch, p, true, 2, Sound.ENTITY_VILLAGER_YES);
                    }
                }
            }
            
        }
        
        /*
        *
            if(event.getAction().isRightClick() && item.getType().equals(Material.SHIELD) && event.getPlayer().getCooldown(Material.SHIELD) <= 0 && Objects.equals(item.getItemMeta().getPersistentDataContainer().get(DataHolder.getItemTypeKey(), PersistentDataType.STRING), "instrument")){
            
                
            }*/
    }
}
