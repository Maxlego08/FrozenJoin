package com.github.frcsty.library.actions.broadcast

import com.github.frcsty.cache.PlaceholderCache
import com.github.frcsty.library.actions.Action
import com.github.frcsty.util.color
import com.github.frcsty.util.getTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ActionbarBroadcastAction : Action {
    override val id = "ACTIONBARBROADCAST"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        Bukkit.getServer().onlinePlayers.forEach { it.sendActionBar(data.getTranslatedMessage(player, cache).color()) }
    }
}