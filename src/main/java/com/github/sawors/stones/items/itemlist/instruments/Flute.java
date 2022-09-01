package com.github.sawors.stones.items.itemlist.instruments;

import org.bukkit.Sound;

public class Flute extends StonesInstrument{
    public Flute() {
        super();
        addData(StonesInstrument.getSoundtypeKey(), Sound.BLOCK_NOTE_BLOCK_FLUTE.getKey().toString());
    }
}
