package com.github.sawors.stones.core.character;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.SerializeInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class StonesCharacter{
    
    UUID id;
     String name;
     int hp;
     int food;
     ItemStack[] inventory;
     ItemStack[] armor;
     ItemStack offhand;
     Location location;
     ArrayList<PotionEffect> potions;
     float xp;
     Location compasstarget;
     Location spawnpoint;
    
    
    public ItemStack[] getArmor() {
        return armor;
    }
    
    public void setArmor(ItemStack[] armor) {
        this.armor = armor;
    }
    
    public ItemStack getOffhand() {
        return offhand;
    }
    
    public void setOffhand(ItemStack offhand) {
        this.offhand = offhand;
    }
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getHp() {
        return hp;
    }
    
    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public int getFood() {
        return food;
    }
    
    public void setFood(int food) {
        this.food = food;
    }
    
    public ItemStack[] getInventory() {
        return inventory;
    }
    
    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public @NotNull ArrayList<PotionEffect> getPotions() {
        return potions;
    }
    
    public void setPotions(ArrayList<PotionEffect> potions) {
        this.potions = potions;
    }
    
    public float getXp() {
        return xp;
    }
    
    public void setXp(float xp) {
        this.xp = xp;
    }
    
    public Location getCompasstarget() {
        return compasstarget;
    }
    
    public void setCompasstarget(Location compasstarget) {
        this.compasstarget = compasstarget;
    }
    
    public Location getSpawnpoint() {
        return spawnpoint;
    }
    
    public void setSpawnpoint(Location spawnpoint) {
        this.spawnpoint = spawnpoint;
    }
    
     
    public StonesCharacter(UUID id, String name, int hp, int food, ItemStack[] inventory,ItemStack[] armor, ItemStack offhand, Location location, ArrayList<PotionEffect> pots, float xp, Location compasstarget, Location spawnpoint){
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.food = food;
        this.inventory = inventory;
        this.armor = armor;
        this.offhand = offhand;
        this.location = location;
        this.potions = pots;
        this.xp = xp;
        this.compasstarget = compasstarget;
        this.spawnpoint = spawnpoint;
    }
    
    public StonesCharacter(Player p){
        this.id = UUID.randomUUID();
        this.name = "Jean "+p.getName()+" "+((int)(Math.random()*10));
        this.hp = (int) p.getHealth();
        this.food = (int) p.getFoodLevel();
        this.inventory = p.getInventory().getContents();
        this.armor = p.getInventory().getArmorContents();
        this.offhand = p.getInventory().getItemInOffHand();
        this.location = p.getLocation();
        this.potions = new ArrayList<>(p.getActivePotionEffects());
        this.xp = p.getExp();
        this.compasstarget = p.getCompassTarget();
        this.spawnpoint = p.getBedSpawnLocation();
    }
    
    public void save(){
        
        File savefile = new File(Stones.getCharacterManager().getRootFile()+"/"+id+".yml");
        YamlConfiguration data = new YamlConfiguration();
        if(!savefile.exists()){
            try{
                savefile.createNewFile();
                data.createSection(SCharacterData.LAST_SEEN.toString());
                data.createSection(SCharacterData.ID.toString());
                data.createSection(SCharacterData.NAME.toString());
                data.createSection(SCharacterData.HP.toString());
                data.createSection(SCharacterData.FOOD.toString());
                data.createSection(SCharacterData.INVENTORY.toString());
                data.createSection(SCharacterData.ARMOR.toString());
                data.createSection(SCharacterData.OFFHAND.toString());
                data.createSection(SCharacterData.LOCATION.toString());
                data.createSection(SCharacterData.POTION_EFFECTS.toString());
                data.createSection(SCharacterData.XP.toString());
                data.createSection(SCharacterData.COMPASS_TARGET.toString());
                data.createSection(SCharacterData.SPAWN_LOCATION.toString());
                Stones.adminLog(savefile.getPath());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        try{
            if(savefile.exists()){
    
                data.set(SCharacterData.LAST_SEEN.toString(), LocalDateTime.now().toString());
                data.set(SCharacterData.ID.toString(), id.toString());
                data.set(SCharacterData.NAME.toString(), name);
                data.set(SCharacterData.HP.toString(), hp);
                data.set(SCharacterData.FOOD.toString(), food);
                try{
                    data.set(SCharacterData.INVENTORY.toString(), SerializeInventory.itemStackArrayToBase64(inventory));
                    data.set(SCharacterData.ARMOR.toString(), SerializeInventory.itemStackArrayToBase64(armor));
                    data.set(SCharacterData.OFFHAND.toString(), SerializeInventory.itemStackArrayToBase64(new ItemStack[]{offhand}));
                } catch (IllegalStateException | IllegalArgumentException e){
                
                }
                data.set(SCharacterData.LOCATION.toString(), location);
                data.set(SCharacterData.POTION_EFFECTS.toString(), potions);
                data.set(SCharacterData.XP.toString(), xp);
                data.set(SCharacterData.COMPASS_TARGET.toString(), compasstarget);
                data.set(SCharacterData.SPAWN_LOCATION.toString(), spawnpoint);
    
                data.save(savefile);
            }
        }catch (IOException exception){
            Bukkit.getLogger().log(Level.WARNING, "The Playerdata for this player could not be saved (path : "+Stones.getCharacterManager().getRootFile()+""+id+".yml)");
            exception.printStackTrace();
        }
    }
}
