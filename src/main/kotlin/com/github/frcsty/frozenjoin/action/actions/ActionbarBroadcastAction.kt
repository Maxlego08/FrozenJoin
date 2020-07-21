package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.action.util.UtilPlayer.sendActionbar
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object ActionbarBroadcastAction : Action {
    fun execute(player: Player, plugin: Plugin, msg: String) = sendActionbar(Bukkit.getOnlinePlayers(), msg)
}