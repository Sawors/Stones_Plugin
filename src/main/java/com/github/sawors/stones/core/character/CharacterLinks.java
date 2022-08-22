package com.github.sawors.stones.core.character;

import com.github.sawors.stones.core.database.HotDatabase;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

public class CharacterLinks implements HotDatabase, Listener {
    
    HashMap<UUID,UUID> linkMap;
    File chroot;
    private File chlinks;
    
    public CharacterLinks(File src){
        this.chroot = src;
        this.chlinks = new File(chroot+"/_links.json").getAbsoluteFile();
    }
    
    
    @Override
    public void load() {
    
        if(!chroot.exists()){
            chroot.mkdir();
        }
        if(!chlinks.exists()){
            try {
                chlinks.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        String mapjson = "";
        try{
            mapjson = new String(Files.readAllBytes(chlinks.getAbsoluteFile().toPath()));
        }catch(IOException excp){
            excp.printStackTrace();
        }
    
        if(mapjson.length() > 2){
            Gson gsonreader = new Gson();
            Type type = new TypeToken<HashMap<UUID,UUID>>(){}.getType();
            linkMap = gsonreader.fromJson(mapjson, type);
        }
    
    }
    
    @Override
    public void save() {
    
        if(!chroot.exists()){
            chroot.mkdir();
        }
        if(!chlinks.exists()){
            try {
                chlinks.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
        try(FileWriter writer = new FileWriter(chlinks.getAbsoluteFile())){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(linkMap, writer);
        } catch (IOException excp){
            excp.printStackTrace();
        }
    }
    
    public UUID getCharacter(Player p){
        return getCharacter(p.getUniqueId());
    }
    
    public UUID getCharacter(UUID p){
        return linkMap.get(p);
    }
}
