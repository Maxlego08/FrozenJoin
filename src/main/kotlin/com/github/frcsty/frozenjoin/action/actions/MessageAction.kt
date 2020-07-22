package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.entity.Player

class MessageAction : Action {
    override val id: String = "MESSAGE"

    override fun run(player: Player, data: String) {
        println(data)
    }
}