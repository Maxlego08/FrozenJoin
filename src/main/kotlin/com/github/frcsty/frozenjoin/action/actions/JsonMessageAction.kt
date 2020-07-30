package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object JsonMessageAction : Action {

    override val id = "JSONMESSAGE"

    override fun run(player: Player, data: String) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw ${player.name} $data")
    }
}
