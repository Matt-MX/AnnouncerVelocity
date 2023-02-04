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

public class OldAnnouncer extends VelocityPlugin {
    static OldAnnouncer instance;
    private boolean protocolize;

    @Inject
    public OldAnnouncer(ProxyServer server, Logger logger) {
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

    public static OldAnnouncer get() {
        return instance;
    }

    public static boolean hasProtocolize() {
        return instance.protocolize;
    }
}
