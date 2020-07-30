package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.util.sendActionBarMessage
import org.bukkit.entity.Player

object ActionbarMessageAction : Action {
    override val id = "ACTIONBARMESSAGE"

    override fun run(player: Player, data: String) {
        player.sendActionBarMessage(data)
    }
}
