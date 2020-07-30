package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ConsoleCommandAction : Action {

    override val id = "CONSOLECOMMAND"

    override fun run(player: Player, data: String) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data)
    }
}
