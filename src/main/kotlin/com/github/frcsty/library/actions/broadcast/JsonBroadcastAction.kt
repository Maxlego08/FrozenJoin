package com.github.frcsty.library.actions.broadcast

import com.github.frcsty.library.actions.Action
import com.github.frcsty.util.getTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object JsonBroadcastAction : Action {

    override val id = "JSONBROADCAST"

    override fun run(player: Player, data: String) {
        Bukkit.getServer().onlinePlayers.forEach { Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw ${it.name} ${data.getTranslatedMessage(player)}") }
    }
}