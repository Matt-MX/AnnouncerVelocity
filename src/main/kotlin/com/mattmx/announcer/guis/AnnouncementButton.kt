package com.mattmx.announcer.guis

import co.pvphub.velocity.extensions.json
import co.pvphub.velocity.util.colored
import com.mattmx.announcer.Announcer
import com.mattmx.announcer.announcements.Announcement
import com.mattmx.announcer.announcements.AnnouncementGroup
import com.mattmx.announcer.announcements.RandomizedAnnouncement
import com.mattmx.announcer.announcements.SimpleAnnouncement
import com.mattmx.announcer.guis.base.GuiButton
import com.mattmx.announcer.guis.base.open
import dev.simplix.protocolize.data.ItemType
import net.kyori.adventure.text.format.TextDecoration

class AnnouncementButton(
    val announcement: Announcement,
    val owner: AnnouncementListScreen
) : GuiButton() {

    init {
        item(getAnnouncementMaterial(announcement)) {
            val title = "&d${announcement.fancyId()}".colored()
            displayName(title.style(title.style().decoration(TextDecoration.ITALIC, false)))
            announcement.lore().forEach { addToLore(it.style(it.style().decoration(TextDecoration.ITALIC, false))) }
        }
        click {
            val proxyPlayer = Announcer.get().server.getPlayer(player().uniqueId()).get()
            // TODO(Matt): Open a GUI to edit this announcement depending on type
            when (announcement) {
                is RandomizedAnnouncement,
                is SimpleAnnouncement -> {
                    println("Simple announcement")
                }
                is AnnouncementGroup -> {
                    AnnouncementListScreen(announcement, owner)
                        .build(proxyPlayer)
                        .open(proxyPlayer)
                }
            }
        }
    }

    companion object {
        fun getAnnouncementMaterial(announcement: Announcement): ItemType {
            return when (announcement) {
                is SimpleAnnouncement -> ItemType.PAPER
                is AnnouncementGroup -> ItemType.BOOK
                is RandomizedAnnouncement -> ItemType.MAP
                else -> ItemType.PAPER
            }
        }
    }

}