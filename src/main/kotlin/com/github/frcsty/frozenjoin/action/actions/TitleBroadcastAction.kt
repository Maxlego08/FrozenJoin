package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.action.util.UtilPlayer.sendTitle
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object TitleBroadcastAction : Action {
    fun execute(player: Player, plugin: Plugin, title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int) = sendTitle(Bukkit.getOnlinePlayers(), title, subtitle, fadeIn, stay, fadeOut)
}