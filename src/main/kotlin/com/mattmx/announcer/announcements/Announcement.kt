package com.mattmx.announcer.announcements

import co.pvphub.velocity.util.colored
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.TextComponent
import java.util.UUID

/**
 * Represents an executable announcement interface.
 *
 * @author MattMX
 */
interface Announcement {
    /**
     * Called when the announcement is to be
     * executed.
     *
     * @author MattMX
     * @param server the proxy server instance
     */
    fun execute(server: ProxyServer)

    /**
     * A unique ID for this announcement.
     * This is only needed if you are registering it to the
     * [AnnouncementManager]
     *
     * @author MattMX
     */
    fun getId(): String = UUID.randomUUID().toString()

    /**
     * Called before [execute] to check if we should
     * call it or not.
     *
     * @author MattMX
     */
    fun shouldExecute() : Boolean

    /**
     * Gets the lore component for a type of announcement
     *
     * @author MattMX
     * @return the announcement's lore
     */
    fun lore() : MutableList<TextComponent> {
        return mutableListOf("&7Type: &d${this::class.simpleName}".colored())
    }

    /**
     * Utility function to format the ID into a nice looking string.
     *
     * @author MattMX
     */
    fun fancyId() = getId()
        .lowercase()
        .replace("-", " ")
        .split(" ")
        .joinToString(" ") {
            it.replaceFirstChar { c -> c.titlecase() }
        }
}