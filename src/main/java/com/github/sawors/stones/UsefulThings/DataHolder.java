package com.github.sawors.stones.UsefulThings;

import com.github.sawors.stones.Stones;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class DataHolder {
    private static final NamespacedKey rolekey = new NamespacedKey(Stones.getPlugin(Stones.class), "role");
    private static final NamespacedKey immovablekey = new NamespacedKey((Stones.getPlugin(Stones.class)), "immovable");
    private static final NamespacedKey itemtypekey = new NamespacedKey((Stones.getPlugin(Stones.class)), "itemtype");
    private static final NamespacedKey ison = new NamespacedKey(Stones.getPlugin(Stones.class), "ison");
    private static final NamespacedKey color = new NamespacedKey(Stones.getPlugin(Stones.class), "color");

    public static NamespacedKey getRoleKey(){
        return rolekey;
    }
    public static NamespacedKey getImmovablekey(){
        return immovablekey;
    }
    public static NamespacedKey getItemTypeKey(){
        return itemtypekey;
    }
    public static NamespacedKey getIsOnKey(){
        return ison;
    }
    public static NamespacedKey getColor(){
        return color;
    }

        // COLLECTIONS

    private static ArrayList<Player> movementchecklist = new ArrayList<>();
    private static HashMap<String, Inventory> inventorymap= new HashMap<>();
    public static ArrayList<Player> getMovementCheckList() {
        return movementchecklist;
    }
    public static HashMap<String, Inventory> getInventoryMap(){return inventorymap;}



}
