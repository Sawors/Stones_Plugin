package com.github.sawors.stones;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.sawors.stones.commandexecutors.*;
import com.github.sawors.stones.listeners.*;
import com.github.sawors.stones.recipes.StonecutterRecipes;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


//https://www.section.io/engineering-education/minecraft-plugin-development-a-hands-on-crash-course/

public final class Stones extends JavaPlugin {

    private static Stones instance;

    @Override
    public void onEnable() {
        //get plugin instance (FINALLY I FOUND HOW)
        instance = this;
        // Plugin startup logic
        saveDefaultConfig();
        //      REGISTER EVENTS
        getServer().getPluginManager().registerEvents(new ListenersALL(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerConnect(), this);
        getServer().getPluginManager().registerEvents(new AnvilListeners(), this);
        getServer().getPluginManager().registerEvents(new MovementListener(), this);
        getServer().getPluginManager().registerEvents(new AttackListeners(), this);
        getServer().getPluginManager().registerEvents(new DeathManager(), this);
        getServer().getPluginManager().registerEvents(new ForbiddenMoveItemListener(), this);
        getServer().getPluginManager().registerEvents(new SpecialEntityListeners(), this);
        getServer().getPluginManager().registerEvents(new BackpackManager(), this);
        getServer().getPluginManager().registerEvents(new StonecutterRecipes(), this);

        //      LOAD COMMANDS
        getServer().getPluginCommand("mark-location").setExecutor(new TowerCommandExecutor());
        getServer().getPluginCommand("worldspawn-mobs").setExecutor(new WorldspawnCommandExecutor());
        getServer().getPluginCommand("vvoid").setExecutor(new VvoidCommand());
        getServer().getPluginCommand("sgive").setExecutor(new SgiveCommandExecutor());
        getServer().getPluginCommand("getname").setExecutor(new NameCommandExecutor());
        getServer().getPluginCommand("ignite").setExecutor(new IgniteCommandExecutor());
        getServer().getPluginCommand("checklist").setExecutor(new MovementCheckListCommandExecutor());
        getServer().getPluginCommand("sit").setExecutor(new SitCommandExecutor());
        getServer().getPluginCommand("handcuff").setExecutor(new HandcuffCommandExecutor());
        getServer().getPluginCommand("uncuff").setExecutor(new UncuffCommandExecutor());
        getServer().getPluginCommand("heal").setExecutor(new HealCommandExecutor());
        getServer().getPluginCommand("sspawn").setExecutor(new SspawnCommandExecutor());
        getServer().getPluginCommand("stest").setExecutor(new StestCommandExecutor());
        getServer().getPluginCommand("sdebug").setExecutor(new SdebugCommandExecutor());

        //      INITIATE PROTOCOLLIB
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        //      RUNNABLES
        BukkitTask task = new LoopCheckRunnable(this).runTaskTimer(this, 20, 20);





    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Stones getPlugin(){
        return instance;
    }




}
