package com.github.sawors.stones.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.github.sawors.stones.Stones;

public class PacketListenners {

        public void listenersAllPackets(){

            ProtocolManager manager = ProtocolLibrary.getProtocolManager();

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


}
