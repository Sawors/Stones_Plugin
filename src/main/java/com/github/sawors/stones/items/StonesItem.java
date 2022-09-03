package com.github.sawors.stones.items;

import com.github.sawors.stones.Stones;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;


// When creating a new object NEVER FORGET to register it in main class Stones on onEnable using Stones.registerItem(StonesItem item)
public abstract class StonesItem {
    Component name;
    ArrayList<Component> lore;
    HashSet<String> types;
    String id;
    boolean unique;
    Material basematerial;
    HashMap<NamespacedKey, String> additionaldata = new HashMap<>();
    
    public StonesItem(){
        String classname = this.getClass().getSimpleName();
        id = getTypeId();
        types = new HashSet<>();
        lore = new ArrayList<>();
        
        
        StringBuilder nameformated = new StringBuilder();
        char lastchar = '/';
        for(char c : classname.toCharArray()){
            if(Character.isUpperCase(c) && Character.isLowerCase(lastchar)){
                nameformated.append(" ");
            }
            nameformated.append(c);
            lastchar = c;
        }
        name = Component.translatable(ChatColor.WHITE + nameformated.toString());
        
        basematerial = Material.ROTTEN_FLESH;
        unique = false;
    }
    
    public String getId(){
        return id;
    }
    
    public String getTypeId(){
        StringBuilder idformated = new StringBuilder();
        char lastchar = '/';
        for(char c : getClass().getSimpleName().toCharArray()){
            if(Character.isUpperCase(c) && Character.isLowerCase(lastchar)){
                idformated.append("_");
            }
            idformated.append(Character.toLowerCase(c));
            lastchar = c;
        }
        return idformated.toString();
    }
    
    public void addData(@NotNull NamespacedKey key, String data){
        additionaldata.put(key,data);
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
    
    public void addTag(ItemType type){
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
        for(Map.Entry<NamespacedKey, String> entry : additionaldata.entrySet()){
            meta.getPersistentDataContainer().set(entry.getKey(), PersistentDataType.STRING,entry.getValue());
        }
        
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
    public static List<String> getItemTags(ItemStack item){
        List<String> tags = List.of();
        if(item.hasItemMeta()){
            String checktags = item.getItemMeta().getPersistentDataContainer().get(getItemTagsKey(), PersistentDataType.STRING);
            if(checktags != null){
                if(checktags.contains(":")){
                    tags = List.of(checktags.split(":"));
                } else {
                    tags = List.of(checktags);
                }
            }
        }
        return tags;
    }
}
