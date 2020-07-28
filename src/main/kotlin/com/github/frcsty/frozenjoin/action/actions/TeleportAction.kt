package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Location
import org.bukkit.entity.Player

object TeleportAction : Action {

    override val id = "TELEPORT"

    override fun run(player: Player, data: String) {
        val args = data.split(";")
        val location = Location(
                player.world,
                args[1].toDouble(),
                args[2].toDouble(),
                args[3].toDouble(),
                Float.fromBits(args[4].toInt()),
                Float.fromBits(args[5].toInt()))

        player.teleport(location)
    }
}
