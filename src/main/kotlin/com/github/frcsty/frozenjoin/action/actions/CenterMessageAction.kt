package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.action.util.UtilMessage.sendCenteredMessage
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object CenterMessageAction : Action {
    fun execute(player: Player, plugin: Plugin, msg: String) {
        sendCenteredMessage(player, msg)
    }
}