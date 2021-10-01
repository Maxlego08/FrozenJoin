package com.github.frcsty.library.actions.broadcast

import com.github.frcsty.library.actions.Action
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object TitleBroadcastAction : Action {
    override val id = "TITLEBROADCAST"

    override fun run(player: Player, data: String) {
        val args = data.split(";")

        val (title, subtitle) = args
        val fadeIn = args.getOrNull(3)?.toIntOrNull() ?: DEFAULT_FADE_IN
        val stay = args.getOrNull(4)?.toIntOrNull() ?: DEFAULT_STAY
        val fadeOut = args.getOrNull(5)?.toIntOrNull() ?: DEFAULT_FADE_OUT

        Bukkit.getServer().onlinePlayers.forEach {
            it.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
        }
    }

    private const val DEFAULT_FADE_IN = 5
    private const val DEFAULT_STAY = 10
    private const val DEFAULT_FADE_OUT = 5
}