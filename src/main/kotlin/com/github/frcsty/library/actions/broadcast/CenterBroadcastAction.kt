package com.github.frcsty.library.actions.broadcast

import com.github.frcsty.cache.PlaceholderCache
import com.github.frcsty.library.actions.Action
import com.github.frcsty.library.actions.player.CenterMessageAction
import com.github.frcsty.util.getTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object CenterBroadcastAction : Action {

    override val id = "CENTERBROADCAST"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        Bukkit.getServer().onlinePlayers.forEach { CenterMessageAction.run(it, data.getTranslatedMessage(player, cache), cache) }
    }
}