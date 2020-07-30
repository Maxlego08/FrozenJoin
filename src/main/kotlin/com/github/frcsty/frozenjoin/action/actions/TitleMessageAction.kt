package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.entity.Player

object TitleMessageAction : Action {

    override val id = "TITLEMESSAGE"

    override fun run(player: Player, data: String) {
        val args = data.split(";")

        TitleBroadcastAction.run(player, (listOf("PLAYER") + args.drop(1)).joinToString(";"))
    }
}
