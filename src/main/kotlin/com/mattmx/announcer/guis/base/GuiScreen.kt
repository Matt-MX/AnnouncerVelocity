package com.mattmx.announcer.guis.base

import com.velocitypowered.api.proxy.Player
import dev.simplix.protocolize.api.inventory.Inventory
import dev.simplix.protocolize.api.inventory.InventoryClick
import dev.simplix.protocolize.api.inventory.InventoryClose
import dev.simplix.protocolize.data.inventory.InventoryType
import net.kyori.adventure.text.TextComponent

open class GuiScreen(
    var title: TextComponent,
    rows: Int? = null,
    var type: InventoryType = getInventoryType(rows)
) {
    val items = hashMapOf<Int, GuiButton>()

    open fun build(player: Player) : Inventory {
        val inventory = Inventory(type)
        inventory.title(title)
        items.forEach { (slot, item) ->
            if (slot >= type.getTypicalSize(player.protocolVersion.protocol))
                return@forEach
            inventory.item(slot, item.itemStack())
        }
        inventory.onClick(this::onClick)
        inventory.onClose(this::onClose)
        return inventory
    }

    protected open fun onClick(event: InventoryClick) {
        val newEvent = event.cancelled(true)
        val item = items[event.slot()]
        item?.onClick(newEvent)
    }

    protected open fun onClose(event: InventoryClose) {
        items.forEach { (slot, item) ->
            item.onClose(event)
        }
    }

    fun maxSlot() = type

    companion object {
        fun getInventoryType(rows: Int?) : InventoryType {
            if (rows == null) return InventoryType.GENERIC_9X6
            return when (rows) {
                1 -> InventoryType.GENERIC_9X1
                2 -> InventoryType.GENERIC_9X2
                3 -> InventoryType.GENERIC_9X3
                4 -> InventoryType.GENERIC_9X4
                5 -> InventoryType.GENERIC_9X5
                else -> InventoryType.GENERIC_9X6
            }
        }
    }
}