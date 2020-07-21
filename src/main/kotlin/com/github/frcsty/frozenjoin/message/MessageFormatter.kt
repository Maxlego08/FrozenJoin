package com.github.frcsty.frozenjoin.message

import com.github.frcsty.frozenjoin.`object`.Format
import com.github.frcsty.frozenjoin.`object`.FormatManager
import com.github.frcsty.frozenjoin.`object`.MOTD
import com.github.frcsty.frozenjoin.action.ActionUtil
import com.github.frcsty.frozenjoin.load.Settings
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionAttachmentInfo
import java.util.*
import java.util.function.Consumer
import java.util.logging.Level

class MessageFormatter {
    companion object {
        private val random = SplittableRandom()

        fun executeMotd(player: Player, manager: FormatManager, actionUtil: ActionUtil) {
            var motds: MutableMap<Int?, String?> = HashMap()

            for (motd in manager.motdsMap.keys) {

                if (motd.equals("firstJoin", ignoreCase = true)) {
                    continue
                }

                val motdObject: MOTD = manager.motdsMap[motd] ?: continue

                val permission: String = motdObject.permission ?: return
                val priority: Int = motdObject.priority
                handlePermission(priority, motd, permission, motds, player)
            }

            motds = motds.toSortedMap(Comparator.reverseOrder<Int>())

            if (motds.isEmpty()) {
                return
            }

            val motd: Map.Entry<Int?, String?> = motds.entries.iterator().next()
            val motdObject: MOTD = manager.motdsMap[motd.value] ?: return
            val actions: List<String> = motdObject.message

            actionUtil.executeActions(player, actions)
            if (Settings.DEBUG) Settings.LOGGER.log(Level.INFO, String.format("Executing '%s' motd for user %s (%s)", motd.value, player.name, player.uniqueId))
        }

        fun executeFormat(player: Player, manager: FormatManager, actionUtil: ActionUtil, action: String): List<String> {
            var formats: MutableMap<Int?, String?> = HashMap()

            for (format in manager.formatsMap.keys) {
                val formatObject: Format = manager.formatsMap[format] ?: continue
                val permission: String = formatObject.permission ?: return Collections.emptyList()
                val priority: Int = formatObject.priority
                handlePermission(priority, format, permission, formats, player)
            }

            formats = formats.toSortedMap(Comparator.reverseOrder<Int>())

            if (formats.isEmpty()) {
                return Collections.emptyList()
            }

            val format: Map.Entry<Int?, String?> = formats.entries.iterator().next()
            val formatObject: Format = manager.formatsMap[format.value] ?: return Collections.emptyList()
            val actions: List<String>
            actions = if (action.equals("join", ignoreCase = true)) {
                formatObject.joinActions
            } else {
                formatObject.leaveActions
            }

            val type: String = formatObject.type ?: return Collections.emptyList()
            if (actions.isEmpty()) {
                return Collections.emptyList()
            }

            var actionType = ""
            when (type.toUpperCase()) {
                "NORMAL" -> {
                    actionType = "NORMAL"
                    actionUtil.executeActions(player, actions)
                }
                "RANDOM" -> {
                    actionType = "RANDOM"
                    actionUtil.executeActions(player, actions[random.nextInt(actions.size) - 1])
                }
                "VANISH" -> {
                    actionType = "VANISH"
                    val inverted: Boolean = formatObject.isInverted
                    if (isVanished(player, inverted)) {
                        actionUtil.executeActions(player, actions)
                    }
                }
            }

            if (Settings.DEBUG) Settings.LOGGER.log(Level.INFO, String.format("Executing '%s' action for user %s (%s)", actionType, player.name, player.uniqueId))
            return actions
        }

        private fun handlePermission(key: Int, value: String, permission: String, map: MutableMap<Int?, String?>, player: Player) {
            player.effectivePermissions.forEach(Consumer { perm: PermissionAttachmentInfo -> if (perm.permission.equals(permission, ignoreCase = true)) map[key] = value })
        }

        private fun isVanished(player: Player, inverted: Boolean): Boolean {
            var isVanish = false

            for (meta in player.getMetadata("vanished")) {
                if (meta.asBoolean()) isVanish = true
            }

            return if (inverted) !isVanish
            else isVanish
        }
    }
}