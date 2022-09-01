package com.github.sawors.stones.items.itemlist.instruments;

import org.bukkit.Sound;

public class Banjo extends StonesInstrument{
    public Banjo() {
        super();
        addData(StonesInstrument.getSoundtypeKey(), Sound.BLOCK_NOTE_BLOCK_BANJO.getKey().toString());
    }
}
