package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.entity.Player

class BroadcastAction : Action {
    override val id: String
        get() = "BROADCAST"

    override fun run(player: Player, data: String) {
        for (i in 0..4) {
            println(data)
        }
    }
}