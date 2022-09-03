package com.github.sawors.stones.items.itemlist.instruments;

import org.bukkit.Sound;

public class Guitar extends StonesInstrument{
    public Guitar() {
        super();
        addData(StonesInstrument.getSoundTypeKey(), Sound.BLOCK_NOTE_BLOCK_GUITAR.getKey().toString());
    }
}
