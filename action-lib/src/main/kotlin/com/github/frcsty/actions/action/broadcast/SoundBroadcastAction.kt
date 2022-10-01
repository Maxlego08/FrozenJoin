package com.github.frcsty.actions.action.broadcast

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

object SoundBroadcastAction : Action {

    override val id = "SOUNDBROADCAST"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        val args = data.split(";")
        val sound = Sound.valueOf(args[0])
        val volume: Float = args[1].toFloat()
        val pitch: Float = args[2].toFloat()

        Bukkit.getServer().onlinePlayers.forEach { it.playSound(it.location, sound, volume, pitch) }
    }
}