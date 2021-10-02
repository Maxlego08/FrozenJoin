package com.github.frcsty.position

import com.github.frcsty.FrozenJoinPlugin
import java.io.File
import java.util.SortedMap
import java.util.UUID
import org.bukkit.configuration.file.YamlConfiguration

object PositionStorage {

    val positions: SortedMap<UUID, Long> = sortedMapOf()

    fun initialize(plugin: FrozenJoinPlugin) {
        val file = File("${plugin.dataFolder}/positions.yml")

        if (file.exists().not()) {
            file.createNewFile()
            return
        }

        val fileConfiguration = YamlConfiguration.loadConfiguration(file)
        val section = fileConfiguration.getConfigurationSection("positions")?: return

        for (position in section.getKeys(false)) {
            positions[UUID.fromString(position)] = section.getLong(position)
        }

        fileConfiguration.save(file)
    }

    fun terminate(plugin: FrozenJoinPlugin) {
        val file = File("${plugin.dataFolder}/positions.yml")

        if (file.exists().not()) {
            return
        }

        val fileConfiguration = YamlConfiguration.loadConfiguration(file)

        if (positions.isEmpty()) {
            return
        }

        for (position in positions.keys) {
            fileConfiguration.set("positions.$position", positions[position])
        }

        fileConfiguration.save(file)
        positions.clear()
    }

}