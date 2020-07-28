package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.extension.sendTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object BroadcastAction : Action {
    override val id = "BROADCAST"

    override fun run(player: Player, data: String) = Bukkit.getServer().onlinePlayers.forEach { it.sendTranslatedMessage(data) }
}
