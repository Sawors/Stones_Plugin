package com.github.sawors.stones.items.itemlist.horns;

import com.github.sawors.stones.items.itemlist.instruments.StonesInstrument;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class RaidHorn extends StonesHorn{
    
    public RaidHorn() {
        super();
        addData(StonesInstrument.getSoundTypeKey(), Sound.EVENT_RAID_HORN.getKey().toString());
    }
    
    @Override
    public BukkitRunnable getEffectAction() {
        return super.getEffectAction();
    }
    
    
}
