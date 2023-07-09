package com.github.frcsty.actions.action.player

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.util.getTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ConsoleCommandAction : Action {

    override val id = "CONSOLECOMMAND"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data.getTranslatedMessage(player = player, player2 = null, cache = cache))
    }
}