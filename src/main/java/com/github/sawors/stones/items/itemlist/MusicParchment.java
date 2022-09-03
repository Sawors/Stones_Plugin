package com.github.sawors.stones.items.itemlist;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.items.StonesItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;

public class MusicParchment extends StonesItem {
    
    String musicname = "Unnamed";
    String musicnotes = "Empty";
    
    public MusicParchment() {
        super();
    
        setMaterial(Material.PAPER);
    
        addData(getMusicSheetKey(), buildMusicSheet(musicname,musicnotes));
        
        ArrayList<Component> lore = noteToLore(musicname, musicnotes.toCharArray(), 10);
        setLore(lore);
    }
    
    public MusicParchment(String musicname, String notes){
        super();
    
        setMaterial(Material.PAPER);
        
        this.musicname = musicname;
        this.musicnotes = notes;
    
        addData(getMusicSheetKey(), buildMusicSheet(musicname,musicnotes));
    
        ArrayList<Component> lore = noteToLore(musicname, musicnotes.toCharArray(), 10);
        setLore(lore);
    }
    
    public void setMusicName(String name){
        this.musicname = name;
        setVariant(musicname);
    }
    
    // modify this to add more data to music sheets (like eventually color, tempo, etc...)
    public static String buildMusicSheet(String name, String music){
        return "[" +
                "name=" + name.replaceAll(",", "") +
                ","+
                "content=" + music.replaceAll(" ", "") +
                "]";
    }
    
    public static String getMusicName(String sheetdata){
        StringBuilder namebuilder = new StringBuilder("Unnamed");
        if(sheetdata.charAt(0) == '[' && sheetdata.charAt(sheetdata.length()-1) == ']' && sheetdata.contains("name=")){
            namebuilder = new StringBuilder();
            for(char c : sheetdata.substring(sheetdata.indexOf("name=")+5).toCharArray()){
                if(c == ',' || c == ']'){
                    break;
                }
                namebuilder.append(c);
            }
        }
        
        return namebuilder.toString();
    }
    
    public static String getMusicNotes(String sheetdata){
        StringBuilder namebuilder = new StringBuilder("Unnamed");
        if(sheetdata.charAt(0) == '[' && sheetdata.charAt(sheetdata.length()-1) == ']' && sheetdata.contains("content=")){
            namebuilder = new StringBuilder();
            for(char c : sheetdata.substring(sheetdata.indexOf("content=")+8).toCharArray()){
                if(c == ',' || c == ']'){
                    break;
                }
                namebuilder.append(c);
            }
            
        }
    
        return namebuilder.toString();
    }
    
    public static ArrayList<Component> noteToLore(String title, char[] note, int linelength){
        
        ArrayList<Component> lore = new ArrayList<>();
        if(title.length() > 1){
            char[] titlechar = title.toCharArray();
            StringBuilder linetitle = new StringBuilder();
            int titlelength = linelength + linelength + 3;
            
            lore.add(Component.text(""));
            linetitle.append("   ");
            for(int i0=0; i0<titlelength; i0++){
                if(titlechar.length > i0){
                    linetitle.append(titlechar[i0]);
                }
            }
            lore.add(Component.text(ChatColor.GOLD + ""+linetitle));
            lore.add(Component.text(""));
        }
        
        for(int i = 1; i<=(note.length/linelength)+1; i++){
            StringBuilder line = new StringBuilder();
            for(int i2 = 0; i2 < linelength; i2++){
                if(i2*i < note.length){
                    line.append(Character.toUpperCase(note[i2 * i]));
                }
                line.append(" ");
            }
            
            lore.add(Component.text(ChatColor.GOLD + " "+i+"    "+ChatColor.BLUE+line+" "));
        }
        return lore;
    }
    
    public static ArrayList<Component> noteToLore(char[] note, int linelength){
        return noteToLore("", note, linelength);
    }
    
    
    public static NamespacedKey getMusicSheetKey(){
        return new NamespacedKey((Stones.getPlugin(Stones.class)), "musicnotes");
    }
}
