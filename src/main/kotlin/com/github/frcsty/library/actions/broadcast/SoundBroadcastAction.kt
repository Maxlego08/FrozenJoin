package com.github.frcsty.library.actions.broadcast

import com.github.frcsty.library.actions.Action
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

object SoundBroadcastAction : Action {

    override val id = "SOUNDBROADCAST"

    override fun run(player: Player, data: String) {
        val args = data.split(";")
        val sound = Sound.valueOf(args[0])
        val volume: Float = args[1].toFloat()
        val pitch: Float = args[2].toFloat()

        Bukkit.getServer().onlinePlayers.forEach { it.playSound(it.location, sound, volume, pitch) }
    }
}