package com.github.frcsty.listener.base

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.listener.event.FrozenQuitEvent
import com.github.frcsty.load.Loader
import com.github.frcsty.message.MessageFormatter
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(private val plugin: FrozenJoinPlugin, private val loader: Loader) : Listener {

    companion object {
        private const val ACTION = "quit"
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        event.quitMessage = ""

        val manager = loader.formatManager
        val actionHandler = loader.actionHandler
        val player = event.player

        val actions = MessageFormatter.executeFormat(
            player = player,
            manager = manager,
            actionHandler = actionHandler,
            settings = loader.settings,
            action = ACTION,
            plugin = plugin
        )
        Bukkit.getServer().pluginManager.callEvent(FrozenQuitEvent(player, actions))
    }
}