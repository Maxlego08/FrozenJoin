package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object MessageAction : Action {
    fun execute(player: Player, plugin: Plugin, msg: String) = player.sendMessage(msg)
}