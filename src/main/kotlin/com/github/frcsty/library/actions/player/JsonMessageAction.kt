package com.github.frcsty.library.actions.player

import com.github.frcsty.library.actions.Action
import com.github.frcsty.util.getTranslatedMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object JsonMessageAction : Action {

    override val id = "JSONMESSAGE"

    override fun run(player: Player, data: String) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw ${player.name} ${data.getTranslatedMessage(player)}")
    }
}