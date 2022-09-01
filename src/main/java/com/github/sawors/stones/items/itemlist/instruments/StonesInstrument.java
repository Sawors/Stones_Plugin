package com.github.sawors.stones.items.itemlist.instruments;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.items.ItemType;
import com.github.sawors.stones.items.StonesItem;
import com.github.sawors.stones.items.itemlist.MusicParchment;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

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
                // checks for music parchment
                if(p.getInventory().getItemInOffHand().getType().equals(Material.PAPER)){
                    // && p.getInventory().getItemInOffHand().hasItemMeta() && p.getInventory().getItemInOffHand().getItemMeta().getLocalizedName().equals("music_parchment") &&
                    // p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING) != null
                    ItemStack offitem = p.getInventory().getItemInOffHand();
    
    
                    if (offitem.hasItemMeta()) {
                        PersistentDataContainer offcontainer = offitem.getItemMeta().getPersistentDataContainer();
    
                        String offid = offcontainer.get(StonesItem.getItemIdKey(), PersistentDataType.STRING);
                        
                        if(Objects.equals(offid, MusicParchment.getTypeId()
                        )){
                            music = offcontainer.get(MusicParchment.getMusicSheetKey(), PersistentDataType.STRING);
                        }
                        
                    }
                   
                }
    
                String soundtype = container.get(getSoundtypeKey(), PersistentDataType.STRING);
                if(music != null && music.length() > 2){
                    if(soundtype != null && soundtype.length() > 0){
                        p.sendActionBar(Component.text(MusicParchment.getMusicName(music)));
                        StonesInstrument.playMusic(MusicParchment.getMusicNotes(music), p, true, 2, soundtype);
                    } else{
                        StonesInstrument.playMusic(MusicParchment.getMusicNotes(music), p, true, 2, Sound.ENTITY_VILLAGER_YES);
                    }
                } else {
                    p.sendActionBar(Component.text(ChatColor.RED + "no music selected"));
                    float[] pitch = {(-p.getLocation().getPitch()/180)+1};
                    if(soundtype != null && soundtype.length() > 0){
                        StonesInstrument.playMusic(pitch, p, true, 2, soundtype);
                    } else{
                        StonesInstrument.playMusic(pitch, p, true, 2, Sound.ENTITY_VILLAGER_YES);
                    }
                }
            }
            
        }
    }
    
    public static NamespacedKey getSoundtypeKey(){
        return new NamespacedKey((Stones.getPlugin(Stones.class)), "soundtype");
    }
    
    public static float noteToPitch(char note){
        note = Character.toLowerCase(note);
        switch(note){
            case('a'):
                return 0.5f;
            case('b'):
                return 0.529732f;
            case('c'):
                return 0.561231f;
            case('d'):
                return 0.594604f;
            case('e'):
                return 0.629961f;
            case('f'):
                return 0.667420f;
            case('g'):
                return 0.707107f;
            case('h'):
                return 0.749154f;
            case('i'):
                return 0.793701f;
            case('j'):
                return 0.840896f;
            case('k'):
                return 0.890899f;
            case('l'):
                return 0.943874f;
            case('m'):
                return 1.0f;
            case('n'):
                return 1.059463f;
            case('o'):
                return 1.122462f;
            case('p'):
                return 1.189207f;
            case('q'):
                return 1.259921f;
            case('r'):
                return 1.334840f;
            case('s'):
                return 1.414214f;
            case('t'):
                return 1.498307f;
            case('u'):
                return 1.587401f;
            case('v'):
                return 1.681793f;
            case('w'):
                return 1.781797f;
            case('x'):
                return 1.887749f;
            case('y'):
                return 2.0f;
            case('z'):
                return -1f;
            case('0'):
                return 0f;
            default:
                return -2f;
        }
    }
    
    public static float[] noteToPitch(char[] note){
        float[] pitch = new float[note.length];
        for(int i = 0; i < note.length; i++){
            pitch[i] = noteToPitch(note[i]);
        }
        return pitch;
    }
    
    public static float[] noteToPitch(char[] note, int forcedlength){
        float[] pitch = new float[forcedlength];
        
        for(int i = 0; i < forcedlength; i++){
            if(i >= note.length){
                pitch[i] = -1;
            }else{
                pitch[i] = noteToPitch(note[i]);
            }
            
        }
        return pitch;
    }
    
    public static Color noteToColor(char note){
        note = Character.toLowerCase(note);
        int color = 0x000000;
        switch(note){
            case('a'):
                color = 0x77D700;
                break;
            case('b'):
                color = 0x95C000;
                break;
            case('c'):
                color = 0xB2A500;
                break;
            case('d'):
                color = 0xCC8600;
                break;
            case('e'):
                color = 0xE26500;
                break;
            case('f'):
                color = 0xF34100;
                break;
            case('g'):
                color = 0xFC1E00;
                break;
            case('h'):
                color = 0xFE000F;
                break;
            case('i'):
                color = 0xF70033;
                break;
            case('j'):
                color = 0xE8005A;
                break;
            case('k'):
                color = 0xCF0083;
                break;
            case('l'):
                color = 0xAE00A9;
                break;
            case('m'):
                color = 0x8600CC;
                break;
            case('n'):
                color = 0x5B00E7;
                break;
            case('o'):
                color = 0x2D00F9;
                break;
            case('p'):
                color = 0x020AFE;
                break;
            case('q'):
                color = 0x0037F6;
                break;
            case('r'):
                color = 0x0068E0;
                break;
            case('s'):
                color = 0x009ABC;
                break;
            case('t'):
                color = 0x00C68D;
                break;
            case('u'):
                color = 0x00E958;
                break;
            case('v'):
                color = 0x00FC21;
                break;
            case('w'):
                color = 0x1FFC00;
                break;
            case('x'):
                color = 0x59E800;
                break;
            case('y'):
                color = 0x94C100;
                break;
        }
        
        return Color.fromRGB(color);
    }
    
    public static Color[] noteToColor(char[] note){
        Color[] colors = new Color[note.length];
        for(int i = 0; i < note.length; i++){
            colors[i] = noteToColor(note[i]);
        }
        return colors;
    }
    
    public static Color[] noteToColor(char[] note, int forcedlength){
        Color[] colors = new Color[forcedlength];
        
        for(int i = 0; i < forcedlength; i++){
            if(i >= note.length){
                colors[i] = Color.BLACK;
            }else{
                colors[i] = noteToColor(note[i]);
            }
            
        }
        return colors;
    }
    
    public static void playMusic(float[] pitch, Player p, boolean shouldhold, int speed, Sound sound){
        if(speed <= 0){
            return;
        }
        new BukkitRunnable(){
            int timer = 0;
            
            @Override
            public void run(){
                if((timer >= pitch.length) || (!p.isBlocking() && shouldhold && timer > 2) || (timer >= 512 || pitch[timer] < 0)) {
                    this.cancel();
                    return;
                }
                if(pitch[timer] > 0){
                    Location loc = p.getLocation();
                    if(p.isSneaking()){
                        loc = loc.subtract(0,0.5,0);
                    }
                    loc.getWorld().playSound(loc.clone().add(0,1.5,0), sound, 1, pitch[timer]);
                    loc.getWorld().spawnParticle(Particle.NOTE, loc.clone().add(0,1.5+pitch[timer],0), 1,.1,0,.1,pitch[timer]/10);
                }
                timer++;
            }
        }.runTaskTimer(Stones.getPlugin(), 0,speed);
        
    }
    
    public static void playMusic(String note, Player p, boolean shouldhold, int speed, Sound sound){
        char[] notes = note.toCharArray();
        float[] pitch = StonesInstrument.noteToPitch(notes);
        playMusic(pitch, p, shouldhold, speed, sound);
    }
    
    public static void playMusic(String note, Player p){
        playMusic(note, p, false, 2, Sound.BLOCK_NOTE_BLOCK_HARP);
    }
    
    public static void playMusic(float[] pitch, Player p, boolean shouldhold, int speed, String sound){
        if(speed <= 0){
            return;
        }
        new BukkitRunnable(){
            int timer = 0;
            
            @Override
            public void run(){
                if((timer >= pitch.length) || (!p.isBlocking() && shouldhold && timer > 2) || (timer >= 1800 || pitch[timer] < 0)) {
                    this.cancel();
                    return;
                }
                if(pitch[timer] > 0){
                    Location loc = p.getLocation();
                    if(p.isSneaking()){
                        loc = loc.subtract(0,0.5,0);
                    }
                    loc.getWorld().playSound(loc.clone().add(0,1.5,0), sound, 1, pitch[timer]);
                    loc.getWorld().spawnParticle(Particle.NOTE, loc.clone().add(0,1.5+pitch[timer],0), 1,.1,0,.1,pitch[timer]/10);
                }
                timer++;
            }
        }.runTaskTimer(Stones.getPlugin(), 0,speed);
        
    }
    
    public static void playMusic(String note, Player p, boolean shouldhold, int speed, String sound){
        char[] notes = note.toCharArray();
        float[] pitch = StonesInstrument.noteToPitch(notes);
        playMusic(pitch, p, shouldhold, speed, sound);
    }
}
