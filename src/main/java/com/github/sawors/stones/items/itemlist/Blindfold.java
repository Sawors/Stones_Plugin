package com.github.sawors.stones.items.itemlist;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.events.PlayerWearHatEvent;
import com.github.sawors.stones.items.ItemType;
import com.github.sawors.stones.items.StonesItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Blindfold extends StonesItem implements Listener {
    public Blindfold() {
        super();
        
        setMaterial(Material.PAPER);
        setDisplayName(Component.translatable(ChatColor.GRAY + "Blindfold"));
        
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.GREEN + "Right-click at someone or wear it to blind the wearer"));
        
        addTag(ItemType.HAT);
    
        setLore(lore);
    }
    
    @EventHandler
    public void onPlayerClickEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(event.getRightClicked() instanceof Player rightclicked && getItemId(item).equals(new Blindfold().getTypeId())){
            if(UsefulThings.isBehind(player, rightclicked, 45) && rightclicked.getInventory().getHelmet() == null){
                rightclicked.getInventory().setHelmet(item);
                // calling wear hat event
                PlayerWearHatEvent hatevent = new PlayerWearHatEvent(rightclicked, item, true);
                Bukkit.getPluginManager().callEvent(hatevent);
                player.getInventory().getItemInMainHand().setAmount(item.getAmount()-1);
            }
        }
    }
    
    @EventHandler
    public static void addBlindWhenWearing(PlayerWearHatEvent event){
        if(getItemId(event.getItem()).equals(new Blindfold().getTypeId())){
            if(event.isPuttingHat()){
                addBlindEffect(event.getPlayer());
            } else {
                event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
            }
        }
    }
    
    public static void addBlindEffect(Player p){
        new BukkitRunnable(){
            @Override
            public void run() {
                ItemStack helmet = p.getInventory().getHelmet();
                if(helmet != null && helmet.hasItemMeta() && getItemId(helmet).equals(new Blindfold().getTypeId())){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,60,1,false,false));
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Stones.getPlugin(), 0,40);
    }
}
