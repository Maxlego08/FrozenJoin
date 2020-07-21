package com.github.frcsty.frozenjoin.action.actions

import org.bukkit.Bukkit

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

object ConsoleCommandAction : Action {
    fun execute(player: Player, plugin: Plugin, action: String) = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action)
}