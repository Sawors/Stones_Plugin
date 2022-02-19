package com.github.sawors.stones.database;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.effects.StoneEffect;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;

public class DataHolder {
    public static final HashMap<UUID, ArrayList<StoneEffect>> effectmap = new HashMap<>();
    public static final ArrayList<UUID> removelist = new ArrayList<>();
    public static NamespacedKey rolekey = new NamespacedKey(Stones.getPlugin(Stones.class), "role");
    public static NamespacedKey getRoleKey(){
        return rolekey;
    }
    public static void setRoleKey(NamespacedKey key){
        rolekey = key;
    }

    private static final NamespacedKey immovablekey = new NamespacedKey((Stones.getPlugin(Stones.class)), "immovable");
    public static NamespacedKey getImmovablekey(){
        return immovablekey;
    }

    private static final NamespacedKey itemtypekey = new NamespacedKey((Stones.getPlugin(Stones.class)), "itemtype");
    public static NamespacedKey getItemTypeKey(){
        return itemtypekey;
    }

    private static final NamespacedKey ison = new NamespacedKey(Stones.getPlugin(Stones.class), "ison");
    public static NamespacedKey getIsOnKey(){
        return ison;
    }

    private static final NamespacedKey stonesitemdata = new NamespacedKey(Stones.getPlugin(Stones.class), "color");
    public static NamespacedKey getStonesItemDataKey(){
        return stonesitemdata;
    }

    private static final NamespacedKey specialentity = new NamespacedKey(Stones.getPlugin(Stones.class), "specialentity");
    public static NamespacedKey getSpecialEntity(){
        return specialentity;
    }

        // COLLECTIONS

    private static ArrayList<Player> movementchecklist = new ArrayList<>();
    public static ArrayList<Player> getMovementCheckList() {
        return movementchecklist;
    }

    private static HashMap<String, Inventory> inventorymap= new HashMap<>();
    public static HashMap<String, Inventory> getInventoryMap(){return inventorymap;}


    
    
    //      PLAYER DATA
    public static File createDataForPlayer(UUID id){
        Player p = Bukkit.getPlayer(id);
        File file = new File(Stones.getPlugin().getDataFolder()+"/playerdata/"+id+".yml");
        YamlConfiguration config = new YamlConfiguration();
        if(p != null){
            try{
                if(!file.exists()){
                    config.createSection(StonePlayerData.INVENTORY_CONTENT.toString());
                    config.createSection(StonePlayerData.EFFECTS.toString());
                    config.createSection(StonePlayerData.DEBUFFS.toString());
                    config.createSection(StonePlayerData.LAST_SEEN.toString());
                    config.createSection(StonePlayerData.MUTATIONS.toString());
                    config.createSection(StonePlayerData.LAST_LOCATION.toString());
                    
                    config.set(StonePlayerData.INVENTORY_CONTENT.toString(), p.getInventory().getContents());
                    config.set(StonePlayerData.EFFECTS.toString(), effectmapGetEntry(p.getUniqueId()));
                    config.set(StonePlayerData.DEBUFFS.toString(), null);
                    config.set(StonePlayerData.LAST_SEEN.toString(), LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
                    config.set(StonePlayerData.MUTATIONS.toString(), null);
                    config.set(StonePlayerData.LAST_LOCATION.toString(), p.getLocation());
                    
                    config.save(file);
                    byte[] encoded = Base64.getEncoder().encode(config.saveToString().getBytes(StandardCharsets.UTF_8));
                    try(FileOutputStream outputStream = new FileOutputStream(file)){
                        outputStream.write(encoded);
                    }
                }
            }catch (IOException exception){
                Bukkit.getLogger().log(Level.WARNING, "The Playerdata for this player could not be saved (path : config/Stones/playerdata/"+p.getUniqueId()+")");
                exception.printStackTrace();
            }
        }
        return file;
    }
    
    public static void saveDataForPlayer(UUID id) throws FileNotFoundException {
        Player p = Bukkit.getPlayer(id);
        File file = new File(Stones.getPlugin().getDataFolder()+"/playerdata/"+id+".yml");
        YamlConfiguration config = new YamlConfiguration();
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        if(p != null){
            try{
                config = YamlConfiguration.loadConfiguration(file);
                config.set(StonePlayerData.INVENTORY_CONTENT.toString(), p.getInventory().getContents());
                config.set(StonePlayerData.EFFECTS.toString(), effectmapGetEntry(p.getUniqueId()));
                config.set(StonePlayerData.DEBUFFS.toString(), null);
                config.set(StonePlayerData.LAST_SEEN.toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
                config.set(StonePlayerData.MUTATIONS.toString(), null);
                config.set(StonePlayerData.LAST_LOCATION.toString(), p.getLocation());
                
                
                config.save(file);
            
            }catch (IOException exception){
                Bukkit.getLogger().log(Level.WARNING, "The Playerdata for this player could not be saved (path : config/Stones/playerdata/"+p.getUniqueId()+")");
                exception.printStackTrace();
            }
        }
    }
    
    public static void saveDataEntryForPlayer(UUID id, StonePlayerData entry, Object value) throws FileNotFoundException{
        Player p = Bukkit.getPlayer(id);
        File file = new File(Stones.getPlugin().getDataFolder()+"/playerdata/"+id+".yml");
        YamlConfiguration config;
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        if(p != null){
            try{
                config = YamlConfiguration.loadConfiguration(file);
                if(config.isConfigurationSection(entry.toString())){
                    config.set(String.valueOf(entry), value);
                }
                
                
                config.save(file);
                
            }catch (IOException exception){
                Bukkit.getLogger().log(Level.WARNING, "The Playerdata for this player could not be saved (path : config/Stones/playerdata/"+p.getUniqueId()+")");
                exception.printStackTrace();
            }
        }
    }
    
    public static YamlConfiguration getDataForPlayer(UUID id) throws FileNotFoundException {
        File file = new File(Stones.getPlugin().getDataFolder()+"/playerdata/"+id+".yml");
        YamlConfiguration config;
        if(file.exists()){
            config = YamlConfiguration.loadConfiguration(file);
        } else {
            throw new FileNotFoundException("the file : "+Stones.getPlugin().getDataFolder()+"/playerdata/"+id+".yml"+" cannot be found");
        }
        return config;
    }
    
    public static ArrayList<UUID> getRemoveList(){
        return removelist;
    }
    
    public static void addToRemoveList(UUID id){
        removelist.add(id);
    }
    
    public static void removeFromRemoveList(UUID id){
        if(!removelist.isEmpty()){
            removelist.removeIf(i -> i == id);
        }
    }
    
    public static void triggerRemoveList(){
        if(!removelist.isEmpty()){
            for(UUID id : removelist){
                if(Bukkit.getEntity(id) != null){
                    Objects.requireNonNull(Bukkit.getEntity(id)).remove();
                }
            }
            removelist.clear();
        }
    }
    
    public static HashMap<UUID, ArrayList<StoneEffect>> getEffectmap(){
        return effectmap;
    }
    
    public static ArrayList<StoneEffect> effectmapGetEntry(UUID id){
        if(effectmap.containsKey(id)){
            return effectmap.get(id);
        }
        return new ArrayList<>();
    }
    
    public static void effectmapSet(UUID id, ArrayList<StoneEffect> effects){
        effectmap.put(id, effects);
    }
    
    public static void effectmapAdd(UUID id, StoneEffect effect){
        if(effectmap.containsKey(id)){
            effectmap.get(id).add(effect);
        } else {
            ArrayList<StoneEffect> l = new ArrayList<>();
            l.add(effect);
            effectmap.put(id, l);
        }
        
    }
    
    public static void effectmapRemove(UUID id, StoneEffect effect){
        if(effectmap.containsKey(id)){
            effectmap.get(id).remove(effect);
        }
        
    }
}
