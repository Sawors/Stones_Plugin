package com.github.sawors.stones;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.sawors.stones.books.StonesBooks;
import com.github.sawors.stones.combat.CombatListeners;
import com.github.sawors.stones.commands.*;
import com.github.sawors.stones.database.DataHolder;
import com.github.sawors.stones.death.DeathManager;
import com.github.sawors.stones.effects.StonesEffects;
import com.github.sawors.stones.entitiy.SpecialEntityListeners;
import com.github.sawors.stones.items.HandcuffCommandExecutor;
import com.github.sawors.stones.items.IgniteCommandExecutor;
import com.github.sawors.stones.items.StonesWeapons;
import com.github.sawors.stones.listeners.*;
import com.github.sawors.stones.magic.ChatController;
import com.github.sawors.stones.magic.MagicExecutor;
import com.github.sawors.stones.magic.StonesBodyParts;
import com.github.sawors.stones.recipes.AnvilListeners;
import com.github.sawors.stones.recipes.StonecutterRecipes;
import com.github.sawors.stones.siege.StonesSiege;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.sql.Time;
import java.time.LocalTime;
import java.util.UUID;
import java.util.logging.Level;


public final class Stones extends JavaPlugin {

    private static Stones instance;
    private static Team t;
    public static ProtocolManager manager;

    @Override
    public void onEnable() {
        //get plugin instance (FINALLY I FOUND HOW)
        instance = this;
        // Plugin startup logic
        saveDefaultConfig();
        
        saveResource("documentation/books/how-to-write-books.txt", true);
        saveResource("documentation/books/template/_close.json", true);
        saveResource("documentation/books/template/_close.properties", true);
        saveResource("documentation/books/template/_cover.png", true);
        saveResource("documentation/books/template/PAGE_TEMPLATE.xcf", true);
        saveResource("documentation/books/template/PAGENUMBER.json", true);
        saveResource("documentation/books/template/PAGENUMBER.properties", true);
        saveResource("documentation/music/how-to-create-music.txt", true);
    
        
        //      REGISTER EVENTS
        getServer().getPluginManager().registerEvents(new ListenersALL(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerConnect(), this);
        getServer().getPluginManager().registerEvents(new AnvilListeners(), this);
        getServer().getPluginManager().registerEvents(new MovementListener(), this);
        getServer().getPluginManager().registerEvents(new CombatListeners(), this);
        getServer().getPluginManager().registerEvents(new DeathManager(), this);
        getServer().getPluginManager().registerEvents(new ForbiddenMoveItemListener(), this);
        getServer().getPluginManager().registerEvents(new SpecialEntityListeners(), this);
        getServer().getPluginManager().registerEvents(new StonecutterRecipes(), this);
        getServer().getPluginManager().registerEvents(new FishingListeners(), this);
        getServer().getPluginManager().registerEvents(new StonesBooks(), this);
        getServer().getPluginManager().registerEvents(new StonesWeapons(), this);
        getServer().getPluginManager().registerEvents(new StonesBodyParts(), this);
        getServer().getPluginManager().registerEvents(new StonesSiege(), this);
        getServer().getPluginManager().registerEvents(new ChatController(), this);
        getServer().getPluginManager().registerEvents(new MagicExecutor(), this);

        //      LOAD COMMANDS
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
        getServer().getPluginCommand("playmusic").setExecutor(new PlayMusicCommandExecutor());

        //      INITIATE PROTOCOLLIB
        manager = ProtocolLibrary.getProtocolManager();

        //      RUNNABLES

        //      SCOREBOARDS
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        t = scoreboard.getTeam("hide_name");
        if(t == null){
          t = scoreboard.registerNewTeam("hide_name");
          t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
          t.setCanSeeFriendlyInvisibles(false);
        }
        
        
        //      GAMERULES
        for(World w : Bukkit.getWorlds()){
            w.setGameRule(GameRule.NATURAL_REGENERATION, false);
        }
    
    
        StonesEffects.doEffects();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DataHolder.triggerRemoveList();
        
    }

    public static Stones getPlugin(){
        return instance;
    }
    
    public static Team getHideNameTeam(){
        return t;
    }
    
    public static ProtocolManager getProtocolManager(){
        return manager;
    }
    
    public static void adminLog(Object msg){
        try{
            Bukkit.getPlayer(UUID.fromString("f96b1fab-2391-4c41-b6aa-56e6e91950fd")).sendMessage("["+ Time.valueOf(LocalTime.now()) + "] "+msg.toString());
        } catch (NullPointerException exception){
            try{
                Bukkit.getLogger().log(Level.INFO, msg.toString());
            } catch (NullPointerException wellwearefucked){
                Bukkit.getLogger().log(Level.INFO, "null");
            }
        }
    }
}
