package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Sound
import org.bukkit.entity.Player

object SoundAction : Action {

    override val id = "SOUND"

    override fun run(player: Player, data: String) {
        val args = data.split(";")
        val sound = Sound.valueOf(args[0])
        val volume: Float = Float.fromBits(args[1].toInt())
        val pitch: Float = Float.fromBits(args[2].toInt())

        player.playSound(player.location, sound, volume, pitch)
    }
}
