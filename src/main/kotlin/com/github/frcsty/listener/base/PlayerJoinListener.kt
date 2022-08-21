package com.github.frcsty.listener.base

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.listener.event.FrozenJoinEvent
import com.github.frcsty.load.Loader
import com.github.frcsty.message.MessageFormatter
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val loader: Loader, private val plugin: FrozenJoinPlugin) : Listener {

    companion object {
        private const val FIRST_JOIN = "firstJoin"
        private const val ACTION = "join"
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = ""

        val actionHandler = loader.actionHandler
        val manager = loader.formatManager
        val player = event.player

        if (loader.positionStorage.positions[player.uniqueId] == null) {
            loader.positionStorage.positions[player.uniqueId] = (loader.positionStorage.positions.size + 1).toLong()
        }

        if (!player.hasPlayedBefore()) {
            val motdObject = manager.motdsMap[FIRST_JOIN] ?: return
            val actions = motdObject.message
            actionHandler.execute(player, actions)
            Bukkit.getServer().pluginManager.callEvent(FrozenJoinEvent(player, actions))
            return
        }

        MessageFormatter.executeMotd(player, manager, actionHandler, command = false, message = "", plugin, loader.placeholderCache)
        val actions = MessageFormatter.executeFormat(player, manager, actionHandler, ACTION)
        Bukkit.getServer().pluginManager.callEvent(FrozenJoinEvent(player, actions))
    }

}