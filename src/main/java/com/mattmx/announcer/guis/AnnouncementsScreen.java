package com.mattmx.announcer.guis;

import com.mattmx.announcer.Announcer;
import com.mattmx.announcer.logic.AnnouncerManager;
import com.mattmx.announcer.logic.AnnouncerMessage;
import com.mattmx.announcer.util.VelocityChat;
import com.mattmx.announcer.util.gui.Enchantments;
import com.mattmx.announcer.util.gui.InventoryBuilder;
import com.mattmx.announcer.util.gui.ItemBuilder;
import com.velocitypowered.api.proxy.Player;
import dev.simplix.protocolize.api.inventory.InventoryClick;
import dev.simplix.protocolize.api.item.ItemStack;
import dev.simplix.protocolize.data.ItemType;
import dev.simplix.protocolize.data.inventory.InventoryType;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class AnnouncementsScreen extends InventoryBuilder {
    int page = 1;

    public void update() {
        clear();
        define(getPlayer());
        //updateItems();
        open();
    }

    @Override
    public void define(Player p) {
        super.define(p);
        this.setTitle(VelocityChat.color("&9&lAnnouncer &7» &fMenu &8[" + page + "]"));
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
        setItem(49, new ItemBuilder(ItemType.EMERALD).name(VelocityChat.color("&a&lCreate new")).lore(VelocityChat.color("&7Click to create a new announcement")).build());
        List<ItemStack> messages = new ArrayList<>();
        AnnouncerManager.getAll().forEach(m -> {
            messages.add(new ItemBuilder(ItemType.PAPER)
                    .name(VelocityChat.color("&9&lID &7» &7'&f" + m.getId() + "&7'"))
                    .lore(Component.text(""))
                    .lore(VelocityChat.color("&bSound &7» &f" + (m.getSound() == null ? "&cnothing" : m.getSound())))
                    .lore(VelocityChat.color("&bTitle &7» " + (m.getTitle() == null ? "&cnothing" : "'&f" + m.getTitle() + "&7'")))
                    .lore(VelocityChat.color("&bSubtitle &7» " + (m.getSubtitle() == null ? "&cnothing" : "'&f" + m.getSubtitle() + "&7'")))
                    .lore(VelocityChat.color("&bDelay &7» &c" + (m.getDelay() == -1 ? "&cdisabled" : "&cevery " + m.getDelay() + " seconds")))
                    .lore(VelocityChat.color("&bChat: " + (m.getLines().size() == 0 ? "&cnone" : "")))
                    .lore(m.getLines().stream().map(l -> VelocityChat.color("&7 - &f" + l)).collect(Collectors.toList()))
                    .lore(Component.text(""))
                    .lore(VelocityChat.color("&bRight Click &7to edit"))
                    .lore(VelocityChat.color("&bLeft Click &7to send manually"))
                    .lore(VelocityChat.color("&bDrop &7to delete"))
                    .enchantment(Enchantments.MENDING, 1)
                    .hideEnchantments(true).build());
        });
        for (int i = (page - 1) * 45; i < Math.min((page * 45), messages.size()); i++) {
            setItem(i - ((page - 1) * 45), messages.get(i));
        }
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
            // create new
            AnnouncerMessage msg = new AnnouncerMessage()
                    .setDelay(-1)
                    .setId("id_" + RandomStringUtils.randomAlphabetic(6));
            AnnouncerManager.add(msg);
            MessageScreen newScreen = new MessageScreen();
            newScreen.define(getPlayer(), msg);
            newScreen.open();
        } else if (e.clickedItem() != null) {
            if (e.clickedItem().itemType() == ItemType.PAPER) {
                // is a message, get the message clicked
                int index = e.slot() + ((page - 1) * 44);
                AnnouncerMessage msg = AnnouncerManager.getAll().get(index);
                if (msg != null) {
                    switch (e.clickType()) {
                        case RIGHT_CLICK -> {
                            // edit
                            MessageScreen newScreen = new MessageScreen();
                            newScreen.define(getPlayer(), msg);
                            newScreen.open();
                        }
                        case LEFT_CLICK ->  {
                            // send manually
                            Announcer.get().getServer().getAllPlayers().forEach(msg::execute);
                        }
                        case DROP -> {
                            AnnouncerManager.remove(msg);
                            update();
                            // delete
                        }
                    }
                }
            }
        }
    }

    private boolean canNext() {
        return AnnouncerManager.getAll().size() > (44 * page);
    }

    private boolean canPrev() {
        return page - 1 > 0;
    }
}
