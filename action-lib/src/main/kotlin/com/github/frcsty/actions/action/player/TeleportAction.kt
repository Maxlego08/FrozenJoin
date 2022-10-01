package com.github.frcsty.actions.action.player

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

object TeleportAction : Action {

    override val id = "TELEPORT"

    override fun run(player: Player, data: String, cache: PlaceholderCache?) {
        val args = data.split(";")
        val location = Location(
                Bukkit.getWorld(args[0]),
                args[1].toDouble(),
                args[2].toDouble(),
                args[3].toDouble(),
                args[4].toFloat(),
                args[5].toFloat()
        )

        player.teleport(location)
    }
}