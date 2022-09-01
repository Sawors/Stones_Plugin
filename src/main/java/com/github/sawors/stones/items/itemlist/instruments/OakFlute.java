package com.github.sawors.stones.items.itemlist.instruments;

import org.bukkit.Sound;

public class OakFlute extends StonesInstrument{
    public OakFlute() {
        super();
        addData(StonesInstrument.getSoundtypeKey(), Sound.BLOCK_NOTE_BLOCK_FLUTE.getKey().toString());
    }
}
