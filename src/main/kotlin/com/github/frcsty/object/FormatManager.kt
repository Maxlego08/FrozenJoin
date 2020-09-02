package com.github.frcsty.`object`

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.load.logError

class FormatManager(private val plugin: FrozenJoinPlugin) {

    val formatsMap = mutableMapOf<String, Format>()
    val motdsMap = mutableMapOf<String, MOTD>()

    fun setFormats() {
        if (formatsMap.isNotEmpty()) {
            formatsMap.clear()
        }

        val config = plugin.config
        val formats = config.getConfigurationSection("formats")
        val motds = config.getConfigurationSection("motds")

        if (formats == null) {
            logError("Configuration section 'formats' is undefined!")
            return
        }
        if (motds == null) {
            logError("Configuration section 'motds' is undefined!")
            return
        }

        formats.getKeys(false).forEach { format ->
            val formatSection = formats.getConfigurationSection(format) ?: return@forEach
            formatsMap[format] = Format(
                    identifier = format,
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
                    identifier = motd,
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

