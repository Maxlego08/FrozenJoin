package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.util.sendActionBarMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ActionbarBroadcastAction : Action {
    override val id = "ACTIONBARBROADCAST"

    override fun run(player: Player, data: String) {
        Bukkit.getServer().onlinePlayers.forEach { it.sendActionBarMessage(data) }
    }
}
