package com.github.frcsty.library.actions.broadcast

import com.github.frcsty.library.actions.Action
import com.github.frcsty.library.util.sendTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object AudienceBroadcastAction : Action {
    override val id = "AUDIENCEBROADCAST"

    override fun run(player: Player, data: String) = Bukkit.getServer().onlinePlayers.forEach {
        if (player == it) return@forEach
        it.sendTranslatedMessage(data, player)
    }
}