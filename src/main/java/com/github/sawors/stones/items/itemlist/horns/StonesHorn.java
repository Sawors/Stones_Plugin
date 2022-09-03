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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class StonesHorn extends StonesItem implements Listener {
    
    NamespacedKey hornsound;
    
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
    
    public NamespacedKey getHornSound(){
        return this.hornsound;
    }
    
    public BukkitRunnable getEffectAction(){
        return new BukkitRunnable() {
            @Override
            public void run() {}
        };
    }
    
    @EventHandler
    public static void onPlayerBlowHorn(PlayerInteractEvent event){
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        
        if (event.getAction().isRightClick() && item.getType().equals(Material.SHIELD) && event.getPlayer().getCooldown(Material.SHIELD) <= 0 && getItemTags(p.getInventory().getItemInMainHand()).contains(ItemTag.HORN.tagString())) {
            final String soundkey = StonesInstrument.getItemSoundType(item);
            Key key = NamespacedKey.fromString(soundkey);
            key = key != null ? key : Sound.EVENT_RAID_HORN.getKey();
            net.kyori.adventure.sound.Sound sound = net.kyori.adventure.sound.Sound.sound(key, net.kyori.adventure.sound.Sound.Source.PLAYER, UsefulThings.getVolume(24), 1);
            
            
            p.getWorld().playSound(sound, net.kyori.adventure.sound.Sound.Emitter.self());
            new BukkitRunnable() {
            
                final int max = 8;
                int countdown = max;
            
                @Override
                public void run() {
                
                    if (countdown <= 0 || Bukkit.getOnlinePlayers().isEmpty()) {
                        if (!p.getWorld().getNearbyPlayers(p.getLocation(), 24).isEmpty()) {
                            for (Player player : p.getWorld().getNearbyPlayers(p.getLocation(), 24)) {
                                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 2, UsefulThings.randomPitchSimple(0.4, 1));
                                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 2, UsefulThings.randomPitchSimple(0.4, 1));
                                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (((-p.getLocation().distance(player.getLocation()) / 24 / 1.25) + 1) * 30 * 20), 0, false, false));
                                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (((-p.getLocation().distance(player.getLocation()) / 24 / 1.25) + 1) * 30 * 20), 0, false, false));
                                p.setCooldown(Material.SHIELD, 20 * 8);
                            }
                        }
                        this.cancel();
                        return;
                    }
                    if (countdown == max) {
                        if (event.hasItem() && event.getItem().hasItemMeta() && Objects.equals(event.getItem().getItemMeta().getLocalizedName(), "raid_horn")) {
                            p.spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 3.25 + ((float) -countdown / max), 0), 4, 0.1, .25, 0.1, new Particle.DustOptions(Color.fromRGB(0x45171f), 1));
                        } else {
                            p.getWorld().stopSound(sound);
                            this.cancel();
                            return;
                        }
                    } else {
                        if (p.isBlocking() && event.hasItem() && event.getItem().hasItemMeta() && Objects.equals(event.getItem().getItemMeta().getLocalizedName(), "raid_horn")) {
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
