package com.mattmx.announcer.guis;

import com.mattmx.announcer.Announcer;
import com.mattmx.announcer.logic.*;
import com.mattmx.announcer.util.VelocityChat;
import com.mattmx.announcer.util.gui.Enchantments;
import com.mattmx.announcer.util.gui.InventoryBuilder;
import com.mattmx.announcer.util.gui.ItemBuilder;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import dev.simplix.protocolize.api.ClickType;
import dev.simplix.protocolize.api.inventory.InventoryClick;
import dev.simplix.protocolize.api.item.ItemStack;
import dev.simplix.protocolize.data.ItemType;
import dev.simplix.protocolize.data.inventory.InventoryType;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MessageScreen extends InventoryBuilder {
    private AnnouncerMessage message;
    private ChatInputs listening;

    public void define(Player p, AnnouncerMessage message) {
        super.define(p);
        this.message = message;
        this.setTitle(VelocityChat.color("&9&lAnnouncer &7» &7Menu » &f" + message.getId()));
        this.type(InventoryType.GENERIC_9X6);
        for (int i = 45; i < 54; i++) {
            setItem(i, new ItemBuilder(ItemType.WHITE_STAINED_GLASS_PANE).name(Component.text("")).build());
        }
        setItem(45, new ItemBuilder(ItemType.SCUTE)
                .name(VelocityChat.color("&a&lSave"))
                .lore(Component.text(""))
                .lore(VelocityChat.color("&bClick&7 to save"))
                .lore(Component.text(""))
                .build());
        setItem(49, new ItemBuilder(ItemType.FIREWORK_ROCKET)
                .name(VelocityChat.color("&6&lPreview"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to preview your"),
                        VelocityChat.color("&7announcement for yourself."),
                        Component.text("")
                }).build());
        setItem(53, new ItemBuilder(ItemType.BARRIER)
                .name(VelocityChat.color("&c&lReturn"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to return"),
                        Component.text("")
                }).build());
        setItem(10, new ItemBuilder(ItemType.MOJANG_BANNER_PATTERN)
                .hideEnchantments(true)
                .clearLores()
                .name(VelocityChat.color("&9&lChat Lines"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&7Currently:"),
                })
                .lore(message.getLines().stream().map(m -> VelocityChat.color("&7 - &f" + m.replace("<none>", " "))).collect(Collectors.toList()))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to change"),
                        Component.text("")
                }).build());
        setItem(19, new ItemBuilder(ItemType.REDSTONE)
                .name(VelocityChat.color("&9&lTitle"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&7Currently » " + (message.getTitle() == null ? "&cnothing" : "'&f" + message.getTitle() + "&7'")),
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to change"),
                        VelocityChat.color("&bDrop&7 to reset"),
                        Component.text("")
                }).build());
        setItem(28, new ItemBuilder(ItemType.GUNPOWDER)
                .name(VelocityChat.color("&9&lSub-Title"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&7Currently » " + (message.getSubtitle() == null ? "&cnothing" : "'&f" + message.getSubtitle() + "&7'")),
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to change"),
                        VelocityChat.color("&bDrop&7 to reset"),
                        Component.text("")
                }).build());
        setItem(22, new ItemBuilder(ItemType.PAPER)
                .name(VelocityChat.color("&9&lAnnouncement ID"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&7Currently » '&f" + message.getId() + "&7'"),
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to change"),
                        Component.text("")
                }).build());
        setItem(16, new ItemBuilder(ItemType.CLOCK)
                .name(VelocityChat.color("&9&lDelay"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&7Currently » " + (message.getDelay() == null ? "&cdisabled" : "&cevery " +
                                message.getDelay().toString().replace(" ", "-")
                                + " seconds")),
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to change"),
                        VelocityChat.color("&bDrop&7 to disable"),
                        Component.text("")
                }).build());
        setItem(34, new ItemBuilder(ItemType.JUKEBOX)
                .name(VelocityChat.color("&9&lSound"))
                .lores(new TextComponent[] {
                        Component.text(""),
                        VelocityChat.color("&7Currently » &f" + (message.getSound() == null ? "&cnothing" : message.getSound())),
                        Component.text(""),
                        VelocityChat.color("&bClick&7 to change"),
                        VelocityChat.color("&bDrop&7 to reset"),
                        Component.text("")
                }).build());
    }

    @Override
    public void onChat(PlayerChatEvent e) {
        if (listening == ChatInputs.DELAY) {
            if (Delay.isValid(e.getMessage())) {
                message.setDelay(new Delay(e.getMessage()));
                ChatManager.remove(e.getPlayer());
                define(getPlayer(), message);
                open();
            } else if (e.getMessage().equalsIgnoreCase("-1")) {
                message.setDelay(null);
                ChatManager.remove(e.getPlayer());
                define(getPlayer(), message);
                open();
            } else {
                VelocityChat.clearChat(e.getPlayer());
                e.getPlayer().sendMessage(VelocityChat.color("&9&lChange Delay &7(seconds)"));
                e.getPlayer().sendMessage(VelocityChat.color("&bPlease provide a valid &lDelay"));
                e.getPlayer().sendMessage(VelocityChat.color("   &7Example: '&f2&7' or '&f2 20&7' for a random delay between &f2 &7and &f20 &7seconds."));
                e.getPlayer().sendMessage(VelocityChat.color("   &7Set to &f-1 &7to disable."));
            }
        } else {
            if (!e.getMessage().equalsIgnoreCase("cancel")) {
                if (listening == ChatInputs.TITLE) message.setTitle(e.getMessage());
                if (listening == ChatInputs.SUB_TITLE) message.setSubtitle(e.getMessage());
                if (listening == ChatInputs.ID) {
                    String old = message.getId();
                    message.setId(e.getMessage().toLowerCase().replace(" ", "_"));
                    AnnouncerManager.removeId(old);
                    AnnouncerManager.save(message);
                }
                define(getPlayer(), message);
            }
            ChatManager.remove(e.getPlayer());
            open();
        }
    }

    @Override
    public void onClick(InventoryClick e) {
        switch (e.slot()) {
            case 45 -> {
                //save
                getPlayer().sendMessage(VelocityChat.color("&a&lSaving..."));
                AnnouncerManager.save(message);
                getPlayer().sendMessage(VelocityChat.color("&a&lSaved..."));
            }
            case 49 -> {
                // preview
                close();
                message.execute(getPlayer());
                Announcer.get().getServer().getScheduler().buildTask(Announcer.get(), this::open).delay(3, TimeUnit.SECONDS).schedule();
            }
            case 53 -> {
                //return
                AnnouncementsScreen screen = new AnnouncementsScreen();
                screen.define(getPlayer());
                screen.open();
            }
            case 10 -> {
                //chat lines
                MessageLinesScreen screen = new MessageLinesScreen();
                screen.define(getPlayer(), this);
                screen.open();
            }
            case 19 -> {
                //title
                if (e.clickType() == ClickType.DROP) {
                    message.setTitle(null);
                    define(getPlayer(), message);
                    open();
                } else {
                    close();
                    listening = ChatInputs.TITLE;
                    ChatManager.add(getPlayer(), this);
                    VelocityChat.clearChat(getPlayer());
                    getPlayer().sendMessage(VelocityChat.color("&9&lChange Title"));
                    getPlayer().sendMessage(VelocityChat.color("&bPlease provide a string"));
                    getPlayer().sendMessage(VelocityChat.color("   &fExample: 'Hello World'"));
                    getPlayer().sendMessage(VelocityChat.color("   &7Say &fcancel &7to cancel the operation."));
                }
            }
            case 28 -> {
                //subtitle
                if (e.clickType() == ClickType.DROP) {
                    message.setSubtitle(null);
                    define(getPlayer(), message);
                    open();
                } else {
                    close();
                    listening = ChatInputs.SUB_TITLE;
                    ChatManager.add(getPlayer(), this);
                    VelocityChat.clearChat(getPlayer());
                    getPlayer().sendMessage(VelocityChat.color("&9&lChange Sub-Title"));
                    getPlayer().sendMessage(VelocityChat.color("&bPlease provide a string"));
                    getPlayer().sendMessage(VelocityChat.color("   &fExample: 'Hello World'"));
                    getPlayer().sendMessage(VelocityChat.color("   &7Say &fcancel &7to cancel the operation."));
                }
            }
            case 22 -> {
                //id
                close();
                listening = ChatInputs.ID;
                ChatManager.add(getPlayer(), this);
                VelocityChat.clearChat(getPlayer());
                getPlayer().sendMessage(VelocityChat.color("&9&lChange Announcement ID"));
                getPlayer().sendMessage(VelocityChat.color("&bPlease provide a string"));
                getPlayer().sendMessage(VelocityChat.color("   &fExample: 'hello_world'"));
                getPlayer().sendMessage(VelocityChat.color("   &7Say &fcancel &7to cancel the operation."));
            }
            case 16 -> {
                //delay
                if (e.clickType() == ClickType.DROP) {
                    message.setDelay(null);
                    define(getPlayer(), message);
                    open();
                } else {
                    close();
                    listening = ChatInputs.DELAY;
                    ChatManager.add(getPlayer(), this);
                    VelocityChat.clearChat(getPlayer());
                    getPlayer().sendMessage(VelocityChat.color("&9&lChange Delay &7(seconds)"));
                    getPlayer().sendMessage(VelocityChat.color("&bPlease provide a valid &lDelay"));
                    getPlayer().sendMessage(VelocityChat.color("   &7Example: '&f2&7' or '&f2 20&7' for a random delay between &f2 &7and &f20 &7seconds."));
                    getPlayer().sendMessage(VelocityChat.color("   &7Set to &f-1 &7to disable."));
                }
            }
            case 34 -> {
                //sound
                if (e.clickType() == ClickType.DROP) {
                    message.setSound(null);
                    define(getPlayer(), message);
                    open();
                } else {
                    SoundScreen sounds = new SoundScreen();
                    sounds.define(getPlayer(), this);
                    sounds.open();
                }
            }
        }
    }

    public AnnouncerMessage getMessage() {
        return message;
    }
}
