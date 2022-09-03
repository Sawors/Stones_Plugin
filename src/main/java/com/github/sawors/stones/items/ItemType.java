package com.github.sawors.stones.items;

import java.util.Locale;

public enum ItemType {
    RING,
    INSTRUMENT,
        FLUTE,
        LYRE,
        GUITAR,
        DOUBLEBASS,
        HARP,
        KOTO,
        OUD,
        PANFLUTE,
        SITAR,
        BANJO,
        MOLOPHONE,
    HAT,
    UNMOVABLE,
    DAGGER,
    CURVED_DAGGER
    ;
    
    public String tagString(){
        return this.toString().toLowerCase(Locale.ROOT);
    }
}
