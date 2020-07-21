package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Sound

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object SoundAction : Action {
    fun execute(player: Player, plugin: Plugin, sound: Sound, volume: Double, pitch: Double) = player.playSound(player.location, sound, volume.toFloat(), pitch.toFloat())
}