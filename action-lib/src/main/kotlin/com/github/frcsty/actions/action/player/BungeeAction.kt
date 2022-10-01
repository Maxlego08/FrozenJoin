@file:Suppress("UnstableApiUsage")

package com.github.frcsty.actions.action.player

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.cache.PlaceholderCache
import com.google.common.io.ByteStreams
import java.util.Arrays
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object BungeeAction : Action {

    override val id = "BUNGEE"

    override fun run(plugin: Plugin, player: Player, data: String, cache: PlaceholderCache?) {
        val output = ByteStreams.newDataOutput()
        Arrays.stream(arrayOf("Connect", data)).forEach { s: String -> output.writeUTF(s) }

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray())
    }
}