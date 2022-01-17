package com.github.sawors.stones.UsefulThings;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.enums.StonePlayerData;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class DataHolder {
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
                    
                    config.set(StonePlayerData.INVENTORY_CONTENT.toString(), p.getInventory().getContents());
                    config.set(StonePlayerData.EFFECTS.toString(), Stones.effectmapGetEntry(p.getUniqueId()));
                    config.set(StonePlayerData.DEBUFFS.toString(), null);
                    config.set(StonePlayerData.LAST_SEEN.toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
                    config.set(StonePlayerData.MUTATIONS.toString(), null);
                    config.save(file);
                }
            }catch (IOException exception){
                Bukkit.getLogger().log(Level.WARNING, "The Playerdata for this player could not be saved (path : config/Stones/playerdata/"+p.getUniqueId()+")");
                exception.printStackTrace();
            }
        }
        return file;
    }
    
    public static void saveDataForPlayer(UUID id){
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
                } else {
                    config = YamlConfiguration.loadConfiguration(file);
                }
                config.set(StonePlayerData.INVENTORY_CONTENT.toString(), p.getInventory().getContents());
                config.set(StonePlayerData.EFFECTS.toString(), Stones.effectmapGetEntry(p.getUniqueId()));
                config.set(StonePlayerData.DEBUFFS.toString(), null);
                config.set(StonePlayerData.LAST_SEEN.toString(), LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
                config.set(StonePlayerData.MUTATIONS.toString(), null);
                config.save(file);
            
            }catch (IOException exception){
                Bukkit.getLogger().log(Level.WARNING, "The Playerdata for this player could not be saved (path : config/Stones/playerdata/"+p.getUniqueId()+")");
                exception.printStackTrace();
            }
        }
    }
    
    public static YamlConfiguration getDataForPlayer(UUID id){
        Player p = Bukkit.getPlayer(id);
        File file = new File(Stones.getPlugin().getDataFolder()+"/playerdata/"+id+".yml");
        YamlConfiguration config = new YamlConfiguration();
        if(file.exists()){
            config = YamlConfiguration.loadConfiguration(file);
        }
        return config;
    }

}
