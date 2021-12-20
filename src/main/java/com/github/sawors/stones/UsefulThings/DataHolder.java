package com.github.sawors.stones.UsefulThings;

import com.github.sawors.stones.Stones;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

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



}
