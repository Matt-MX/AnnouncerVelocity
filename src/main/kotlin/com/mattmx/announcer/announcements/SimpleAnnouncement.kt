package com.mattmx.announcer.announcements

import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.TextComponent

/**
 * Default implementation of an announcement which
 * takes a single duration.
 *
 * @author MattMX
 * @param id of the announcer
 * @param messages messages of which to broadcast
 * @param delay of the announcement
 */
class SimpleAnnouncement(
    val id: String,
    val messages: ArrayList<TextComponent>,
    val options: SimpleAnnouncementOptions
) : Announcement {
    private var lastExecute: Long = 0L

    override fun execute(server: ProxyServer) {
        for (registeredServer in server.allServers) {
            if (options.targetServers != null && options.targetServers?.matches(registeredServer.serverInfo.name) == false)
                continue
            for (player in registeredServer.playersConnected) {
                if ((options.permissionToHide != null && player.hasPermission(options.permissionToHide))
                    || options.permissionToDisplay != null && !player.hasPermission(options.permissionToDisplay))
                    continue
                // Display the message
                messages.forEach { player.sendMessage(it) }
            }
        }
    }

    override fun shouldExecute() = lastExecute + options.delay.toMillis() <= System.currentTimeMillis()
    override fun getId() = id
}