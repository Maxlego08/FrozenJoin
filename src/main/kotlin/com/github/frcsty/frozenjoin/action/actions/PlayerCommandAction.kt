package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.entity.Player

class PlayerCommandAction : Action {

    override val id = "PLAYER"

    override fun run(player: Player, data: String) {
        player.performCommand(data)
    }
}