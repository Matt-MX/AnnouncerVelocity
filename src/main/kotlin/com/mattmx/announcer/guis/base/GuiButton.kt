package com.mattmx.announcer.guis.base

import dev.simplix.protocolize.api.inventory.InventoryClick
import dev.simplix.protocolize.api.inventory.InventoryClose
import dev.simplix.protocolize.api.item.BaseItemStack
import dev.simplix.protocolize.api.item.ItemStack
import dev.simplix.protocolize.data.ItemType

open class GuiButton {
    private lateinit var item: ItemStack
    private lateinit var clickCallback: (InventoryClick) -> Unit
    private lateinit var closeCallback: (InventoryClose) -> Unit

    fun onClick(event: InventoryClick) {
        if (this::clickCallback.isInitialized)
            clickCallback(event)
    }
    fun onClose(event: InventoryClose) {
        if (this::closeCallback.isInitialized)
            closeCallback(event)
    }

    fun click(callback: InventoryClick.() -> Unit) : GuiButton {
        this.clickCallback = callback
        return this
    }

    fun close(callback: InventoryClose.() -> Unit) : GuiButton {
        this.closeCallback = callback
        return this
    }

    fun item(material: ItemType, callback: BaseItemStack.() -> Unit) : GuiButton {
        this.item = ItemStack(material).apply(callback)
        return this
    }

    fun itemStack() = item

}