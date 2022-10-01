package com.github.frcsty.actions

import com.github.frcsty.actions.action.Action
import com.github.frcsty.actions.action.broadcast.*
import com.github.frcsty.actions.action.player.*
import com.github.frcsty.actions.load.Loader
import com.github.frcsty.actions.load.Settings
import com.github.frcsty.actions.time.parseTime
import java.util.SplittableRandom
import java.util.concurrent.TimeUnit
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

private val ACTION_PATTERN = Regex("(.*) ?\\[(?<action>[A-Z]+?)] ?(?<arguments>.+)", RegexOption.IGNORE_CASE)
private val DELAY_PATTERN = Regex("\\[DELAY=(?<delay>\\d+[a-z]+)]", RegexOption.IGNORE_CASE)
private val CHANCE_PATTERN = Regex("\\[CHANCE=(?<chance>\\d+)]", RegexOption.IGNORE_CASE)
private val RANDOM = SplittableRandom()

class ActionHandler(private val plugin: Plugin, private val loader: Loader, private val settings: Settings) {

    val actions = mutableMapOf<String, Action>()

    fun loadDefault() {
        setOf(
                ActionbarBroadcastAction,
                ActionbarMessageAction,
                AudienceBroadcastAction,
                BroadcastAction,
                BungeeAction,
                CenterBroadcastAction,
                CenterMessageAction,
                ConsoleCommandAction,
                EquipItemAction,
                JsonBroadcastAction,
                JsonMessageAction,
                MessageAction,
                PlayerCommandAction,
                SoundAction,
                SoundBroadcastAction,
                TeleportAction,
                TitleBroadcastAction,
                TitleMessageAction
        ).forEach { this.actions[it.id] = it }
    }

    fun setAction(identifier: String, action: Action) {
        this.actions[identifier] = action
    }

    fun execute(player: Player, input: List<String>) {
        input.forEach { execute(player, it) }
    }

    private fun execute(player: Player, input: String) {
        val actionHolder = getDelayAction(hasChanceAction(input) ?: return)
        val inputAction = actionHolder.action
        val match = ACTION_PATTERN.matchEntire(inputAction)

        if (match == null) {
            if (settings.debug) plugin.logger.info("Action does not match regex: $inputAction")
            return
        }

        val arguments = match.groups["arguments"]?.value ?: return
        val delay = actionHolder.delay

        val actionName = match.groups["action"]?.value?.uppercase()

        val action = actions[actionName] ?: return

        Bukkit.getScheduler().runTaskLater(
                plugin,
                Runnable {
                    action.run(player, arguments, loader.placeholderCache)
                    action.run(plugin, player, arguments, loader.placeholderCache)
                },
                delay
        )
    }

    private fun hasChanceAction(input: String): String? {
        val match = CHANCE_PATTERN.find(input) ?: return input
        val chanceGroup = match.groups["chance"] ?: return null
        val chance = chanceGroup.value.toInt()

        val randomValue = RANDOM.nextInt(100) + 1

        return if (randomValue <= chance) {
            input.replaceFirst(CHANCE_PATTERN, "")
        } else null
    }

    private fun getDelayAction(input: String): ActionHolder {
        val match = DELAY_PATTERN.find(input) ?: return ActionHolder(input, 0L)

        val delayGroup = match.groups["delay"] ?: return ActionHolder(input, 0L)
        val delay = delayGroup.value

        return try {
            val time = delay.parseTime()
            ActionHolder(
                action = input.replaceFirst(DELAY_PATTERN, ""),
                delay = time.to(TimeUnit.SECONDS) * 20L
            )
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
            ActionHolder(input, 0L)
        }
    }

    init {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord")
    }
}