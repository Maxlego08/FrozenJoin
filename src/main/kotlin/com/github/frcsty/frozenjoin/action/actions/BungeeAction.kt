@file:Suppress("UnstableApiUsage")

package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.google.common.io.ByteStreams
import org.bukkit.entity.Player
import java.util.*

object BungeeAction : Action {

    override val id = "BUNGEE"

    override fun run(plugin: FrozenJoinPlugin, player: Player, data: String) {
        val output = ByteStreams.newDataOutput()
        Arrays.stream<String>(arrayOf("Connect", data)).forEach { s: String -> output.writeUTF(s) }

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray())
    }
}
