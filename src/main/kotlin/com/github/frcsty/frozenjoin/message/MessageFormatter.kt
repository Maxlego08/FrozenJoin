package com.github.frcsty.frozenjoin.message

import com.github.frcsty.frozenjoin.`object`.FormatManager
import com.github.frcsty.frozenjoin.action.ActionHandler
import com.github.frcsty.frozenjoin.load.Settings
import com.github.frcsty.frozenjoin.load.logInfo
import com.github.frcsty.frozenjoin.util.getCustomMessage
import com.github.frcsty.frozenjoin.util.luckPermsCheck
import com.github.frcsty.frozenjoin.util.sendTranslatedMessage
import org.bukkit.entity.Player
import java.util.*

object MessageFormatter {
    fun executeMotd(player: Player, manager: FormatManager, actionHandler: ActionHandler, command: Boolean, message: String) {
        //This is not the most readable thing in the world, but I'm kind of scared to change anything since there's no test suite
        val motds = manager.motdsMap.filter { (key, value) ->
            !("firstJoin".equals(key, true)) && player.hasEffectivePermission(value.permission)
        }.mapKeys {
            it.value.priority
        }.toSortedMap(Comparator.reverseOrder<Int>())

        val motd = motds.entries.firstOrNull()
        if (motd == null) {
            if (command) {
                if (Settings.DEBUG) logInfo("Executor ${player.name} executed action 'motd'")
                player.sendTranslatedMessage(message)
            }
            return
        }
        val motdObject = motd.value
        val actions: List<String> = motdObject.message

        actionHandler.execute(player, actions)
        if (Settings.DEBUG) logInfo("Executing '${motdObject.identifier}' motd for user ${player.name} (${player.uniqueId})")
    }

    fun executeFormat(player: Player, manager: FormatManager, actionHandler: ActionHandler, action: String): List<String> {
        val customMessage = player.getCustomMessage(action)
        if (customMessage != "emptyValue") {
            actionHandler.execute(player, "[BROADCAST] $customMessage")
            if (Settings.DEBUG) logInfo("Executing custom message for user ${player.name} (${player.uniqueId})")
            return listOf(customMessage)
        }

        val formats = manager.formatsMap.filter { (_, value) ->
            player.hasEffectivePermission(value.permission)
        }.map {
            it.value.priority to it.key
        }.toMap().toSortedMap(Comparator.reverseOrder<Int>())

        val format = formats.entries.firstOrNull() ?: return emptyList()
        val formatObject = manager.formatsMap[format.value] ?: return emptyList()

        val actions = if (action.equals("join", ignoreCase = true)) {
            formatObject.joinActions
        } else {
            formatObject.leaveActions
        }

        val type = formatObject.type
        if (actions.isEmpty()) {
            return emptyList()
        }

        when (type.toUpperCase()) {
            "NORMAL" -> {
                actionHandler.execute(player, actions)
            }
            "RANDOM" -> {
                actionHandler.execute(player, actions.random())
            }
            "VANISH" -> {
                val inverted = formatObject.isInverted
                if (isVanished(player, inverted)) {
                    actionHandler.execute(player, actions)
                }
            }
        }

        if (Settings.DEBUG)
            logInfo("Executing '${formatObject.identifier}' format for user ${player.name} (${player.uniqueId})")

        return actions
    }

    private fun isVanished(player: Player, inverted: Boolean): Boolean {
        val isVanish = player.getMetadata("vanished").any { it.asBoolean() }
        return !inverted == isVanish
    }
}

private fun Player.hasEffectivePermission(permission: String): Boolean {
    if (Settings.LUCK_PERMS != null && Settings.LUCK_PERMS.isEnabled) {
        return this.luckPermsCheck(permission)
    }

    return effectivePermissions.any { it.permission.equals(permission, true) }
}
