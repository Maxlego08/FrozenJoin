package com.github.frcsty.cache

import com.github.frcsty.actions.cache.PlaceholderCache
import com.github.frcsty.actions.load.Settings
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class PlaceholderCache(private val plugin: Plugin, private val settings: Settings) : PlaceholderCache, Listener,
    BukkitRunnable() {

    // for each player hold a map of simple placeholders and their values
    private val simplePlaceholdersCache = ConcurrentHashMap<UUID, Map<String, String>>()

    // for each player pair hold a map of relational placeholders and their values
    private val relationalPlaceholdersCache = ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, Map<String, String>>>()


    override fun updatePlaceholders() {
        Bukkit.getOnlinePlayers().forEach { player1 ->
            Bukkit.getOnlinePlayers().forEach secondLoop@{ player2 ->
                if (player1.uniqueId == player2.uniqueId) return@secondLoop
                updatePlaceholders(player1, player2)
            }
        }
    }

    override fun updatePlaceholders(uuid: UUID, uuid2: UUID?) {
        Bukkit.getPlayer(uuid)?.let { player -> updatePlaceholders(player, uuid2?.let { Bukkit.getPlayer(it) }) }
    }

    override fun updatePlaceholders(player: Player, player2: Player?) {
        // Clear the cache
        simplePlaceholdersCache.remove(player.uniqueId)
        relationalPlaceholdersCache.remove(player.uniqueId)

        // Update the simple placeholders cache
        simplePlaceholdersCache[player.uniqueId] =
            settings.cachedPlaceholders.associateWith { PlaceholderAPI.setPlaceholders(player, it) }

        // Update the relational placeholders cache
        player2?.let {
            relationalPlaceholdersCache.getOrDefault(player.uniqueId, ConcurrentHashMap())[player2.uniqueId] =
                settings.cachedRelationalPlaceholders.associateWith {
                    PlaceholderAPI.setRelationalPlaceholders(player, player2, it)
                }
        }
    }

    override fun removePlayer(uuid: UUID) {
        simplePlaceholdersCache.remove(uuid)
        relationalPlaceholdersCache.remove(uuid)
        relationalPlaceholdersCache.forEach { it.value.remove(uuid) }
    }

    override fun removePlayer(player: Player) {
        removePlayer(player.uniqueId)
    }

    override fun setPlaceholders(message: String, uuid: UUID, uuid2: UUID?): String {
        val simplePlaceholders = simplePlaceholdersCache[uuid]
        val relationalPlaceholders = relationalPlaceholdersCache[uuid]

        if (simplePlaceholders == null && relationalPlaceholders == null) {
            return message
        }

        var newMessage = message

        simplePlaceholders?.forEach {
            newMessage = newMessage.replace(it.key, it.value)
        }

        relationalPlaceholders?.get(uuid2)?.forEach {
            newMessage = newMessage.replace(it.key, it.value)
        }

        return newMessage
    }

    override fun setPlaceholders(message: String, player: Player, player2: Player?): String {
        return setPlaceholders(message, player.uniqueId, player2?.uniqueId)
    }

    override fun run() {
        updatePlaceholders()
    }

    @EventHandler
    private fun onJoin(event: PlayerJoinEvent) {
        updatePlaceholders(event.player)
    }

    @EventHandler
    private fun onLeave(event: PlayerQuitEvent) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            plugin,
            { removePlayer(event.player) },
            settings.cacheUpdateInterval
        )
    }
}