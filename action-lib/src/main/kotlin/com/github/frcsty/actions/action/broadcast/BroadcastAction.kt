package com.github.frcsty.actions.action.broadcast

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.util.sendTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object BroadcastAction : Action {
    override val id = "BROADCAST"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) = Bukkit.getServer().onlinePlayers.forEach {
        it.sendTranslatedMessage(data, player, cache)
    }
}