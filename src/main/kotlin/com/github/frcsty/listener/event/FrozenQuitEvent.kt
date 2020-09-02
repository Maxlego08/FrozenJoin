package com.github.frcsty.listener.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class FrozenQuitEvent(private val player: Player, private val actions: List<String>) : Event() {

    private val handlers = HandlerList()

    fun getHandlerList(): HandlerList? {
        return handlers
    }

    override fun getHandlers(): HandlerList {
        return handlers
    }

    fun getPlayer(): Player {
        return player
    }

    fun getActions(): List<String> {
        return actions
    }
}