package com.github.frcsty.library.actions.player

import com.github.frcsty.cache.PlaceholderCache
import com.github.frcsty.library.actions.Action
import com.github.frcsty.util.getTranslatedMessage
import org.bukkit.entity.Player

object PlayerCommandAction : Action {

    override val id = "PLAYERCOMMAND"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        player.performCommand(data.getTranslatedMessage(player, cache))
    }
}