package com.github.frcsty.library.actions.player

import com.github.frcsty.cache.PlaceholderCache
import com.github.frcsty.library.actions.Action
import org.bukkit.Sound
import org.bukkit.entity.Player

object SoundAction : Action {

    override val id = "SOUND"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        val args = data.split(";")
        val sound = Sound.valueOf(args[0])
        val volume: Float = args[1].toFloat()
        val pitch: Float = args[2].toFloat()

        player.playSound(player.location, sound, volume, pitch)
    }
}