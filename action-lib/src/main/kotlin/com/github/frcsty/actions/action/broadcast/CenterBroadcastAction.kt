package com.github.frcsty.actions.action.broadcast

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.action.player.CenterMessageAction
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.util.getTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object CenterBroadcastAction : Action {

    override val id = "CENTERBROADCAST"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        Bukkit.getServer().onlinePlayers.forEach { CenterMessageAction.run(it, data.getTranslatedMessage(player, cache), cache) }
    }
}