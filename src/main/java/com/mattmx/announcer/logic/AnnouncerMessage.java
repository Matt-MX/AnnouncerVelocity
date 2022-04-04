package com.mattmx.announcer.logic;

import com.mattmx.announcer.Announcer;
import com.mattmx.announcer.util.VelocityChat;
import com.velocitypowered.api.proxy.Player;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.SoundCategory;
import dev.simplix.protocolize.api.inventory.PlayerInventory;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import dev.simplix.protocolize.data.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;

import java.time.Duration;
import java.util.*;

public class AnnouncerMessage {
    private String id;
    private List<String> lines = new ArrayList<>();
    private String title = null;
    private String subtitle = null;
    private String sound = null;
    private int delay;

    public void execute(Player p) {
            Title title = Title.title(
                    this.title == null ? Component.text("") : VelocityChat.color(this.title),
                    this.subtitle == null ? Component.text("") : VelocityChat.color(this.subtitle),
                    Title.Times.of(Ticks.duration(10), Ticks.duration(40), Ticks.duration(10))
            );
            p.showTitle(title);
            if (Announcer.hasProtocolize() && sound != null) {
                ProtocolizePlayer protocolizePlayer = Protocolize.playerProvider().player(p.getUniqueId());
                protocolizePlayer.playSound(Sound.valueOf(sound), SoundCategory.MASTER, 1f, 1f);
            }
            if (lines != null) {
                lines.forEach(l -> p.sendMessage(VelocityChat.color(l)));
            }
    }

    public String getId() {
        return id;
    }

    public AnnouncerMessage setId(String id) {
        this.id = id;
        return this;
    }

    public List<String> getLines() {
        return lines;
    }

    public AnnouncerMessage setLines(List<String> lines) {
        this.lines = lines;
        return this;
    }

    public AnnouncerMessage removeLine(int i) {
        this.lines.remove(i);
        return this;
    }

    public AnnouncerMessage addLine(String content) {
        this.lines.add(content);
        return this;
    }

    public AnnouncerMessage setLine(int i, String content) {
        this.lines.set(i, content);
        return this;
    }

    public void clearLines() {
        this.lines.clear();
    }

    public String getTitle() {
        return title;
    }

    public AnnouncerMessage setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public AnnouncerMessage setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public String getSound() {
        return sound;
    }

    public AnnouncerMessage setSound(String sound) {
        this.sound = sound;
        return this;
    }

    public int getDelay() {
        return delay;
    }

    public AnnouncerMessage setDelay(int delay) {
        this.delay = delay;
        return this;
    }
}
