package com.github.frcsty.load

import com.github.frcsty.FrozenJoinPlugin
import java.util.logging.Level
import org.bukkit.plugin.java.JavaPlugin

object Settings {
    private val plugin = JavaPlugin.getPlugin(FrozenJoinPlugin::class.java)
    private val config = plugin.config

    val CACHE_UPDATE_INTERVAL: Long = config.getLong("placeholder-cache.update-interval", 20L)
    val CACHED_PLACEHOLDERS: List<String> = config.getStringList("placeholder-cache.placeholders")
    val DEBUG: Boolean = config.getString("consoleMessages", "ENABLED").equals("ENABLED", ignoreCase = true)
    val METRICS: Boolean = config.getBoolean("stonks", true)

    val LOGGER = plugin.logger
    val PLUGIN_VERSION = plugin.description.version
}

fun logError(message: String): Unit = Settings.LOGGER.log(Level.WARNING, message)
fun logInfo(message: String): Unit = Settings.LOGGER.log(Level.INFO, message)