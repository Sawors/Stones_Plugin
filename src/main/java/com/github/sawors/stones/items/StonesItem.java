package com.github.sawors.stones.items;

import com.github.sawors.stones.Stones;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;


// When creating a new object NEVER FORGET to register it in main class Stones on onEnable using Stones.registerItem(StonesItem item)
public abstract class StonesItem {
    Component name;
    ArrayList<Component> lore;
    HashSet<String> types;
    String id;
    boolean unique;
    Material basematerial;
    
    public StonesItem(){
        String classname = this.getClass().getSimpleName();
        StringBuilder idformated = new StringBuilder();
        char lastchar = '/';
        for(char c : classname.toCharArray()){
            if(Character.isUpperCase(c) && Character.isLowerCase(lastchar)){
                idformated.append("_");
            }
            idformated.append(Character.toLowerCase(c));
            lastchar = c;
        }
        id = idformated.toString();
        types = new HashSet<>();
        lore = new ArrayList<>();
        
        name = Component.text(id);
        basematerial = Material.ROTTEN_FLESH;
        unique = false;
    }
    
    public String getId(){
        return id;
    }
    
    public void setDisplayName(Component name){
        this.name = name;
    }
    public void setId(String id){
        this.id = id;
    }
    
    public void setLore(ArrayList<Component> lore){
        this.lore.addAll(lore);
    }
    
    public void setUnique(boolean unique){
        this.unique = unique;
    }
    
    public void setMaterial(Material material){
        if(material.isItem()){
            this.basematerial = material;
        }
    }
    
    public void addType(ItemType type){
        types.add(type.toString().toLowerCase(Locale.ROOT));
    }
    
    public ItemStack get(){
        ItemStack item = new ItemStack(basematerial);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(name);
        meta.lore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.getPersistentDataContainer().set(StonesItem.getItemIdKey(), PersistentDataType.STRING, id.toLowerCase(Locale.ROOT));
        StringBuilder typeskey = new StringBuilder();
        for(String s : types){
            typeskey.append(s.toUpperCase(Locale.ROOT)).append(":");
        }
        if(typeskey.toString().endsWith(":")){
            typeskey.deleteCharAt(typeskey.lastIndexOf(":"));
        }
        meta.getPersistentDataContainer().set(StonesItem.getItemTagsKey(), PersistentDataType.STRING, typeskey.toString().toLowerCase(Locale.ROOT));
        
        item.setItemMeta(meta);
        
        return item;
    }
    
    
    public String getPersistentDataPrint(){
        PersistentDataContainer container = this.get().getItemMeta().getPersistentDataContainer();
        StringBuilder printed = new StringBuilder();
        
        printed
                .append("Item ID : ").append(container.get(StonesItem.getItemIdKey(), PersistentDataType.STRING))
                .append("\n")
                .append("Item Tags : ").append("[")
        ;
        String typestr = container.get(StonesItem.getItemTagsKey(), PersistentDataType.STRING);
        if(typestr != null){
            if(typestr.contains(":")){
                for(String str : typestr.split(":")){
                    printed.append("\n  - ").append(str);
                }
            } else {
                printed.append(typestr);
            }
            
            printed.append("\n");
        }
        printed.append("]");
        
        return printed.toString();
    }
    
    public static NamespacedKey getItemIdKey(){
        return new NamespacedKey((Stones.getPlugin(Stones.class)), "id");
    }
    
    public static NamespacedKey getItemTagsKey(){
        return new NamespacedKey((Stones.getPlugin(Stones.class)), "itemtype");
    }
    
    public static String getItemId(ItemStack item){
        String id = item.getType().toString().toLowerCase(Locale.ROOT);;
        if(item.hasItemMeta()){
            String checkid = item.getItemMeta().getPersistentDataContainer().get(getItemIdKey(), PersistentDataType.STRING);
            if(checkid != null){
                id = checkid;
            }
        }
        
        return id;
    }
}
