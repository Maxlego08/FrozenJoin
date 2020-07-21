package com.github.frcsty.frozenjoin.action.translator

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class StringTranslator : Translator<String?> {
    private val placeholderApi: Boolean = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null

    @Throws(TranslationException::class)
    override fun translate(player: Player, input: String): String {
        var message = ChatColor.translateAlternateColorCodes('&', input)
        message = message.replace("<nl>", System.lineSeparator())
        message = if (placeholderApi) PlaceholderAPI.setPlaceholders(player, message) else message.replace("%player_name%", player.name)
        return message
    }
}