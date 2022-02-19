package com.github.sawors.stones.effects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.sawors.stones.Stones;
import com.github.sawors.stones.database.DataHolder;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class StonesEffects implements Listener {
    
    @EventHandler
    public void onEnable(PluginEnableEvent event){
        PacketAdapter muteeffect = new PacketAdapter(Stones.getPlugin(), ListenerPriority.NORMAL,
                PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                // Item packets (id: 0x29)
                if (event.getPacketType() == PacketType.Play.Server.CHAT && DataHolder.getEffectmap().containsKey(event.getPlayer().getUniqueId()) && DataHolder.getEffectmap().get(event.getPlayer().getUniqueId()).contains(StoneEffect.MUTED)) {
                    event.setCancelled(true);
                    PacketContainer packet = event.getPacket();
                    String message = packet.getStrings().read(0);
                    StringBuilder newmessage = new StringBuilder();
                    for(char c : message.toCharArray()){
                        if(Character.isAlphabetic(c)){
                            double random = Math.random();
                            if(random <= .5 && random > 0){
                                newmessage.append('m');
                            } else if(random <= .75 && random > .5){
                                newmessage.append('h');
                            } else {
                                newmessage.append(c);
                            }
                        } else  {
                            newmessage.append(c);
                        }
                    }
                    packet.getStrings().write(0, newmessage.toString());
                }
            }
        };
        
        Stones.getProtocolManager().addPacketListener(muteeffect);
    }
    
    public static void doEffects(){
    
        new BukkitRunnable(){
            @Override
            public void run(){
                for(int i = 0; i< DataHolder.getEffectmap().keySet().toArray().length; i++){
                    UUID id = (UUID) DataHolder.getEffectmap().keySet().toArray()[i];
                    if(Bukkit.getPlayer(id) != null && Objects.requireNonNull(Bukkit.getPlayer(id)).isOnline() && DataHolder.getEffectmap().containsKey(id)){
                        Player p = Bukkit.getPlayer(id);
                        ArrayList<StoneEffect> effectlist = DataHolder.effectmapGetEntry(id);
                        if(effectlist.size() > 0 && p != null){
                            for(StoneEffect effect : effectlist){
                                switch (effect){
                                    case CARRY:
                                        doCarry(p);
                                    case DEMON:
                                        doDemon(p);
                                }
                                
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Stones.getPlugin(), 0, 80);
        
        
    }
    
    
    public static void doCarry(Player p){
        if(p.getPassengers().size() > 0){
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, (int) Math.sqrt(4*p.getPassengers().get(0).getBoundingBox().getVolume()), false, false));
        } else {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 2, false, false));
        }
    }
    
    public static void doDemon(Player p){
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,90,0,false,false));
        new BukkitRunnable(){
            int count = 80;
            @Override
            public void run() {
                Vector eyes = new Vector(0,0,1).rotateAroundY(Math.toRadians(-p.getLocation().getYaw()));
                Vector lefteye = eyes.clone().multiply(0.2).add(eyes.clone().setX(-eyes.getZ()).setZ(eyes.getX()).multiply(.2));
                Vector righteye = eyes.clone().multiply(0.2).add(eyes.clone().setX(-eyes.getZ()).setZ(eyes.getX()).multiply(-.2));
                p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p.getLocation().clone().add(0,1,0),32,.2,.3,.2,0);
                p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p.getEyeLocation().add(lefteye),1,0,0,0,0);
                p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p.getEyeLocation().add(righteye),1,0,0,0,0);
                count--;
                if(count <= 0){
                    this.cancel();
                }
            }
        }.runTaskTimer(Stones.getPlugin(), 0, 2);
    }
    
    
}
