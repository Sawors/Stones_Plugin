package com.github.sawors.stones.items;

import java.util.Locale;

public enum ItemTag {
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
    CURVED_DAGGER,
    ACTIVATED,
    DISABLED,
    HORN,
    DISABLE_ORIGINAL_FUNCTION
    ;
    
    public String tagString(){
        return this.toString().toLowerCase(Locale.ROOT);
    }
}
