package com.github.frcsty.actions.action.broadcast

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.util.getTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object JsonBroadcastAction : Action {

    override val id = "JSONBROADCAST"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        Bukkit.getServer().onlinePlayers.forEach {
            Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "tellraw ${it.name} ${data.getTranslatedMessage(player = player, player2 = it, cache = cache)}"
            )
        }
    }
}