package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.util.getTranslatedMessage
import org.bukkit.entity.Player

object PlayerCommandAction : Action {

    override val id = "PLAYERCOMMAND"

    override fun run(player: Player, data: String) {
        player.performCommand(data.getTranslatedMessage(player))
    }
}
