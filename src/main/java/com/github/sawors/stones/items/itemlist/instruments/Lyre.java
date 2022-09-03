package com.github.sawors.stones.items.itemlist.instruments;

import org.bukkit.Sound;

public class Lyre extends StonesInstrument{
    public Lyre() {
        super();
        addData(StonesInstrument.getSoundTypeKey(), Sound.BLOCK_NOTE_BLOCK_HARP.getKey().toString());
    }
}
