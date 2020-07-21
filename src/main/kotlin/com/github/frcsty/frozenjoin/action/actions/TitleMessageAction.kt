package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.action.util.UtilPlayer.sendTitle
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

object TitleMessageAction : Action {
    fun execute(player: Player, plugin: Plugin, title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int) = sendTitle(Collections.singletonList(player), title, subtitle, fadeIn, stay, fadeOut)
}