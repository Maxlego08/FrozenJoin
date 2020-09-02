package com.github.frcsty.library.actions.player

import com.github.frcsty.library.actions.Action
import com.github.frcsty.library.util.getTranslatedMessage
import org.bukkit.entity.Player

object PlayerCommandAction : Action {

    override val id = "PLAYERCOMMAND"

    override fun run(player: Player, data: String) {
        player.performCommand(data.getTranslatedMessage(player))
    }
}