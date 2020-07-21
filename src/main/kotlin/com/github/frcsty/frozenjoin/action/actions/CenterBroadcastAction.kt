package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Bukkit

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object CenterBroadcastAction : Action {
    fun execute(player: Player, plugin: Plugin, msg: String) {
        Bukkit.getOnlinePlayers().forEach { other: Player? -> CenterMessageAction.execute(player, plugin, msg) }
    }
}