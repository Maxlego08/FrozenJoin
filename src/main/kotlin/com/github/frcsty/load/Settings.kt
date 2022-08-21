package com.github.frcsty.load

import com.github.frcsty.FrozenJoinPlugin
import java.util.logging.Level
import org.bukkit.plugin.java.JavaPlugin

object Settings {
    private val plugin = JavaPlugin.getPlugin(FrozenJoinPlugin::class.java)

    val CACHE_UPDATE_INTERVAL: Long = plugin.config.getLong("placeholder-cache.update-interval", 20L)
    var CACHED_PLACEHOLDERS: List<String> = plugin.config.getStringList("placeholder-cache.placeholders")
    val DEBUG: Boolean = plugin.config.getString("consoleMessages", "ENABLED").equals("ENABLED", ignoreCase = true)
    val METRICS: Boolean = plugin.config.getBoolean("stonks", true)

    val LOGGER = plugin.logger
    val PLUGIN_VERSION = plugin.description.version

    fun reload() {
        CACHED_PLACEHOLDERS = plugin.config.getStringList("placeholder-cache.placeholders")
    }
}

fun logError(message: String): Unit = Settings.LOGGER.log(Level.WARNING, message)
fun logInfo(message: String): Unit = Settings.LOGGER.log(Level.INFO, message)