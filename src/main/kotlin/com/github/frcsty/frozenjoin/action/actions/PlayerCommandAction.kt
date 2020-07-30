package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.entity.Player

object PlayerCommandAction : Action {

    override val id = "PLAYERCOMMAND"

    override fun run(player: Player, data: String) {
        player.performCommand(data)
    }
}
