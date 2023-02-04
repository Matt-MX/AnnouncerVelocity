package com.mattmx.announcer.guis

import co.pvphub.velocity.scheduling.syncDelayed
import co.pvphub.velocity.util.colored
import com.mattmx.announcer.AnnouncementManager
import com.mattmx.announcer.Announcer
import com.mattmx.announcer.announcements.AnnouncementGroup
import com.mattmx.announcer.guis.base.GuiButton
import com.mattmx.announcer.guis.base.GuiScreen
import com.mattmx.announcer.guis.base.open
import dev.simplix.protocolize.api.inventory.InventoryClose
import dev.simplix.protocolize.data.ItemType

class AnnouncementListScreen(
    private val group: AnnouncementGroup? = null,
    private val previous: AnnouncementListScreen? = null
) : GuiScreen(
    if (group != null) "&7${group.fancyId()} > &rAnnouncements".colored()
    else "Announcements".colored(),
    6
) {

    init {
        val announcements = group?.announcements ?: AnnouncementManager.all()
        var currentSlot = 10
        for (announcement in announcements) {

            items[currentSlot] = AnnouncementButton(announcement, this)

            currentSlot++
            if ((currentSlot - 8) % 9 == 0) {
                currentSlot += 2
            }
            // TODO(Matt): Impl pages
            if (currentSlot >= 43)
                break
        }
        items[49] = GuiButton()
            .item(ItemType.SPECTRAL_ARROW) {
                displayName("&dGo Back".colored())
            }.click {
                val proxyPlayer = Announcer.get().server.getPlayer(player().uniqueId()).get()
                previous?.build(proxyPlayer)?.open(proxyPlayer)
                    ?: player().closeInventory()
            }
    }

    override fun onClose(event: InventoryClose) {
        super.onClose(event)
        if (previous != null) {
            syncDelayed(Announcer.get(), 100) {
                val proxyPlayer = Announcer.get().server.getPlayer(event.player().uniqueId()).get()
                previous.build(proxyPlayer).open(proxyPlayer)
            }
        }
    }
}