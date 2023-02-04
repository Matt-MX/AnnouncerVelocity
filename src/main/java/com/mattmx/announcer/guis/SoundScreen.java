package com.mattmx.announcer.guis;

import com.mattmx.announcer.OldAnnouncer;
import com.mattmx.announcer.logic.ChatManager;
import com.mattmx.announcer.util.VelocityChat;
import com.mattmx.announcer.util.gui.Enchantments;
import com.mattmx.announcer.util.gui.InventoryBuilder;
import com.mattmx.announcer.util.gui.ItemBuilder;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import dev.simplix.protocolize.api.ClickType;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.SoundCategory;
import dev.simplix.protocolize.api.inventory.InventoryClick;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import dev.simplix.protocolize.data.ItemType;
import dev.simplix.protocolize.data.Sound;
import dev.simplix.protocolize.data.inventory.InventoryType;
import net.kyori.adventure.text.Component;

import java.util.*;

public class SoundScreen extends InventoryBuilder {
    int page = 1;
    MessageScreen parent;
    boolean searching;
    String search;
    List<Sound> results = new ArrayList<>();

    public void update() {
        clear();
        if (searching) {
            search(getPlayer());
        } else {
            define(getPlayer(), parent);
        }
        open();
    }

    public void define(Player p, MessageScreen parent) {
        super.define(p);
        if (searching) {
            search(p);
            return;
        }
        this.parent = parent;
        this.setTitle(VelocityChat.color("&9&lAnnouncer &7» &7Menu » " + parent.getMessage().getId() + " » &fSound &8[" + (page) + "&8]"));
        this.type(InventoryType.GENERIC_9X6);
        for (int i = 45; i < 54; i++) {
            setItem(i, new ItemBuilder(ItemType.WHITE_STAINED_GLASS_PANE).name(Component.text("")).build());
        }
        if (canNext()) {
            setItem(53, new ItemBuilder(ItemType.ARROW).name(VelocityChat.color("&9&lNext Page")).build());
        }
        if (canPrev()) {
            setItem(45, new ItemBuilder(ItemType.ARROW).name(VelocityChat.color("&9&lPrevious Page")).build());
        }
        setItem(49, new ItemBuilder(ItemType.COMPASS).name(VelocityChat.color("&6&lSearch")).lore(VelocityChat.color("&7Click to search for a sound")).build());
        for (int i = (page - 1) * 45; i < Math.min((page * 45), Sound.values().length); i++) {
            Sound sound = Sound.values()[i];
            ItemBuilder b = new ItemBuilder(ItemType.MUSIC_DISC_BLOCKS)
                    .hideEnchantments(true)
                    .clearLores()
                    .name(VelocityChat.color("&f" + sound))
                    .lore(Component.text(""))
                    .lore(VelocityChat.color("&bLeft Click &7to select this sound"))
                    .lore(VelocityChat.color("&bRight Click &7to preview this sound"))
                    .lore(Component.text(""));
            if (sound.toString().equalsIgnoreCase(parent.getMessage().getSound())) {
                b.enchantment(Enchantments.MENDING, 1).hideEnchantments(true).name(VelocityChat.color("&a" + sound)).clearLores()
                        .lore(Component.text(""))
                        .lore(VelocityChat.color("&a&lCurrently Selected"))
                        .lore(Component.text(""))
                        .type(ItemType.MUSIC_DISC_CAT);
            }
            setItem(i - ((page - 1) * 45), b.build());
        }
    }

    @Override
    public void onChat(PlayerChatEvent e) {
        if (!e.getMessage().equalsIgnoreCase("cancel")) {
            page = 1;
            searching = true;
            search = e.getMessage();

            results.clear();
            results = new ArrayList<>();
            for (Sound s : Sound.values()) {
                if (s.name().toUpperCase().contains(search.toUpperCase().replace(" ", "_"))) {
                    results.add(s);
                }
            }
        } else {
            page = 1;
            searching = false;
            search = null;
        }
        ChatManager.remove(e.getPlayer());
        define(getPlayer(), parent);
        open();
    }

    @Override
    public void onClick(InventoryClick e) {
        if (e.slot() == 45 && e.clickedItem().itemType() == ItemType.ARROW) {
            page--;
            update();
        } else if (e.slot() == 53 && e.clickedItem().itemType() == ItemType.ARROW) {
            page++;
            update();
        } else if (e.slot() == 49) {
            if (e.clickedItem().itemType() == ItemType.COMPASS) {
                close();
                ChatManager.add(getPlayer(), this);
                VelocityChat.clearChat(getPlayer());
                getPlayer().sendMessage(VelocityChat.color("&9&lSpecify a sound to search for"));
                getPlayer().sendMessage(VelocityChat.color("&bPlease provide a string"));
                getPlayer().sendMessage(VelocityChat.color("   &7Say &fcancel &7to cancel the operation."));
            } else {
                page = 1;
                searching = false;
                search = null;
                define(getPlayer(), parent);
                open();
            }
        } else if (e.clickedItem() != null) {
            if (e.clickedItem().itemType() == ItemType.MUSIC_DISC_BLOCKS) {
                int index = e.slot() + ((page - 1) * 45);
                Sound sound = Sound.values()[index];
                if (searching) {
                    sound = results.get(index);
                }
                if (e.clickType() == ClickType.LEFT_CLICK) {
                    if (sound != null) {
                        parent.getMessage().setSound(sound.name());
                    }
                    parent.define(getPlayer(), parent.getMessage());
                    parent.open();
                } else {
                    if (OldAnnouncer.hasProtocolize() && sound != null) {
                        ProtocolizePlayer protocolizePlayer = Protocolize.playerProvider().player(getPlayer().getUniqueId());
                        protocolizePlayer.playSound(sound, SoundCategory.MASTER, 1f, 1f);
                    }
                }
            }
        }
    }

    private boolean canNext() {
        return (searching ? results.size() > (44 * page) : Sound.values().length > (44 * page));
    }

    private boolean canPrev() {
        return page - 1 > 0;
    }

    public void search(Player p) {
        super.define(p);
        this.clear();
        this.setTitle(VelocityChat.color("&9&lAnnouncer &7» &7Menu » " + parent.getMessage().getId() + " » &fSound &8[" + search + "&8]"));
        this.type(InventoryType.GENERIC_9X6);
        for (int i = 45; i < 54; i++) {
            setItem(i, new ItemBuilder(ItemType.WHITE_STAINED_GLASS_PANE).name(Component.text("")).build());
        }
        if (canNext()) {
            setItem(53, new ItemBuilder(ItemType.ARROW).name(VelocityChat.color("&9&lNext Page")).build());
        }
        if (canPrev()) {
            setItem(45, new ItemBuilder(ItemType.ARROW).name(VelocityChat.color("&9&lPrevious Page")).build());
        }
        setItem(49, new ItemBuilder(ItemType.BARRIER).name(VelocityChat.color("&6&lCancel Search")).lore(VelocityChat.color("&7Click to cancel the search")).build());
        for (int i = (page - 1) * 45; i < Math.min((page * 45), results.size()); i++) {
            Sound sound = results.get(i);
            ItemBuilder b = new ItemBuilder(ItemType.MUSIC_DISC_BLOCKS)
                    .hideEnchantments(true)
                    .clearLores()
                    .name(VelocityChat.color("&f" + sound))
                    .lore(Component.text(""))
                    .lore(VelocityChat.color("&bClick &7to select this sound"))
                    .lore(Component.text(""));
            if (sound.toString().equalsIgnoreCase(parent.getMessage().getSound())) {
                b.enchantment(Enchantments.MENDING, 1).hideEnchantments(true).name(VelocityChat.color("&a" + sound)).clearLores()
                        .lore(Component.text(""))
                        .lore(VelocityChat.color("&a&lCurrently Selected"))
                        .lore(Component.text(""))
                        .type(ItemType.MUSIC_DISC_CAT);
            }
            setItem(i - ((page - 1) * 45), b.build());
        }
    }
}