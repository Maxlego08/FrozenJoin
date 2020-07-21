package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object PlayerCommandAction : Action {
    fun execute(player: Player, plugin: Plugin, command: String) = player.performCommand(command)
}