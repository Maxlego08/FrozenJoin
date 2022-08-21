package com.github.frcsty.library.actions.broadcast

import com.github.frcsty.cache.PlaceholderCache
import com.github.frcsty.library.actions.Action
import com.github.frcsty.util.sendTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object BroadcastAction : Action {
    override val id = "BROADCAST"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) = Bukkit.getServer().onlinePlayers.forEach {
        it.sendTranslatedMessage(data, player, cache)
    }
}