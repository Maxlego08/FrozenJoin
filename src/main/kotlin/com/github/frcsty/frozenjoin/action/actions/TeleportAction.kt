package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Location
import org.bukkit.World

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object TeleportAction : Action {
    fun execute(player: Player, plugin: Plugin, world: World, x: Double?, y: Double?, z: Double?, yaw: Float?, pitch: Float?) {
        val location = Location(
                world,
                x ?: player.location.x,
                y ?: player.location.y,
                z ?: player.location.z,
                yaw ?: player.location.yaw,
                pitch ?: player.location.pitch
        )
        player.teleport(location)
    }
}