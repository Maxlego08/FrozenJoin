package com.github.frcsty.frozenjoin.`object`

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.load.logError

class FormatManager(private val plugin: FrozenJoinPlugin) {

    val formatsMap: MutableMap<String, Format> = HashMap()
    val motdsMap: MutableMap<String, MOTD> = HashMap()

    fun setFormats() {
        if (formatsMap.isNotEmpty()) {
            formatsMap.clear()
        }

        val config = plugin.config
        val formats = config.getConfigurationSection("formats")
        val motds = config.getConfigurationSection("motds")

        when {
            formats == null -> {
                logError("Configuration section 'formats' is undefined!")
                return
            }
            motds == null -> {
                logError("Configuration section 'motds' is undefined!")
                return
            }
            else -> Unit
        }

        formats.getKeys(false).forEach { format ->
            val formatSection = formats.getConfigurationSection(format) ?: return@forEach
            formatsMap[format] = Format(
                    priority = formatSection.getInt("priority"),
                    type = formatSection.getString("type") ?: "",
                    permission = formatSection.getString("permission") ?: "frozenjoin.format.$format",
                    joinActions = formatSection.getStringList("join"),
                    leaveActions = formatSection.getStringList("quit"),
                    isInverted = formatSection.getBoolean("inverted")
            )
        }
        motds.getKeys(false).forEach { motd ->
            val motdSection = motds.getConfigurationSection(motd) ?: return@forEach
            motdsMap[motd] = MOTD(
                    message = motdSection.getStringList("actions"),
                    priority = motdSection.getInt("priority"),
                    permission = motdSection.getString("permission") ?: "frozenjoin.motd.$motd"
            )
        }

        motdsMap["firstJoin"] = MOTD(
                message = plugin.config.getStringList("firstJoinMessage")
        )
    }


}

