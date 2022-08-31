package com.github.sawors.stones.items.itemlist;

import com.github.sawors.stones.Stones;
import com.github.sawors.stones.items.StonesItem;
import org.bukkit.NamespacedKey;

public class MusicParchment extends StonesItem {
    public MusicParchment() {
        super();
    }
    
    public static NamespacedKey getMusicSheetKey(){
        return new NamespacedKey((Stones.getPlugin(Stones.class)), "musicnotes");
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
}
