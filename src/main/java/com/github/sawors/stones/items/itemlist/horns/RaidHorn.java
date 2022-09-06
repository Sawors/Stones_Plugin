package com.github.sawors.stones.items.itemlist.horns;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.items.itemlist.instruments.StonesInstrument;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class RaidHorn extends StonesHorn{
    
    public RaidHorn() {
        super();
        addData(StonesInstrument.getSoundTypeKey(), Sound.EVENT_RAID_HORN.getKey().toString());
        setCooldown(45*20);
        setCastLength(4);
    }
    
    @Override
    public BukkitRunnable getEffectAction(Player blower) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                    if (!blower.getWorld().getNearbyPlayers(blower.getLocation(), 24).isEmpty()) {
                        for (Player player : blower.getWorld().getNearbyPlayers(blower.getLocation(), 24)) {
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 2, UsefulThings.randomPitchSimple(0.4, 1));
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 2, UsefulThings.randomPitchSimple(0.4, 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (((-blower.getLocation().distance(player.getLocation()) / 24 / 1.25) + 1) * 30 * 20), 0, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (((-blower.getLocation().distance(player.getLocation()) / 24 / 1.25) + 1) * 30 * 20), 0, false, false));
                            blower.setCooldown(Material.SHIELD, 20 * 8);
                        }
                    }
            }
        };
    }
    
    
}
