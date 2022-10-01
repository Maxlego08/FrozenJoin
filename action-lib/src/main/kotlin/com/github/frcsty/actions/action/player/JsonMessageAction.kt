package com.github.frcsty.actions.action.player

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.util.getTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object JsonMessageAction : Action {

    override val id = "JSONMESSAGE"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        Bukkit.dispatchCommand(
            Bukkit.getConsoleSender(),
            "tellraw ${player.name} ${data.getTranslatedMessage(player, cache)}"
        )
    }
}