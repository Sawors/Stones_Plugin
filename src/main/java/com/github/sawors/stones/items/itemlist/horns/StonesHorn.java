package com.github.sawors.stones.items.itemlist.horns;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.items.ItemTag;
import com.github.sawors.stones.items.StonesItem;
import com.github.sawors.stones.items.itemlist.instruments.StonesInstrument;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class StonesHorn extends StonesItem implements Listener {
    
    NamespacedKey hornsound;
    int cooldown = 20*4;
    int castlength;
    
    public StonesHorn() {
        super();
        
        setMaterial(Material.SHIELD);
        setDisplayName(Component.translatable(ChatColor.GRAY+formatTextToName(getClass().getSimpleName())));
        addTag(ItemTag.HORN);
        addTag(ItemTag.DISABLE_ORIGINAL_FUNCTION);
        hornsound = Sound.EVENT_RAID_HORN.getKey();
    }
    
    public void setHornSound(NamespacedKey soundkey){
        this.hornsound = soundkey;
    }

    public void setCastLength(int seconds){
        this.castlength = seconds;
    }

    public int getCastLength(){
        return this.castlength;
    }

    public NamespacedKey getHornSound(){
        return this.hornsound;
    }
    
    public void setCooldown(int cooldown){
        this.cooldown = cooldown;
    }
    
    public int getCooldown(){
        return this.cooldown;
    }
    
    public BukkitRunnable getEffectAction(Player blower){
        return new BukkitRunnable() {
            @Override
            public void run() {}
        };
    }
    
    @EventHandler
    public static void onPlayerBlowHorn(PlayerInteractEvent event){
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        
        if (event.getAction().isRightClick() && item.getType().equals(Material.SHIELD) && event.getPlayer().getCooldown(Material.SHIELD) <= 0 && Stones.getRegisteredItem(getItemId(item)) instanceof StonesHorn registeredhorn) {
            final String soundkey = StonesInstrument.getItemSoundType(item);
            Key key = NamespacedKey.fromString(soundkey);
            key = key != null ? key : Sound.EVENT_RAID_HORN.getKey();
            net.kyori.adventure.sound.Sound sound = net.kyori.adventure.sound.Sound.sound(key, net.kyori.adventure.sound.Sound.Source.PLAYER, UsefulThings.getVolume(24), 1);

            p.getWorld().playSound(sound, net.kyori.adventure.sound.Sound.Emitter.self());
            new BukkitRunnable() {
            
                final int max = registeredhorn.getCastLength() > 0 ? registeredhorn.getCastLength()*2 : 2;
                int countdown = max;
                final ItemStack hornitem = item.clone();
            
                @Override
                public void run() {
                    
                    // action to do when the player finished blowing
                    if (countdown <= 0 || Bukkit.getOnlinePlayers().isEmpty()) {
                        StonesItem hornobject = Stones.getRegisteredItem(getItemId(hornitem));
                        if(hornobject instanceof StonesHorn horndata){
                            horndata.getEffectAction(p).run();
                            p.setCooldown(Material.SHIELD, horndata.getCooldown());
                        }
                        this.cancel();
                        return;
                    }
                    if (countdown == max) {
                        if (hornitem.hasItemMeta() && getItemTags(hornitem).contains(ItemTag.HORN.tagString())) {
                            p.spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 3.25 + ((float) -countdown / max), 0), 4, 0.1, .25, 0.1, new Particle.DustOptions(Color.fromRGB(0x45171f), 1));
                        } else {
                            p.getWorld().stopSound(sound);
                            this.cancel();
                            return;
                        }
                    } else {
                        if (p.isBlocking() && hornitem.hasItemMeta() && getItemTags(hornitem).contains(ItemTag.HORN.tagString())) {
                            p.spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 3.25 + ((float) -countdown / max), 0), 4, 0.1, .25, 0.1, new Particle.DustOptions(Color.fromRGB(0x45171f), 1));
                        } else {
                            p.getWorld().stopSound(sound);
                            this.cancel();
                            return;
                        }
                    }
                
                    countdown--;
                }
            
            
            }.runTaskTimer(Stones.getPlugin(), 0, 10);
        }
    }
    
}
