package com.github.frcsty.frozenjoin.convert

import com.github.frcsty.frozenjoin.load.Settings
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.logging.Level

class FileConverter {

    fun convertFile(file: File, dir: File) {
        val resultDirectory = File("$dir/converted")
        if (resultDirectory.exists().not()) {
            resultDirectory.mkdir()
        }

        val fileName = file.nameWithoutExtension
        if (file.extension != "yml") {
            Settings.LOGGER.log(Level.WARNING, "File $fileName")
            return
        }

        val startTime = System.currentTimeMillis()
        if (Settings.DEBUG) Settings.LOGGER.log(Level.INFO, "Converting file '$fileName.yml'")
        val resultFile = File("$dir/converted", "$fileName-converted.yml")
        val resultConfig = YamlConfiguration.loadConfiguration(resultFile)
        val config = YamlConfiguration.loadConfiguration(file)

        val firstJoin = config.getConfigurationSection("first_join")
        if (firstJoin == null) {
            Settings.LOGGER.log(Level.WARNING, "File $fileName does not contain 'first_join' section.")
        } else {
            resultConfig.set("firstJoinMessage", firstJoin.getStringList("actions"))
        }

        val motds = config.getConfigurationSection("motds")
        if (motds == null) {
            Settings.LOGGER.log(Level.WARNING, "File $fileName does not contain 'motds' section.")
        } else {
            for (motd in motds.getKeys(false)) {
                val oldSection = motds.getConfigurationSection(motd) ?: continue
                val newSection = resultConfig.createSection("motds.$motd")

                newSection.set("priority", oldSection.getInt("priority"))
                newSection.set("permission", "frozenjoin.motd.$motd")
                newSection.set("actions", oldSection.getStringList("motd_actions"))
            }
        }

        val formats = config.getConfigurationSection("deluxejoin_formats")
        if (formats == null) {
            Settings.LOGGER.log(Level.WARNING, "File $fileName does not contain 'deluxejoin_formats' section.")
        } else {
            for (format in formats.getKeys(false)) {
                val oldSection = formats.getConfigurationSection(format) ?: continue
                val newSection = resultConfig.createSection("formats.$format")

                newSection.set("priority", oldSection.getInt("priority"))
                newSection.set("type", "NORMAL")
                newSection.set("join", oldSection.getStringList("join_actions"))
                newSection.set("quit", oldSection.getStringList("leave_actions"))
                newSection.set("permission", "frozenjoin.$format")
            }
        }

        resultConfig.save(resultFile)
        if (file.delete().not()) {
            Settings.LOGGER.log(Level.WARNING, "Failed to delete file '$fileName'")
        }

        val estimatedTime = System.currentTimeMillis() - startTime
        if (Settings.DEBUG) Settings.LOGGER.log(Level.INFO, "Successfully converted file '$fileName.yml'! (New destination: FrozenJoin/converted/$fileName-converted.yml) Took ${estimatedTime}ms.")
    }
}