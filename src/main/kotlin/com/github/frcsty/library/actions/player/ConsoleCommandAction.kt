package com.github.frcsty.library.actions.player

import com.github.frcsty.library.actions.Action
import com.github.frcsty.library.util.getTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ConsoleCommandAction : Action {

    override val id = "CONSOLECOMMAND"

    override fun run(player: Player, data: String) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data.getTranslatedMessage(player))
    }
}