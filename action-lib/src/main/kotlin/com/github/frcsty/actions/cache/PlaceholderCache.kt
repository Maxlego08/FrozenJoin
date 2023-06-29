package com.github.frcsty.actions.cache

import java.util.UUID
import org.bukkit.entity.Player

interface PlaceholderCache {
    fun updatePlaceholders()
    fun updatePlaceholders(uuid: UUID, uuid2: UUID? = null)
    fun updatePlaceholders(player: Player, player2: Player? = null)
    fun removePlayer(uuid: UUID)
    fun removePlayer(player: Player)
    fun setPlaceholders(message: String, uuid: UUID, uuid2: UUID? = null): String
    fun setPlaceholders(message: String, player: Player, player2: Player? = null): String
}