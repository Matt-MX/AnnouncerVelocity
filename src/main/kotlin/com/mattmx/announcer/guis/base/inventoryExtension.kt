package com.mattmx.announcer.guis.base

import com.velocitypowered.api.proxy.Player
import dev.simplix.protocolize.api.Protocolize
import dev.simplix.protocolize.api.inventory.Inventory

fun Inventory.open(player: Player) {
    val protocolizePlayer = Protocolize.playerProvider().player(player.uniqueId)
    protocolizePlayer.openInventory(this)
}