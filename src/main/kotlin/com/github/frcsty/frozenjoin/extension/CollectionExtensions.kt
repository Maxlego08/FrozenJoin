package com.github.frcsty.frozenjoin.extension

import org.bukkit.entity.Player

fun Collection<Player>.sendOldActionBar(message: String) {
    this.forEach { }
}

fun Collection<Player>.sendActionBar(message: String) {
    this.forEach { it.sendActionBar(message.color()) }
}

