package com.github.frcsty.frozenjoin.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author AlexL
 */

fun Player.getRecipients(scope: String): Set<Player> {
    return when (scope) {
        "WORLD" /*TODO Make constant. */ -> {
            Bukkit.getOnlinePlayers().filter { it.world == world }.toSet()
        }
        else -> emptySet()
    }
}
