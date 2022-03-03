package com.github.sawors.stones.player;

import com.github.sawors.stones.Stones;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class StonesPlayerData {
    static HashMap<UUID, ArrayList<SPlayerAction>> playerActionMap = new HashMap<>();
    
    public static void logAction(UUID id, SPlayerAction a, int duration, int cooldown){
        Stones.adminLog(playerActionMap.get(id));
        ArrayList<SPlayerAction> ar;
        if(playerActionMap.containsKey(id)){
            ar = playerActionMap.get(id);
        } else {
            playerActionMap.put(id, new ArrayList<>());
            ar = playerActionMap.get(id);
        }
        ar.add(a);
        Stones.adminLog(playerActionMap.get(id));
        if(duration > 0){
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(playerActionMap.containsKey(id)){
                        playerActionMap.get(id).remove(a);
                    }
                    Stones.adminLog(playerActionMap.get(id));
                }
            }.runTaskLater(Stones.getPlugin(), duration);
        }
        
        if(cooldown > 0){
            ar.add(SPlayerAction.COOLDOWN);
            new BukkitRunnable(){
                @Override
                public void run() {
                   if(playerActionMap.containsKey(id)){
                       playerActionMap.get(id).remove(SPlayerAction.COOLDOWN);
                   }
                    Stones.adminLog(playerActionMap.get(id));
                }
            }.runTaskLater(Stones.getPlugin(), cooldown);
        }
    }
    public static void logAction(UUID id, SPlayerAction a,int duration){
        logAction(id,a,duration,0);
    }
    public static void logAction(UUID id, SPlayerAction a){
        logAction(id,a,0,0);
    }
    public static void addCooldown(UUID id, int cooldown) {
        if(cooldown > 0){
            if(playerActionMap.containsKey(id)){
                playerActionMap.get(id).add(SPlayerAction.COOLDOWN);
            } else {
                ArrayList<SPlayerAction> ar = new ArrayList<>();
                ar.add(SPlayerAction.COOLDOWN);
                playerActionMap.put(id, ar);
            }
            Stones.adminLog(playerActionMap.get(id));
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(playerActionMap.containsKey(id)){
                        playerActionMap.get(id).remove(SPlayerAction.COOLDOWN);
                    }
                    Stones.adminLog(playerActionMap.get(id));
                }
            }.runTaskLater(Stones.getPlugin(), cooldown);
        }
    }
    
    
    public static ArrayList<SPlayerAction> getActions(UUID id){
        if(playerActionMap.containsKey(id)){
            return playerActionMap.get(id);
        } else {
            return new ArrayList<>();
        }
    }
    
    public static void clearActions(UUID id){
        playerActionMap.remove(id);
    }
    
    
    public static boolean hasAnyAction(UUID id){
        return playerActionMap.containsKey(id) && playerActionMap.get(id).size() > 0;
    }
    public static boolean hasAction(UUID id, SPlayerAction ac){
        return playerActionMap.containsKey(id)&&playerActionMap.get(id).contains(ac);
    }
}
