package com.github.sawors.stones.siege;

import org.bukkit.block.Block;

public interface SiegeUnit {
    void shoot();
    void create(Block core);
    boolean check();
}
