@file:Suppress("UnstableApiUsage")

package com.github.frcsty.library.actions.player

import com.github.frcsty.library.actions.Action
import com.google.common.io.ByteStreams
import java.util.Arrays
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object BungeeAction : Action {

    override val id = "BUNGEE"

    override fun run(plugin: Plugin, player: Player, data: String) {
        val output = ByteStreams.newDataOutput()
        Arrays.stream<String>(arrayOf("Connect", data)).forEach { s: String -> output.writeUTF(s) }

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray())
    }
}