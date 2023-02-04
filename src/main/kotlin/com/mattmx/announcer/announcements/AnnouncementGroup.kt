package com.mattmx.announcer.announcements

import co.pvphub.velocity.util.colored
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.TextComponent

/**
 * Implementation of a group of announcements.
 * This will execute each announcement one after the other.
 *
 * @author MattMX
 * @param announcements announcements to display (in-order)
 */
class AnnouncementGroup(
    private val id: String,
    val announcements: ArrayList<Announcement>
) : Announcement {

    private var currentAnnouncementIndex = 0

    override fun execute(server: ProxyServer) {
        currentAnnouncement()?.execute(server)
        nextAnnouncement()
    }

    private fun nextAnnouncement() {
        currentAnnouncementIndex++
        if (currentAnnouncementIndex >= announcements.size) {
            currentAnnouncementIndex = 0
        }
    }

    private fun currentAnnouncement() = announcements.getOrNull(currentAnnouncementIndex)

    override fun shouldExecute() = currentAnnouncement()?.shouldExecute() == true

    override fun lore(): MutableList<TextComponent> {
        val lore = super.lore()
        lore += "&7Announcements: &d${announcements.size}".colored()
        return lore
    }

    override fun getId() = id
}