package com.github.sawors.stones;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.sawors.stones.commandexecutors.*;
import com.github.sawors.stones.listeners.*;
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

        //      INITIATE PROTOCOLLIB
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        //      RUNNABLES
        BukkitTask task = new LoopCheckRunnable(this).runTaskTimer(this, 20, 20);


        manager.addPacketListener(new PacketAdapter(Stones.getPlugin(Stones.class), ListenerPriority.NORMAL, PacketType.Play.Client.CHAT) {

            @Override
            public void onPacketReceiving(PacketEvent event){
                if(event.getPacketType() == PacketType.Play.Client.CHAT && event.getPlayer().getInventory().getHelmet() != null && event.getPlayer().getInventory().getHelmet().getItemMeta().getLocalizedName().equals("bouboule")){
                    //event.setCancelled(true);
                    PacketContainer packet = event.getPacket();
                    char[] message = packet.getStrings().read(0).toCharArray();
                    char[] newmessage = message.clone();
                    for(int i = 0; i< message.length; i++){
                        if(Character.isAlphabetic(message[i])){
                            if(Math.random() <= .25){
                                newmessage[i] = 'h';
                            } else{
                                newmessage[i] = 'm';
                            }
                        }
                    }
                    event.getPacket().getStrings().write(0, String.valueOf(newmessage));
                }
            }
        });


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Stones getPlugin(){
        return instance;
    }




}
