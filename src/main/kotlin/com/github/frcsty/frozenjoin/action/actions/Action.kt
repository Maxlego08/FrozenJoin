package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import org.bukkit.entity.Player

interface Action {
    val id: String
    fun run(player: Player, data: String) {}
    fun run(plugin: FrozenJoinPlugin, player: Player, data: String) {}
}