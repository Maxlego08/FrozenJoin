package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.entity.Player

class MessageAction : Action {
    override val id: String
        get() = "MESSAGE"

    override fun run(player: Player, data: String) {
        println(data)
    }
}