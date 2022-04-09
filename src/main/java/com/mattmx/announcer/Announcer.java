package com.mattmx.announcer;

import com.google.inject.Inject;
import com.mattmx.announcer.commands.AnnounceCommand;
import com.mattmx.announcer.logic.AnnouncerManager;
import com.mattmx.announcer.logic.ChatManager;
import com.mattmx.announcer.util.Config;
import com.mattmx.announcer.util.DependencyChecker;
import com.mattmx.announcer.util.VelocityPlugin;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(
        id = "announcer",
        name = "Announcer",
        version = "1.2.1",
        description = "Velocity server-wide announcements!",
        url = "https://www.mattmx.com",
        authors = {"MattMX"}
)
public class Announcer extends VelocityPlugin {
    static Announcer instance;
    private boolean protocolize;

    @Inject
    public Announcer(ProxyServer server, Logger logger) {
        this.init(server, logger, "announcer");
        if (DependencyChecker.protocolize()) {
            protocolize = true;
        } else {
            protocolize = false;
        }
        instance = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        Config.init();
        getServer().getCommandManager().register("vannouncer", new AnnounceCommand(), "velocityannouncer", "va");
        getServer().getEventManager().register(this, new ChatManager());
        AnnouncerManager.load();
        AnnouncerManager.init();
    }

    public static Announcer get() {
        return instance;
    }

    public static boolean hasProtocolize() {
        return instance.protocolize;
    }
}
