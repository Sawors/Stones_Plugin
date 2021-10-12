package com.github.sawors.stones;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.sawors.stones.commandexecutors.*;
import com.github.sawors.stones.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;


//https://www.section.io/engineering-education/minecraft-plugin-development-a-hands-on-crash-course/

public final class Stones extends JavaPlugin {



    @Override
    public void onEnable() {
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

        //      INITIATE PROTOCOLLIB
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();


        /*manager.addPacketListener(new PacketAdapter(Stones.getPlugin(Stones.class), ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {

            @Override
            public void onPacketSending(PacketEvent event){
                if(event.getPacketType() == PacketType.Play.Server.NAMED_SOUND_EFFECT){
                    event.setCancelled(true);
                }
            }
        });*/


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }




}
