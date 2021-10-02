package com.github.sawors.stones;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FishSpawnRunnable extends BukkitRunnable {

    private final Plugin plugin;

    public FishSpawnRunnable(Plugin plugin) {
        //COMM. FROM SOURCE -> Get the main class that extends JavaPlugin so we can access config variables.
        this.plugin = plugin;
    }

    @Override
    public void run(){
        String name = plugin.getConfig().getString("world-name");
        World w = plugin.getServer().getWorld(name);

        w.spawnEntity(w.getSpawnLocation(), EntityType.COD);
    }
}
