package com.github.frcsty.configuration

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.actions.util.color
import org.bukkit.configuration.ConfigurationSection

class MessageLoader(private val plugin: FrozenJoinPlugin) {

    private val messages = mutableMapOf<String, String?>()
    private val listMessages = mutableMapOf<String, List<String>?>()

    fun load() {
        val messageSection: ConfigurationSection = plugin.config.getConfigurationSection("messages")?: return

        for (message in messageSection.getKeys(false)) {
            if (messageSection.isList(message)) {
                listMessages[message] = messageSection.getStringList(message)
            } else {
                messages[message] = messageSection.getString(message)
            }
        }
    }

    fun getMessage(key: String): String {
        val message = messages[key] ?: key
        return message.color()
    }

    fun getMessageList(key: String): List<String> {
        return listMessages[key] ?: listOf(key)
    }
}