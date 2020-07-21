package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.action.util.UtilPlayer.sendActionbar
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

object ActionbarMessageAction : Action {
    fun execute(player: Player, plugin: Plugin, msg: String) = sendActionbar(Collections.singletonList(player), msg)
}