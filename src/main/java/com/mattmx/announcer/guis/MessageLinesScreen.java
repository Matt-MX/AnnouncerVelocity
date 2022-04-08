package com.mattmx.announcer.guis;

import com.mattmx.announcer.logic.AnnouncerMessage;
import com.mattmx.announcer.logic.ChatInputs;
import com.mattmx.announcer.logic.ChatManager;
import com.mattmx.announcer.util.VelocityChat;
import com.mattmx.announcer.util.gui.InventoryBuilder;
import com.mattmx.announcer.util.gui.ItemBuilder;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import dev.simplix.protocolize.api.ClickType;
import dev.simplix.protocolize.api.inventory.InventoryClick;
import dev.simplix.protocolize.api.item.ItemStack;
import dev.simplix.protocolize.data.ItemType;
import dev.simplix.protocolize.data.inventory.InventoryType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.stream.Collectors;

public class MessageLinesScreen extends InventoryBuilder {
    private MessageScreen parent;
    private int line = 0;

    public void define(Player p, MessageScreen parent) {
        super.define(p);
        this.clear();
        this.parent = parent;
        this.setTitle(VelocityChat.color("&9&lAnnouncer &7» &7Menu » &7" + parent.getMessage().getId() + " » &fLines"));
        this.type(InventoryType.GENERIC_9X6);
        for (int i = 45; i < 54; i++) {
            setItem(i, new ItemBuilder(ItemType.WHITE_STAINED_GLASS_PANE).name(Component.text("")).build());
        }
        setItem(53, new ItemBuilder(ItemType.BARRIER)
                .name(VelocityChat.color("&c&lReturn"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to return"),
                        Component.text("")
                }).build());
        for (int i = 0; i < Math.min(44, parent.getMessage().getLines().size()); i++) {
            String line = parent.getMessage().getLines().get(i);
            setItem(i, new ItemBuilder(ItemType.PAPER)
                    .name(VelocityChat.color("&b&lLine " + (i + 1)))
                    .lore(Component.text(""))
                    .lore(VelocityChat.color("&7Currently » '&f" + line.replace("<none>", " ") + "&7'"))
                    .lore(Component.text(line))
                    .lore(Component.text(""))
                    .lore(VelocityChat.color("&bClick &7to change"))
                    .lore(VelocityChat.color("&bDrop &7to remove"))
                    .lore(Component.text("")).build());
        }
        setItem(45, new ItemBuilder(ItemType.TNT)
                .name(VelocityChat.color("&c&lReset"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to reset all lines"),
                        Component.text("")
                }).build());
        setItem(49, new ItemBuilder(ItemType.EMERALD)
                .name(VelocityChat.color("&b&lAdd new line"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to add a new line"),
                        Component.text("")
                }).build());
    }

    @Override
    public void onChat(PlayerChatEvent e) {
        if (!e.getMessage().equalsIgnoreCase("cancel")) {
            parent.getMessage().setLine(line, e.getMessage());
        }
        ChatManager.remove(e.getPlayer());
        define(getPlayer(), parent);
        open();
    }

    @Override
    public void onClick(InventoryClick e) {
        if (e.slot() == 53) {
            parent.define(getPlayer(), parent.getMessage());
            parent.open();
        } else if (e.slot() == 45) {
            parent.getMessage().clearLines();
            define(getPlayer(), parent);
            open();
        } else if (e.slot() == 49) {
            parent.getMessage().addLine("null");
            line = parent.getMessage().getLines().size() - 1;
            close();
            ChatManager.add(getPlayer(), this);
            VelocityChat.clearChat(getPlayer());
            getPlayer().sendMessage(VelocityChat.color("&9&lCreate Line " + (line + 1)));
            getPlayer().sendMessage(VelocityChat.color("&bPlease provide a string"));
            getPlayer().sendMessage(VelocityChat.color("   &fExample: 'Hello World'"));
            getPlayer().sendMessage(VelocityChat.color("   &7Say &fcancel &7to cancel the operation."));
            getPlayer().sendMessage(VelocityChat.color("   &7Say &f<none> &7to have an empty line"));
        } else if (e.clickedItem() != null) {
            if (e.clickedItem().itemType() == ItemType.PAPER) {
                int lineIndex = e.slot();
                switch (e.clickType()) {
                    case LEFT_CLICK,RIGHT_CLICK -> {
                        line = lineIndex;
                        close();
                        ChatManager.add(getPlayer(), this);
                        VelocityChat.clearChat(getPlayer());
                        getPlayer().sendMessage(VelocityChat.color("&9&lChange Line " + (line + 1)));
                        getPlayer().sendMessage(VelocityChat.color("&7Currently » '&f" + parent.getMessage().getLines().get(line).replace("<none>", " ") + "&7' (Click to copy)")
                                .clickEvent(ClickEvent.suggestCommand(parent.getMessage().getLines().get(line))));
                        getPlayer().sendMessage(VelocityChat.color("&bPlease provide a string"));
                        getPlayer().sendMessage(VelocityChat.color("   &fExample: 'Hello World'"));
                        getPlayer().sendMessage(VelocityChat.color("   &7Say &fcancel &7to cancel the operation."));
                        getPlayer().sendMessage(VelocityChat.color("   &7Say &f<none> &7to have an empty line"));
                    }
                    case DROP -> {
                        parent.getMessage().removeLine(lineIndex);
                        define(getPlayer(), parent);
                        open();
                    }
                }
            }
        }
    }
}
