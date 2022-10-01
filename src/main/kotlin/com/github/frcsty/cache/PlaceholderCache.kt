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
    private val cache = ConcurrentHashMap<UUID, Map<String, String>>()

    override fun updatePlaceholders() {
        Bukkit.getOnlinePlayers().forEach { updatePlaceholders(it) }
    }

    override fun updatePlaceholders(uuid: UUID) {
        Bukkit.getPlayer(uuid)?.let { updatePlaceholders(it) }
    }

    override fun updatePlaceholders(player: Player) {
        cache.remove(player.uniqueId)
        cache[player.uniqueId] =
            settings.cachedPlaceholders.associateWith { PlaceholderAPI.setPlaceholders(player, it) }
    }

    override fun removePlayer(uuid: UUID) {
        cache.remove(uuid)
    }

    override fun removePlayer(player: Player) {
        removePlayer(player.uniqueId)
    }

    override fun setPlaceholders(message: String, player: Player): String {
        return setPlaceholders(message, player.uniqueId)
    }

    override fun setPlaceholders(message: String, uuid: UUID): String {
        val placeholders = cache[uuid] ?: return message

        var newMessage = message
        placeholders.forEach {
            newMessage = newMessage.replace(it.key, it.value)
        }
        return newMessage
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