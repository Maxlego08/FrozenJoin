package com.github.frcsty.actions.action.player

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.util.getTranslatedMessage
import org.bukkit.entity.Player

object PlayerCommandAction : Action {

    override val id = "PLAYERCOMMAND"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        player.performCommand(data.getTranslatedMessage(player = player, player2 = null, cache = cache))
    }
}