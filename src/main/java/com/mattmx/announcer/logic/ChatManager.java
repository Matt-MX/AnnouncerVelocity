package com.mattmx.announcer.logic;

import com.mattmx.announcer.util.gui.InventoryBuilder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.HashMap;

public class ChatManager {
    private static HashMap<Player, InventoryBuilder> listening = new HashMap<>();

    public static void remove(Player p) {
        listening.remove(p);
    }

    public static void add(Player p, InventoryBuilder i) {
        listening.remove(p);
        listening.put(p, i);
    }

    public static InventoryBuilder get(Player p) {
        return listening.get(p);
    }

    @Subscribe
    public void chat(PlayerChatEvent e) {
        InventoryBuilder i = get(e.getPlayer());
        if (i != null) {
            e.setResult(PlayerChatEvent.ChatResult.denied());
            i.onChat(e);
        }
    }

    @Subscribe
    public void quit(DisconnectEvent e) {
        remove(e.getPlayer());
    }

    @Subscribe
    public void sw(ServerConnectedEvent e) {
        remove(e.getPlayer());
    }
}
