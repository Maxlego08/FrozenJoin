package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.util.getRecipients
import com.github.frcsty.frozenjoin.util.sendPlayerTitle
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object TitleBroadcastAction : Action {
    override val id = "TITLEBROADCAST"

    override fun run(player: Player, data: String) {
        val args = data.split(" ")

        val (scope, title, subtitle) = args
        val fadeIn = args.getOrNull(3)?.toIntOrNull() ?: DEFAULT_FADE_IN
        val stay = args.getOrNull(4)?.toIntOrNull() ?: DEFAULT_STAY
        val fadeOut = args.getOrNull(5)?.toIntOrNull() ?: DEFAULT_FADE_OUT

        val recipients = when (scope) {
            "PLAYER" -> setOf(player)
            "WORLD", "REGION" -> player.getRecipients(scope)
            else -> Bukkit.getOnlinePlayers().toSet()
        }

        recipients.forEach {
            it.sendPlayerTitle(title, subtitle, fadeIn, stay, fadeOut)
        }
    }


    private const val DEFAULT_FADE_IN = 5
    private const val DEFAULT_STAY = 10
    private const val DEFAULT_FADE_OUT = 5
}
