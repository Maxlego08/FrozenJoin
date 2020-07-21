package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object JsonMessageAction : Action {
    fun execute(player: Player, plugin: Plugin, json: String) {
        ConsoleCommandAction.execute(player, plugin, "tellraw " + player.name + " " + json)
    }
}