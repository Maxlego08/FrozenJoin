package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.action.util.UtilPlayer.setPlayerItem
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object EquipItemAction : Action {
    fun execute(player: Player, plugin: Plugin, item: String, display: String, lore: List<String>, amount: Int, slot: Int) {
        setPlayerItem(player, item, display, lore, amount, slot)
    }
}