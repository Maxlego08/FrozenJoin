package com.github.frcsty.frozenjoin.listener

import com.github.frcsty.frozenjoin.listener.event.FrozenJoinEvent
import com.github.frcsty.frozenjoin.load.Loader
import com.github.frcsty.frozenjoin.message.MessageFormatter
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val loader: Loader) : Listener {

    companion object {
        private const val FIRST_JOIN = "firstJoin"
        private const val ACTION = "join"
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = ""

        val actionUtil = loader.actionUtil
        val manager = loader.formatManager
        val player = event.player

        if (!player.hasPlayedBefore()) {
            val motdObject = manager.motdsMap[FIRST_JOIN] ?: return
            val actions = motdObject.message
            actionUtil.executeActions(player, actions)
            Bukkit.getServer().pluginManager.callEvent(FrozenJoinEvent(player, actions))
            return
        }

        MessageFormatter.executeMotd(player, manager, actionUtil)
        val actions = MessageFormatter.executeFormat(player, manager, actionUtil, ACTION)
        Bukkit.getServer().pluginManager.callEvent(FrozenJoinEvent(player, actions))
    }
}