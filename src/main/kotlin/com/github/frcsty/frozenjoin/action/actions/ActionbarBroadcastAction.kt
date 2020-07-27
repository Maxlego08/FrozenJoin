package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.extension.sendActionBar
import com.github.frcsty.frozenjoin.extension.sendOldActionBar
import com.github.frcsty.frozenjoin.load.Settings
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ActionbarBroadcastAction : Action {
    override val id = "ACTIONBARBROADCAST"

    override fun run(player: Player, data: String) {
        if (Settings.VERSION <= 8) {
            Bukkit.getServer().onlinePlayers.sendOldActionBar(message = data)
        } else {
            Bukkit.getServer().onlinePlayers.sendActionBar(message = data)
        }
    }
}