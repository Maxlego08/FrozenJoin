package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.extension.sendActionBar
import com.github.frcsty.frozenjoin.extension.sendOldActionBar
import com.github.frcsty.frozenjoin.load.Settings
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class ActionbarMessageAction : Action {
    override val id = "ACTIONBARMESSAGE"

    override fun run(player: Player, data: String) {
        if (Settings.VERSION <= 8) {
            Bukkit.getServer().onlinePlayers.sendOldActionBar(message = data)
        } else {
            Collections.singletonList(player).sendActionBar(message = data)
        }
    }
}