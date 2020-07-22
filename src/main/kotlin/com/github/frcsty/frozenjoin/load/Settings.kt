package com.github.frcsty.frozenjoin.load

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

object Settings {
    private val plugin = JavaPlugin.getPlugin(FrozenJoinPlugin::class.java)
    private val config = plugin.config

    val LOGGER = plugin.logger
    val DEBUG: Boolean = config.getString("consoleMessages", "ENABLED").equals("ENABLED", ignoreCase = true)
    val ALIASES: List<String> = config.getStringList("settings.alias")
    val METRICS: Boolean = config.getBoolean("stonks", true)
    private val version: Int = Integer.valueOf(Bukkit.getServer().javaClass.`package`.name.split("_")[1])
    val HEX_USE: Boolean = version == 16
}

fun logError(message: String): Unit = Settings.LOGGER.log(Level.WARNING, message)
fun logInfo(message: String): Unit = Settings.LOGGER.log(Level.INFO, message)