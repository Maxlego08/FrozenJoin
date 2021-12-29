package com.github.frcsty.util

import java.util.regex.Pattern
import me.clip.placeholderapi.PlaceholderAPI
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

private val LEGACY_HEX_PATTERN: Pattern = Pattern.compile("#<([A-Fa-f0-9]){6}>")
private val HEX_PATTERN: Pattern = Pattern.compile("#([A-Fa-f0-9]){6}")

fun String.color(): String {
    var translation = this.colorLegacy()

    var matcher = HEX_PATTERN.matcher(translation)

    while (matcher.find()) {
        val hex: ChatColor = ChatColor.of(matcher.group())
        val before = translation.substring(0, matcher.start())
        val after = translation.substring(matcher.end())

        translation = before + hex + after
        matcher = HEX_PATTERN.matcher(translation)
    }

    return ChatColor.translateAlternateColorCodes('&', translation)
}

fun String.colorLegacy(): String {
    var translation = this

    var matcher = LEGACY_HEX_PATTERN.matcher(translation)

    while (matcher.find()) {
        var hexString = matcher.group()

        hexString = "#" + hexString.substring(2, hexString.length - 1)
        val hex: ChatColor = ChatColor.of(hexString)
        val before = translation.substring(0, matcher.start())
        val after = translation.substring(matcher.end())

        translation = before + hex + after
        matcher = LEGACY_HEX_PATTERN.matcher(translation)
    }

    return translation
}

fun String.replacePlaceholder(placeholder: String, value: String): String {
    return this.replace(placeholder, value)
}

fun Player.sendTranslatedMessage(message: String) {
    this.spigot().sendMessage(*TextComponent.fromLegacyText(message.getTranslatedMessage(this).color()))
}

fun Player.sendTranslatedMessage(message: String, player: Player) {
    this.spigot().sendMessage(*TextComponent.fromLegacyText(message.getTranslatedMessage(player).color()))
}

fun CommandSender.sendTranslatedMessage(player: Player, message: String) {
    this.spigot().sendMessage(*TextComponent.fromLegacyText(message.getTranslatedMessage(player).color()))
}

fun String.getTranslatedMessage(player: Player): String {
    var message = this
    val daddy: Plugin? = Bukkit.getPluginManager().getPlugin("PlaceholderAPI")

    if (daddy != null && daddy.isEnabled) {
        message = PlaceholderAPI.setPlaceholders(player, message)
    }

    return message
}