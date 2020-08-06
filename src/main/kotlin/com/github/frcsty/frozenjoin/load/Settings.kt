package com.github.frcsty.frozenjoin.load

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

object Settings {
    private val plugin = JavaPlugin.getPlugin(FrozenJoinPlugin::class.java)
    private val config = plugin.config
    private val server = Bukkit.getServer()

    val LOGGER = plugin.logger
    val DEBUG: Boolean = config.getString("consoleMessages", "ENABLED").equals("ENABLED", ignoreCase = true)
    val METRICS: Boolean = config.getBoolean("stonks", true)
    val FULL_VERSION = server.javaClass.`package`.name
    val VERSION: Int = Integer.valueOf(FULL_VERSION.split("_")[1])
    val HEX_USE: Boolean = VERSION == 16
    val PLUGIN_VERSION = plugin.description.version
    val LUCK_PERMS = server.pluginManager.getPlugin("LuckPerms")
    val USE_LUCK_PERMS = config.getBoolean("settings.luckperms-permissions")
}

fun logError(message: String): Unit = Settings.LOGGER.log(Level.WARNING, message)
fun logInfo(message: String): Unit = Settings.LOGGER.log(Level.INFO, message)