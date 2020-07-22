package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.entity.Player

interface Action {
    val id: String
    fun run(player: Player, data: String)
}