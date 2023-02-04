package com.mattmx.announcer.announcements

import co.pvphub.velocity.util.colored
import com.mattmx.announcer.utils.formatted
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
class RandomizedAnnouncement(
    private val id: String,
    val messages: ArrayList<TextComponent>,
    val options: RandomizedAnnouncementOptions
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
        // Generate the new delay
        options.generateDelay()
    }

    override fun shouldExecute() = lastExecute + options.delay.toMillis() <= System.currentTimeMillis()

    override fun lore(): MutableList<TextComponent> {
        val lore = super.lore()
        lore += "&7Delay Min: &d${options.delayMin.formatted()}".colored()
        lore += "&7Delay Max: &d${options.delayMax.formatted()}".colored()
        lore += "&7Permission (Show): &d${options.permissionToDisplay}".colored()
        lore += "&7Permission (Hide): &d${options.permissionToHide}".colored()
        lore += "&7Target servers: &d${options.targetServers}".colored()
        lore += "&7Chat lines: &d${messages.size}".colored()
        return lore
    }

    override fun getId() = id
}