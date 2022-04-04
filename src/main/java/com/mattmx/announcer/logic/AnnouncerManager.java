package com.mattmx.announcer.logic;

import com.mattmx.announcer.Announcer;
import com.mattmx.announcer.util.Config;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AnnouncerManager {
    private static List<AnnouncerMessage> messages = new ArrayList<>();
    private static long timer;

    public static void add(AnnouncerMessage msg) {
        messages.add(msg);
    }

    public static void remove(AnnouncerMessage msg) {
        messages.remove(msg);
        FileConfiguration config = Config.DATA;
        config.set(msg.getId(), null);
        try {
            config.save(Config.DATA_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeId(String id) {
        new ArrayList<>(messages).forEach(m -> {
            if (m.getId().equalsIgnoreCase(id)) {
                remove(m);
            }
        });
    }

    public static List<AnnouncerMessage> getAll() {
        return messages;
    }

    public static void init() {
        Announcer.get().getServer().getScheduler().buildTask(Announcer.get(), () -> {
            timer++;
            new ArrayList<>(messages).forEach(m -> {
                if (m.getDelay() != -1) {
                    if (timer % m.getDelay() == 0) {
                        Announcer.get().getServer().getAllPlayers().forEach(m::execute);
                    }
                }
            });
        }).repeat(1, TimeUnit.SECONDS).schedule();
    }

    public static void save(AnnouncerMessage msg) {
        FileConfiguration config = Config.DATA;
        config.set(msg.getId(), null);
        config.set(msg.getId() + ".title", msg.getTitle());
        config.set(msg.getId() + ".subtitle", msg.getSubtitle());
        config.set(msg.getId() + ".sound", msg.getSound());
        config.set(msg.getId() + ".delay", msg.getDelay());
        config.set(msg.getId() + ".chat", msg.getLines());
        try {
            config.save(Config.DATA_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        messages.clear();
        FileConfiguration config = Config.DATA;
        if (config.getConfigurationSection("").getKeys(false) != null) {
            for (String id : config.getConfigurationSection("").getKeys(false)) {
                AnnouncerMessage msg = new AnnouncerMessage();
                if (config.getString(id + ".title") != null) msg.setTitle(config.getString(id + ".title"));
                if (config.getString(id + ".subtitle") != null) msg.setSubtitle(config.getString(id + ".subtitle"));
                if (config.getString(id + ".sound") != null) msg.setSound(config.getString(id + ".sound"));
                if (config.getString(id + ".delay") != null) msg.setDelay(config.getInt(id + ".delay"));
                if (config.getStringList(id + ".chat") != null) msg.setLines(config.getStringList(id + ".chat"));
                msg.setId(id);
                messages.add(msg);
            }
        }
    }
}
