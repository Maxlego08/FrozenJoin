package com.github.frcsty.frozenjoin.action

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.action.actions.*
import com.github.frcsty.frozenjoin.action.time.TimeAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

private val ACTION_PATTERN = Pattern.compile("(.*) ?\\[(?<action>[A-Z]+?)] ?(?<arguments>.+)", Pattern.CASE_INSENSITIVE)
private val DELAY_PATTERN = Pattern.compile("\\[DELAY=(?<delay>\\d+[a-z])]", Pattern.CASE_INSENSITIVE)
private val CHANCE_PATTERN = Pattern.compile("\\[CHANCE=(?<chance>\\d+)]", Pattern.CASE_INSENSITIVE)
private val RANDOM = SplittableRandom()

class ActionHandler(private val plugin: FrozenJoinPlugin) {

    private val actions: Map<String, Action> = setOf(
            ActionbarBroadcastAction(),
            ActionbarMessageAction(),
            BroadcastAction(),
            BungeeAction(),
            CenterBroadcastAction(),
            CenterMessageAction(),
            ConsoleCommandAction(),
            EquipItemAction(),
            JsonBroadcastAction(),
            JsonMessageAction(),
            MessageAction(),
            PlayerCommandAction(),
            SoundAction(),
            SoundBroadcastAction(),
            TeleportAction(),
            TitleBroadcastAction(),
            TitleMessageAction()
    ).associateBy { it.id }

    private val matcher: Matcher = ACTION_PATTERN.matcher("")
    private val chanceMatcher: Matcher = CHANCE_PATTERN.matcher("")
    private val delayMatcher: Matcher = DELAY_PATTERN.matcher("")

    fun execute(player: Player, input: List<String>) {
        input.forEach { execute(player, it) }
    }

    fun execute(player: Player, input: String) {
        val actionHolder = getDelayAction(
                hasChanceAction(input) ?: return
        )
        val inputAction = actionHolder.action
        matcher.reset(inputAction)

        if (!matcher.matches()) {
            println("Action does not matches regex $inputAction")
            return
        }

        val arguments = matcher.group("arguments")
        val delay = actionHolder.delay

        val actionName = matcher.group("action").toUpperCase()

        val action: Action = actions[actionName] ?: return
        Bukkit.getScheduler().runTaskLater(
                plugin,
                Runnable { action.run(player, arguments) },
                delay
        )
    }

    private fun hasChanceAction(input: String): String? {
        chanceMatcher.reset(input)
        if (!chanceMatcher.find()) {
            return input
        }
        val chance: Int = Integer.valueOf(
                chanceMatcher.group("chance")
        )
        val value: Int = RANDOM.nextInt(100) + 1
        return if (value <= chance) {
            input.replace(chanceMatcher.group(), "")
        } else null
    }

    private fun getDelayAction(input: String): ActionHolder {
        delayMatcher.reset(input)
        if (!delayMatcher.find()) {
            return ActionHolder(input, 0L)
        }
        val delay = delayMatcher.group("delay")
        return try {
            val time = TimeAPI(delay)
            ActionHolder(
                    action = input.replace(delayMatcher.group(), ""),
                    delay = time.seconds * 20L
            )
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
            ActionHolder(input, 0L)
        }
    }
}