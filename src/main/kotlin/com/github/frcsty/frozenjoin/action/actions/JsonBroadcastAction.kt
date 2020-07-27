package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class JsonBroadcastAction : Action {

    override val id = "JSONBROADCAST";

    override fun run(player: Player, data: String) {
        Bukkit.getServer().onlinePlayers.forEach { Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw ${it.name} $data") }
    }
}