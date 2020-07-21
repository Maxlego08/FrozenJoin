package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Bukkit

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object BroadcastAction : Action {
    fun execute(player: Player, plugin: Plugin, message: String) = Bukkit.broadcastMessage(message)
}