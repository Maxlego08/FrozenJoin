package com.github.frcsty.util

import com.github.frcsty.FrozenJoinPlugin
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

fun getNamespacedKey(type: String): NamespacedKey {
    return NamespacedKey(JavaPlugin.getPlugin(FrozenJoinPlugin::class.java), "frozenjoin-$type")
}

fun Player.setCustomMessage(type: String, message: String) {
    this.persistentDataContainer.set(getNamespacedKey(type), PersistentDataType.STRING, message)
}

fun Player.removeCustomMessage(type: String) {
    this.persistentDataContainer.set(getNamespacedKey(type), PersistentDataType.STRING, "emptyValue")
}

fun Player.getCustomMessage(type: String): String {
    return this.persistentDataContainer.get(getNamespacedKey(type), PersistentDataType.STRING) ?: return "emptyValue"
}