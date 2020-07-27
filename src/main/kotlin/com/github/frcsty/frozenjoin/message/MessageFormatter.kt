package com.github.frcsty.frozenjoin.message

import com.github.frcsty.frozenjoin.`object`.Format
import com.github.frcsty.frozenjoin.`object`.FormatManager
import com.github.frcsty.frozenjoin.`object`.MOTD
import com.github.frcsty.frozenjoin.action.ActionHandler
import com.github.frcsty.frozenjoin.extension.sendTranslatedMessage
import com.github.frcsty.frozenjoin.load.Settings
import com.github.frcsty.frozenjoin.load.logInfo
import org.bukkit.entity.Player
import java.util.*

object MessageFormatter {
    private val random = SplittableRandom()

    fun executeMotd(player: Player, manager: FormatManager, actionHandler: ActionHandler, command: Boolean, message: String) {
        val motds: Map<Int, MOTD> = manager.motdsMap.filter { (key, value) ->
            !("firstJoin".equals(key, true)) &&
                    player.hasEffectivePermission(value.permission)
        }.mapKeys {
            it.value.priority
        }.toSortedMap(Comparator.reverseOrder<Int>())

        val motd: Map.Entry<Int, MOTD>? = motds.entries.firstOrNull()

        if (motd == null) {
            if (command) {
                if (Settings.DEBUG) logInfo("Executor ${player.name} executed action 'motd'")
                player.sendTranslatedMessage(message)
            }
            return
        }
        val motdObject: MOTD = motd.value
        val actions: List<String> = motdObject.message

        actionHandler.execute(player, actions)
        if (Settings.DEBUG) logInfo("Executing '${manager.motdsMap.values.firstOrNull { it == motdObject }}' motd for user ${player.name} (${player.uniqueId})")
    }

    fun executeFormat(player: Player, manager: FormatManager, actionHandler: ActionHandler, action: String): List<String> {
        val formats: Map<Int, String> = manager.formatsMap.filter { (_, value) ->
            player.hasEffectivePermission(value.permission)
        }.map {
            it.value.priority to it.key
        }.toMap().toSortedMap(Comparator.reverseOrder<Int>())

        val format: Map.Entry<Int, String> = formats.entries.firstOrNull() ?: return emptyList()
        val formatObject: Format = manager.formatsMap[format.value] ?: return emptyList()
        val actions = if (action.equals("join", ignoreCase = true)) {
            formatObject.joinActions
        } else {
            formatObject.leaveActions
        }

        val type: String = formatObject.type

        if (actions.isEmpty()) {
            return emptyList()
        }

        val actionType = type.toUpperCase()

        when (actionType) {
            "NORMAL" -> {
                actionHandler.execute(player, actions)
            }
            "RANDOM" -> {
                actionHandler.execute(player, actions[random.nextInt(actions.size) - 1])
            }
            "VANISH" -> {
                val inverted: Boolean = formatObject.isInverted
                if (isVanished(player, inverted)) {
                    actionHandler.execute(player, actions)
                }
            }
        }

        if (Settings.DEBUG)
            logInfo("Executing '$actionType' action for user ${player.name} (${player.uniqueId})")

        return actions
    }

    private fun isVanished(player: Player, inverted: Boolean): Boolean {
        val isVanish = player.getMetadata("vanished").any { it.asBoolean() }
        return !inverted == isVanish
    }
}

private fun Player.hasEffectivePermission(permission: String): Boolean {
    return effectivePermissions.any { it.permission.equals(permission, true) }
}