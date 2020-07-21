package com.github.frcsty.frozenjoin.action.util

import com.google.common.io.ByteStreams
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

object UtilServer {
    fun writeToBungee(player: Player, plugin: Plugin, vararg args: String) {
        val output = ByteStreams.newDataOutput()
        Arrays.stream(args).forEach { s: String -> output.writeUTF(s) }
        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray())
    }
}