package com.github.frcsty.cache

import com.github.frcsty.load.Settings
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

class PlaceholderCache(private val plugin: Plugin) : Listener, BukkitRunnable() {
    private val cache = ConcurrentHashMap<UUID, Map<String, String>>()

    fun updatePlaceholders() {
        Bukkit.getOnlinePlayers().forEach { updatePlaceholders(it) }
    }

    fun updatePlaceholders(uuid: UUID) {
        Bukkit.getPlayer(uuid)?.let { updatePlaceholders(it) }
    }

    fun updatePlaceholders(player: Player) {
        cache.remove(player.uniqueId)
        cache[player.uniqueId] = Settings.CACHED_PLACEHOLDERS.associateWith { PlaceholderAPI.setPlaceholders(player, it) }
    }

    fun removePlayer(uuid: UUID) {
        cache.remove(uuid)
    }

    fun removePlayer(player: Player) {
        removePlayer(player.uniqueId)
    }

    fun setPlaceholders(message: String, player: Player): String {
        return setPlaceholders(message, player.uniqueId)
    }

    fun setPlaceholders(message: String, uuid: UUID) : String {
        val placeholders = cache[uuid] ?: return  message

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
            Settings.CACHE_UPDATE_INTERVAL
        )
    }
}