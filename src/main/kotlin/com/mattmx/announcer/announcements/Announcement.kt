package com.mattmx.announcer.announcements

import com.velocitypowered.api.proxy.ProxyServer
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
}