package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.extension.sendTranslatedMessage
import org.bukkit.entity.Player

object MessageAction : Action {
    override val id = "MESSAGE"

    override fun run(player: Player, data: String) = player.sendTranslatedMessage(data)
}
