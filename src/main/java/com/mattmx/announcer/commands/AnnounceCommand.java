package com.mattmx.announcer.commands;

import com.mattmx.announcer.Announcer;
import com.mattmx.announcer.guis.AnnouncementsScreen;
import com.mattmx.announcer.logic.AnnouncerManager;
import com.mattmx.announcer.util.Config;
import com.mattmx.announcer.util.VelocityChat;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

public class AnnounceCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(Component.text("Sorry! That command is player only!").color(TextColor.color(255, 0, 0)));
            return;
        }
        Player p = (Player) invocation.source();
        if (p.hasPermission("vannouncer.commands.use")) {
            if (!Announcer.hasProtocolize()) {
                p.sendMessage(VelocityChat.color("&9&lAnnouncer &7» &fYou do not have &a&nProtocolize&f installed!")
                        .clickEvent(ClickEvent.openUrl("https://github.com/Exceptionflug/protocolize")));
                return;
            }
            String[] args = invocation.arguments();
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "about" -> {
                        p.sendMessage(VelocityChat.color("&9&lVelocityAnnouncer &7» &fRunning v1.0.0"));
                        p.sendMessage(VelocityChat.color("&9&lVelocityAnnouncer &7» &fCreated by &b&nMattMX")
                                .clickEvent(ClickEvent.openUrl("https://www.mattmx.com/")));
                    }
                    case "reload" -> {
                        if (p.hasPermission("vannouncer.commands.reload")) {
                            p.sendMessage(VelocityChat.color("&9&lVelocityAnnouncer &7» &fReloading!"));
                            Config.init();
                            AnnouncerManager.load();
                            p.sendMessage(VelocityChat.color("&9&lVelocityAnnouncer &7» &fReloaded all data from files!"));
                        } else {
                            p.sendMessage(VelocityChat.color("&9&lVelocityAnnouncer &7» &fRunning v1.0.0"));
                        }
                    }
                    default -> {
                        p.sendMessage(VelocityChat.color("&9&lVelocityAnnouncer &7» &fRunning v1.0.0"));
                    }
                }
            } else {
                AnnouncementsScreen screen = new AnnouncementsScreen();
                screen.define(p);
                screen.open();
            }
        } else {
            p.sendMessage(VelocityChat.color("&9&lVelocityAnnouncer &7» &fRunning v1.0.0"));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return SimpleCommand.super.suggest(invocation);
    }
}
