package com.github.sawors.stones.core.character;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.SerializeInventory;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class CharacterManager{
    
    File chroot;
    CharacterLinks chlinksmap;
    
    public CharacterManager(File chroot){
        this.chroot = chroot;
        this.chlinksmap = new CharacterLinks(chroot);
    }
    
    public void saveLinks(){
        chlinksmap.save();
    }
    
    public void loadLinks(){
        chlinksmap.load();
    }
    
    public File getRootFile(){
        return this.chroot;
    }
    
    public void saveCharacter(StonesCharacter ch){
            ch.save();
    }
    
    public StonesCharacter loadCharacter(String id) throws FileNotFoundException {
        YamlConfiguration conf = new YamlConfiguration();
        File src = new File(chroot.getAbsolutePath()+"/"+id+".yml");
        if(src.exists()){
            try{
                conf.load(src);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            
            ArrayList<PotionEffect> potionlist = new ArrayList<>();
            if(conf.getList(SCharacterData.POTION_EFFECTS.toString()) == null || conf.getList(SCharacterData.POTION_EFFECTS.toString()).size() > 0){
                for(Object ef : Objects.requireNonNull(conf.getList(SCharacterData.POTION_EFFECTS.toString()))){
                    potionlist.add((PotionEffect) ef);
                }
            }
    
            ItemStack[] inventory = new ItemStack[]{};
            if(Objects.requireNonNull(conf.getString(SCharacterData.INVENTORY.toString())).length() >4){
                try{
                    inventory = SerializeInventory.itemStackArrayFromBase64(conf.getString(SCharacterData.INVENTORY.toString()));
                } catch (IOException | IllegalArgumentException exception) {
                    exception.printStackTrace();
                }
            }
            
            ItemStack[] armor = new ItemStack[]{};
            if(Objects.requireNonNull(conf.getString(SCharacterData.ARMOR.toString())).length() >4){
                try{
                    armor = SerializeInventory.itemStackArrayFromBase64(conf.getString(SCharacterData.ARMOR.toString()));
                } catch (IOException | IllegalArgumentException exception) {
                    exception.printStackTrace();
                }
            }
            
            ItemStack offhand = new ItemStack(Material.AIR);
            if(Objects.requireNonNull(conf.getString(SCharacterData.OFFHAND.toString())).length() >4){
                Stones.logAdmin(conf.getString(SCharacterData.OFFHAND.toString()));
                try{
                    offhand = SerializeInventory.itemStackArrayFromBase64(conf.getString(SCharacterData.OFFHAND.toString()))[0];
                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (IllegalArgumentException e2){
                
                }
            }
            
            return new StonesCharacter(
                    conf.getObject(SCharacterData.ID.toString(), UUID.class),
                    conf.getString(SCharacterData.NAME.toString()),
                    conf.getInt(SCharacterData.HP.toString()),
                    conf.getInt(SCharacterData.FOOD.toString()),
                    inventory,
                    armor,
                    offhand,
                    conf.getLocation(SCharacterData.LOCATION.toString()),
                    potionlist,
                    (float) conf.getDouble(SCharacterData.XP.toString()),
                    conf.getLocation(SCharacterData.COMPASS_TARGET.toString()),
                    conf.getLocation(SCharacterData.SPAWN_LOCATION.toString())
            );
        } else {
            Stones.logAdmin(src);
            throw new FileNotFoundException("data file for this id could not be found");
        }
    }
    
}
