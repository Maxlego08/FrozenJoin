package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Bukkit

import org.bukkit.Sound

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object SoundBroadcastAction : Action {
    fun execute(player: Player, plugin: Plugin, sound: Sound, volume: Double, pitch: Double) = Bukkit.getOnlinePlayers().forEach { other: Player -> other.playSound(other.location, sound!!, volume.toFloat(), pitch.toFloat()) }
}