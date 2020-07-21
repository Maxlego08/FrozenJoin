package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.action.util.UtilServer.writeToBungee
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object BungeeAction : Action {
    fun execute(player: Player, plugin: Plugin, server: String) {
        println(server)
        writeToBungee(player, plugin, "Connect", server)
    }
}