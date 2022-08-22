package com.github.sawors.stones.UsefulThings;

import org.bukkit.block.Block;

public class StonesMaterial {
    public static boolean isWoodFullBlock(Block b){
        return b.getType().toString().contains("PLANKS") || b.getType().toString().contains("LOG") || b.getType().toString().contains("STEM");
    }
}
