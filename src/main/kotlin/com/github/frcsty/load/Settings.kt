package com.github.frcsty.load

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.actions.load.Settings

class Settings(private val plugin: FrozenJoinPlugin) : Settings {
    override val cacheUpdateInterval = plugin.config.getLong("placeholder-cache.update-interval", 20L)
    override var cachedPlaceholders: List<String> = plugin.config.getStringList("placeholder-cache.placeholders")
    override val debug = plugin.config.getString("consoleMessages", "ENABLED").equals("ENABLED", ignoreCase = true)
    override val metrics = plugin.config.getBoolean("stonks", true)

    override val logger = plugin.logger
    override val pluginVersion = plugin.description.version

    fun reload() {
        cachedPlaceholders = plugin.config.getStringList("placeholder-cache.placeholders")
    }
}