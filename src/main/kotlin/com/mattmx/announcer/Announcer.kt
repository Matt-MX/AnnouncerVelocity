package com.mattmx.announcer

import co.pvphub.velocity.command.oldliteral.arguments.string
import co.pvphub.velocity.command.oldliteral.dsl.command
import co.pvphub.velocity.plugin.VelocityPlugin
import co.pvphub.velocity.util.colored
import com.google.inject.Inject
import com.mattmx.announcer.utils.DependencyChecker
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import java.nio.file.Path
import java.util.logging.Logger

@Plugin(
    id = "announcer",
    name = "Announcer",
    version = "2.0.0",
    description = "Velocity server-wide announcements!",
    url = "https://mattmx.com",
    authors = ["MattMX"]
)
class Announcer @Inject constructor(
    server: ProxyServer,
    logger: Logger,
    @DataDirectory
    dataDirectory: Path
) : VelocityPlugin(server, logger, dataDirectory) {
    private var protocolizeInstalled: Boolean = false

    @Subscribe(order = PostOrder.LATE)
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        saveDefaultConfig()

        protocolizeInstalled = DependencyChecker.protocolize()
        if (!protocolizeInstalled) {
            logger.warning("Protocolize is not installed. Sounds and GUIs are not available.")
        }

        AnnouncementManager.init(server, this)

        command<CommandSource>("announcer") {
            subcommand(command("reload") {
                // TODO(Matt): VelocityUtils api needs changing to allow for reloading
            })
            subcommand(command("force") {
                val id by string("announcement-id")
                runs {
                    val announcement = AnnouncementManager.getById(id)
                    if (announcement == null) {
                        source.sendMessage("&cInvalid announcement ID.".colored())
                        source.sendMessage("&f${AnnouncementManager.all().joinToString("&7, &f") { it.getId() }}".colored())
                        return@runs
                    }
                    source.sendMessage("&aForcing ${announcement.getId()} to run.".colored())
                    announcement.execute(server)
                }
            })
            runs {
                if (!protocolizeInstalled) {
                    source.sendMessage("&#3f30caRunning Velocity Announcer &f|&#9f40d6 MattMX. &7(v${version()})".colored())
                    source.sendMessage("&cInstall Protocolize to use the GUI interface.".colored())
                    return@runs
                }
                // TODO(Matt): Open velocity announcer GUI manager
            }
        }
    }

    fun version() = (this::class.annotations.firstOrNull { it is Plugin } as Plugin?)?.version ?: "0.0.0"
}