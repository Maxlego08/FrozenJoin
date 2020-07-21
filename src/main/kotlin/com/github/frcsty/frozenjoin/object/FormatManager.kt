package com.github.frcsty.frozenjoin.`object`

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.load.Settings
import java.util.logging.Level

class FormatManager(private val plugin: FrozenJoinPlugin) {

    val formatsMap: MutableMap<String, Format> = HashMap()
    val motdsMap: MutableMap<String, MOTD> = HashMap()

    fun setFormats() {
        if (formatsMap.isEmpty().not()) {
            formatsMap.clear()
        }

        val config = plugin.config
        val formats = config.getConfigurationSection("formats")
        val motds = config.getConfigurationSection("motds")

        if (formats == null) {
            Settings.LOGGER.log(Level.WARNING, "Configuration section 'formats' is undefined!")
            return
        }
        if (motds == null) {
            Settings.LOGGER.log(Level.WARNING, "Configuration section 'motds' is undefined!")
            return
        }

        for (format in formats.getKeys(false)) {
            val formatSection = formats.getConfigurationSection(format)?: continue

            val newFormat = Format.create()
                    .withPriority(formatSection.getInt("priority"))
                    .withType(formatSection.getString("type"))
                    .withPermission(formatSection.getString( "permission"))
                    .withJoinActions(formatSection.getStringList("join"))
                    .withLeaveActions(formatSection.getStringList("quit"))

            if (formatSection.get("inverted") != null) {
                newFormat.isInverted(formatSection.getBoolean("inverted"))
            }
            formatsMap[format] = newFormat
        }

        for (motd in motds.getKeys(false)) {
            val motdSection = motds.getConfigurationSection(motd)?: continue

            val newMotd = MOTD.create()
                    .withMessage(motdSection.getStringList("actions"))
                    .withPriority(motdSection.getInt("priority"))
                    .withPermission(motdSection.getString("permission"))

            motdsMap[motd] = newMotd
        }

        motdsMap["firstJoin"] = MOTD.create().withMessage(plugin.config.getStringList("firstJoinMessage"))
    }

}