package com.github.sawors.stones;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.sawors.stones.books.StonesBooks;
import com.github.sawors.stones.combat.CombatListeners;
import com.github.sawors.stones.combat.StonesWeapons;
import com.github.sawors.stones.commands.*;
import com.github.sawors.stones.core.character.CharacterManager;
import com.github.sawors.stones.core.database.DataHolder;
import com.github.sawors.stones.core.death.DeathManager;
import com.github.sawors.stones.core.player.StonesPlayerAdditions;
import com.github.sawors.stones.core.player.StonesPlayerSit;
import com.github.sawors.stones.core.recipes.AnvilListeners;
import com.github.sawors.stones.core.recipes.SickleRecipes;
import com.github.sawors.stones.core.recipes.StonecutterRecipes;
import com.github.sawors.stones.core.recipes.TreeCuttingRecipes;
import com.github.sawors.stones.effects.StonesEffects;
import com.github.sawors.stones.entity.SpecialEntityListeners;
import com.github.sawors.stones.items.StonesItem;
import com.github.sawors.stones.items.itemlist.*;
import com.github.sawors.stones.items.itemlist.horns.RaidHorn;
import com.github.sawors.stones.items.itemlist.instruments.*;
import com.github.sawors.stones.items.itemlist.weapons.CurvedDagger;
import com.github.sawors.stones.items.itemlist.weapons.StraightDagger;
import com.github.sawors.stones.items.itemlist.wearable.*;
import com.github.sawors.stones.listeners.*;
import com.github.sawors.stones.magic.ChatController;
import com.github.sawors.stones.magic.MagicExecutor;
import com.github.sawors.stones.siege.StonesSiege;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;


public final class Stones extends JavaPlugin {

    private static Stones instance;
    private static Team t;
    public static ProtocolManager manager;
    private static final CharacterManager chmanager = new CharacterManager(new File("./characters").getAbsoluteFile());
    private static HashMap<String, StonesItem> itemmap = new HashMap<>();
    private static HashSet<Integer> registeredlisteners = new HashSet<>();
    
    

    public static CharacterManager getCharacterManager() {
        return chmanager;
    }

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
    
        //      REGISTER ITEMS
        registerItem(new GoldRing());
        registerItem(new MusicParchment());
        registerItem(new BlankParchment());
        registerItem(new Hammer());
        registerItem(new Handcuffs());
        registerItem(new WoodCrayon());
        registerItem(new Spoon());
        registerItem(new Rope());
        registerItem(new Sextant());
        //hats
        registerItem(new StrawHat());
        registerItem(new Fez());
        registerItem(new Kirby());
        registerItem(new Monocle());
        registerItem(new Sombrero());
        registerItem(new Blindfold());
        //horns
        registerItem(new RaidHorn());
        //weapons
        registerItem(new StraightDagger());
        registerItem(new CurvedDagger());
        //music instruments
        registerItem(new Flute());
        registerItem(new Banjo());
        registerItem(new Doublebass());
        registerItem(new Guitar());
        registerItem(new Koto());
        registerItem(new Lyre());
        registerItem(new Molophone());
        registerItem(new Oud());
        registerItem(new Panflute());
        registerItem(new Sitar());
        
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
        getServer().getPluginManager().registerEvents(new StonesSiege(), this);
        getServer().getPluginManager().registerEvents(new ChatController(), this);
        getServer().getPluginManager().registerEvents(new MagicExecutor(), this);
        getServer().getPluginManager().registerEvents(new TreeCuttingRecipes(), this);
        getServer().getPluginManager().registerEvents(new StonesPlayerAdditions(), this);
        getServer().getPluginManager().registerEvents(new StonesPlayerSit(), this);
        getServer().getPluginManager().registerEvents(new SickleRecipes(), this);

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
        getServer().getPluginCommand("recall").setExecutor(new RecallCommandExecutor());

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
        
        //  CHARACTERS MAP
        chmanager.loadLinks();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DataHolder.triggerRemoveList();
        
        chmanager.saveLinks();
        
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
    
    public static void logAdmin(Object msg){

        String output = "["+ ChatColor.YELLOW+"DEBUG"+ChatColor.WHITE+"-"+ Time.valueOf(LocalTime.now()) + "] "+msg;
        Bukkit.getLogger().log(Level.INFO, output);
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.isOp()){
                p.sendMessage(Component.text(output));
            }
        }
    }

    private void registerItem(StonesItem item){
        itemmap.put(item.getId(), item);


        if(item instanceof Listener listener){
            for(Method method : listener.getClass().getMethods()){
                if(!registeredlisteners.contains(method.hashCode()) && method.getAnnotation(EventHandler.class) != null && method.getParameters().length >= 1 && Event.class.isAssignableFrom(method.getParameters()[0].getType())){
                    // method is recognized as handling an event
                    /*
                    plugin -> parameter
                    listener -> parameter
                    for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader().createRegisteredListeners(listener, plugin).entrySet()) {
                        getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
                    }
                    */
                    logAdmin("Listener found : "+method.getName());
                    Class<? extends Event> itemclass = method.getParameters()[0].getType().asSubclass(Event.class);

                    getServer().getPluginManager().registerEvent(itemclass, listener, method.getAnnotation(EventHandler.class).priority(), EventExecutor.create(method, itemclass),getPlugin());
                    registeredlisteners.add(method.hashCode());
                }
            }
        }
    }

    private void registerListeners(StonesItem item){
        if(item instanceof Listener itemlistener){
            getServer().getPluginManager().registerEvents(itemlistener, this);
        }
    }
    
    public static StonesItem getRegisteredItem(String id){
        return itemmap.get(id);
    }
    
    private void loadItems(){
    
    }
    
    public static Inventory getItemListDisplay(){
        Inventory itemsview = Bukkit.createInventory(null, 6*9, Component.text("Item List"));
        for(StonesItem item : itemmap.values()){
            itemsview.addItem(item.get());
        }
        
        return itemsview;
    }
}
