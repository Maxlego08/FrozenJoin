package com.github.frcsty.library.actions.broadcast

import com.github.frcsty.library.actions.Action
import com.github.frcsty.library.util.getTranslatedMessage
import com.github.frcsty.library.util.sendActionBarMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ActionbarBroadcastAction : Action {
    override val id = "ACTIONBARBROADCAST"

    override fun run(player: Player, data: String) {
        Bukkit.getServer().onlinePlayers.forEach { it.sendActionBarMessage(data.getTranslatedMessage(player)) }
    }
}