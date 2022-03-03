package com.github.sawors.stones.entitiy;

import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.UUID;

public class StonesEntities {
    private static HashMap<UUID, SEntity> entitymap = new HashMap<>();
    
    
    public static void registerEntity(UUID id, SEntity entitytype){
        if(!entitymap.containsKey(id)){
            entitymap.put(id, entitytype);
        }
    }
    
    public static boolean containsEntity(UUID id){
        return entitymap.containsKey(id);
    }
    
    public static SEntity getEntity(UUID id){
        return entitymap.get(id);
    }
    
    public static void removeEntityFromList(UUID id){
        entitymap.remove(id);
    }
    
    public static void entityRemove(Entity e){
        removeEntityFromList(e.getUniqueId());
        e.remove();
    }
}
