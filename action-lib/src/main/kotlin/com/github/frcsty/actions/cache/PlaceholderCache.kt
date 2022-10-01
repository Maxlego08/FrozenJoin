package com.github.frcsty.actions.cache

import java.util.UUID
import org.bukkit.entity.Player

interface PlaceholderCache {
    fun updatePlaceholders()
    fun updatePlaceholders(uuid: UUID)
    fun updatePlaceholders(player: Player)
    fun removePlayer(uuid: UUID)
    fun removePlayer(player: Player)
    fun setPlaceholders(message: String, player: Player): String
    fun setPlaceholders(message: String, uuid: UUID): String
}