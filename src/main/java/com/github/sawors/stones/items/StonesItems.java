package com.github.sawors.stones.items;

import com.github.sawors.stones.UsefulThings.UsefulThings;
import com.github.sawors.stones.database.DataHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class StonesItems {
 
    @SuppressWarnings("deprecation")
    public static ItemStack get(StoneItem itemname){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        ArrayList<Component> lore = new ArrayList<>();
        switch(itemname){
            case PARCHMENT:
                item.setType(Material.PAPER);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Blank Parchment"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GOLD +"To sign it, crouch and use"));
                
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case MUSIC_PARCHMENT:
                item.setType(Material.PAPER);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Music Parchment"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                // LOTR : kp0000000000nn000000nnnnpg000000000000000000uuwwx0000000wwuuss000000uuwwru0000000000ds000000rb00pk0000000000nb000000kknnkp000000000000000000uuw0xa0000000wwuuks000000uu00pw0000000000ww
                // NGGYU : gillp00p00n00000gilin00n00l00ki00gilil000n0k00igg00n000lz
                // THE SHIRE : ikm000p000m000kki000000000k000p000r000u0t00000p0m00000nmk00000ikm000p000mk00i00ki000000000m000p000r00000p000m000k0000000k000000iki
            
            
                String notes = "gillp00p00n00000gilin00n00l00ki00gilil000n0k00igg00n000lz";
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, notes);
                lore = UsefulThings.noteToLore("Concerning Hobbits", notes.toCharArray(),10);
                lore.add(Component.text(""));
                
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case HAMMER:
                item.setType(Material.STICK);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Hammer"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case GOLD_RING:
                item.setType(Material.GOLD_NUGGET);
                meta.displayName(Component.translatable(ChatColor.GOLD + "Gold Ring"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "unique : " + ChatColor.MAGIC + (int)((Math.random()*10)-1) + (int)((Math.random()*10)-1)));
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GOLD+ ""+ ChatColor.ITALIC +"classic, stylish, never gets old"));
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "ring");
                break;
            case RESONANT_CRYSTAL:
                item.setType(Material.AMETHYST_SHARD);
                meta.displayName(Component.translatable(ChatColor.LIGHT_PURPLE + "Resonant Crystal"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.DARK_GRAY + "I MUST FIND A WAY TO CREATE CHANNELS IN LORE !"));
                
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case STRAWHAT:
                item.setType(Material.WHEAT);
                meta.displayName(Component.translatable(ChatColor.YELLOW + "Straw Hat"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text("Shift Click on air above your head to wear it"));
                
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat");
                break;
            case FEZ:
                item.setType(Material.RED_DYE);
                meta.displayName(Component.translatable(ChatColor.RED + "Fez"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text("Greetings traveler !"));
                
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat");
                break;
            case KIRBY:
                item.setType(Material.PINK_DYE);
                meta.displayName(Component.translatable(ChatColor.LIGHT_PURPLE + "Kirby"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text("A very special friend"));
                
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat");
                break;
            case OLAPAPI:
                item.setType(Material.GREEN_DYE);
                meta.displayName(Component.translatable(ChatColor.DARK_RED + "Hola ¿Que Tal?"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.DARK_PURPLE + "Hehehe, me no abla tacos"));
                
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat");
                break;
            case MONOCLE:
                item.setType(Material.GOLD_NUGGET);
                meta.displayName(Component.translatable(ChatColor.GOLD + "Monocle"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GOLD + "" + ChatColor.ITALIC + "Oh, hello !"));
                
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat"); // if item is a wearable. Possible wearables : head, chest, legs, feet, ring
                break;
            case DICE_6:
                item.setType(Material.FLINT);
                meta.displayName(Component.translatable(ChatColor.GOLD + "Dice 6"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.LIGHT_PURPLE + "1D6 yeah baby !"));
                
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "dice6");
                break;
            case IRON_DAGGER:
                item.setType(Material.IRON_SWORD);
                meta.displayName(Component.translatable(ChatColor.GRAY +  "Iron Dagger"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text(""));
                
                //meta.setUnbreakable(false);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "dagger"); // set item type if it's needed (optional) (useful for instruments etc...)meta.getPersistentDataContainer().set(ison, PersistentDataType.INTEGER, 0); // set if item is "on" (1) or "off" (0), useful if you want to make an actionable item (flashlight, resonant crystal, instrument)
                break;
            case IRON_CURVED_DAGGER:
                item.setType(Material.IRON_SWORD);
                meta.displayName(Component.translatable(ChatColor.GRAY +  "Iron Curved Dagger"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text(""));
                
                //meta.setUnbreakable(false);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "curveddagger"); // set item type if it's needed (optional) (useful for instruments etc...)meta.getPersistentDataContainer().set(ison, PersistentDataType.INTEGER, 0); // set if item is "on" (1) or "off" (0), useful if you want to make an actionable item (flashlight, resonant crystal, instrument)
                break;
            case HANDCUFFS:
                item.setType(Material.IRON_NUGGET);
                meta.displayName(Component.translatable(ChatColor.GRAY + "Handcuffs"));
                meta.setUnbreakable(true);
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GREEN + "Right Click at someone to prevent him from using items"));
                
                break;
            case BLINDFOLD:
                item.setType(Material.PAPER);
                meta.displayName(Component.translatable(ChatColor.GRAY + "Blindfold"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GREEN + "Right-click at someone or wear it to blind the wearer"));
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat"); // if item is a wearable. Possible wearables : head, chest, legs, feet, ring
                break;
            case BOUBOULE:
                item.setType(Material.GOLD_NUGGET);
                meta.displayName(Component.translatable(ChatColor.RED + "La Bouboule"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.LIGHT_PURPLE + "BAH ALORS PETIT COQUINOU !"));
                
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "hat"); // if item is a wearable. Possible wearables : head, chest, legs, feet, ring
                break;
            case SPOON:
                item.setType(Material.STICK);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Spoon"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case RAID_HORN:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.GRAY + "Raid Horn"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "horn");
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case IRON_SPEAR:
                item.setType(Material.STICK);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Iron Spear"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case WALL_BOMB:
                item.setType(Material.FIREWORK_STAR);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Wall Bomb"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY + "Charge : 0"));
                lore.add(Component.text(ChatColor.GRAY + "Fuse : 1s"));
                int[] values = {0, 20};
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY, values);
                
                break;
            case OAK_FLUTE:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oak Flute"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case OAK_LYRE:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oak Lyre"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case OAK_GUITAR:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oak Guitar"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case OAK_DOUBLEBASS:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oak Double-Bass"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case OAK_HARP:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oak Harp"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case OAK_KOTO:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oak Koto"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case OAK_OUD:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oak Oud"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case OAK_PANFLUTE:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oak Pan Flute"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case OAK_SITAR:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oak Sitar"));
                meta.setLocalizedName(itemname.toString().toLowerCase());;
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case OAK_BANJO:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oak Banjo"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case MOLOPHONE:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Molophone"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(DataHolder.getItemTypeKey(), PersistentDataType.STRING, "instrument");
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.STRING, "");
                //00g00i00l00l00p0000p0000n0000000g00i00l00i00n0000n0000l0000k00i0000g00i00l00i00l00000n000k0000i00g000g0000n00000lz
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"No music to be played"));
                lore.add(Component.text(""));
            
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
                break;
            case IRON_SICKLE:
                item.setType(Material.SHEARS);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Iron Sickle"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case THATCH:
                item.setType(Material.WHEAT);
                meta.displayName(Component.translatable(ChatColor.WHITE  + "Thatch"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case DANDELION_HEAD:
                item.setType(Material.YELLOW_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Dandelion Head"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case POPPY_HEAD:
                item.setType(Material.RED_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Poppy Head"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case BLUE_ORCHID_BUDS:
                item.setType(Material.LIGHT_BLUE_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Blue Orchid Buds"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case ALLIUM_HEAD:
                item.setType(Material.MAGENTA_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Allium Head"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case AZURE_BLUET_BUDS:
                item.setType(Material.LIGHT_GRAY_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Azure Bluet Buds"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case RED_TULIP_PETALS:
                item.setType(Material.RED_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Red Tulip Petals"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case ORANGE_TULIP_PETALS:
                item.setType(Material.ORANGE_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Orange Tulip Petals"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case WHITE_TULIP_PETALS:
                item.setType(Material.WHITE_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "White Tulip Petals"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case PINK_TULIP_PETALS:
                item.setType(Material.PINK_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Pink Tulip Petals"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case OXEYE_DAISY_HEAD:
                item.setType(Material.LIGHT_GRAY_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Oxeye Daisy Head"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case CORNFLOWER_HEAD:
                item.setType(Material.BLUE_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Cornflower Head"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case LILY_OF_THE_VALLEY_BUDS:
                item.setType(Material.WHITE_DYE);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Lily of the Valley Buds"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                break;
            case BOOK_TEST:
                item.setType(Material.BOOK);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Test Book 2"));
                meta.setLocalizedName("book_hey_check");
                lore.add(0, Component.text(ChatColor.GRAY + "Close"));
                int[] pages = {1, 16};
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY, pages);
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GOLD + " " + pages[0]+"/"+pages[1] + " pages"));
                break;
            case BOOK_IR:
                item.setType(Material.BOOK);
                meta.displayName(Component.translatable(ChatColor.WHITE + "IR"));
                meta.setLocalizedName("book_ir");
                lore.add(0, Component.text(ChatColor.GRAY + "Close"));
                int[] pages_tdt = {1, 24};
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY, pages_tdt);
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GOLD + " " + pages_tdt[0]+"/"+pages_tdt[1] + " pages"));
                break;
            case BOOK_PLAYBOY:
                item.setType(Material.BOOK);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Playboy"));
                meta.setLocalizedName("book_playboy");
                lore.add(0, Component.text(ChatColor.GRAY + "Close"));
                int[] pages_playboy = {1, 58};
                meta.getPersistentDataContainer().set(DataHolder.getStonesItemDataKey(), PersistentDataType.INTEGER_ARRAY, pages_playboy);
                lore.add(Component.text(""));
                lore.add(Component.text(ChatColor.GOLD + " " + pages_playboy[0]+"/"+pages_playboy[1] + " pages"));
                break;
            case WOOD_CRAYON:
                item.setType(Material.STICK);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Wood Crayon"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case TIME_SKIPPER:
                item.setType(Material.SHIELD);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Time Skipper"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC +"Dancing in the Time Time !"));
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case ROPE:
                item.setType(Material.STRING);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Rope"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case SEXTANT:
                item.setType(Material.COPPER_INGOT);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Sextant"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                break;
            case CANNON_BALL:
                item.setType(Material.FLINT);
                meta.displayName(Component.translatable(ChatColor.WHITE + "Cannon Ball"));
                meta.setLocalizedName(itemname.toString().toLowerCase());
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                
        
        }
        
        if(lore.size() > 0){
            meta.lore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack get(String item){
        return get(StoneItem.valueOf(item.toUpperCase()));
    }
    
    
}
