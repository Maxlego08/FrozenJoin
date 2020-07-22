package com.github.frcsty.frozenjoin.action

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.action.actions.Action
import com.github.frcsty.frozenjoin.action.actions.BroadcastAction
import com.github.frcsty.frozenjoin.action.actions.MessageAction
import com.github.frcsty.frozenjoin.action.time.TimeAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Stream

class ActionHandler(private val plugin: FrozenJoinPlugin) {

    companion object {
        private val ACTION_PATTERN = Pattern.compile("(.*) ?\\[(?<action>[A-Z]+?)] ?(?<arguments>.+)", Pattern.CASE_INSENSITIVE)
        private val DELAY_PATTERN = Pattern.compile("\\[DELAY=(?<delay>\\d+[a-z])]", Pattern.CASE_INSENSITIVE)
        private val CHANCE_PATTERN = Pattern.compile("\\[CHANCE=(?<chance>\\d+)]", Pattern.CASE_INSENSITIVE)
        private val RANDOM = SplittableRandom()
    }

    private val actions: MutableMap<String, Action> = mutableMapOf()
    private var matcher: Matcher? = null
    private var chanceMatcher: Matcher? = null
    private var delayMatcher: Matcher? = null

    init {
        load()
    }

    fun execute(player: Player, input: List<String>) {
        input.forEach{ execute(player, it) }
    }

    fun execute(player: Player, input: String) {
        var input = hasChanceAction(input) ?: return
        val holder = getDelayAction(input)
        input = holder.action

        if (matcher == null) {
            matcher = ACTION_PATTERN.matcher(input)
        } else {
            matcher.reset(input)
        }

        if (!matcher.matches()) {
            println("Action does not matches regex $input")
            return
        }
        val arguments = matcher.group("arguments")
        val delay = holder.delay

        val action: Action = actions[matcher.group("action").toUpperCase()] ?: return
        Bukkit.getScheduler().runTaskLater(plugin, Runnable { action.run(player, arguments) }, delay)
    }

    private fun hasChanceAction(input: String): String? {
        if (chanceMatcher == null) {
            chanceMatcher = CHANCE_PATTERN.matcher(input)
        } else {
            chanceMatcher.reset(input)
        }
        if (!chanceMatcher.find()) {
            return input
        }
        val chance: Int = Integer.valueOf(chanceMatcher.group("chance"))
        val value: Int = RANDOM.nextInt(100) + 1
        return if (value <= chance) {
            input.replace(chanceMatcher.group(), "")
        } else null
    }

    private fun getDelayAction(input: String): ActionHolder {
        if (delayMatcher == null) {
            delayMatcher = DELAY_PATTERN.matcher(input)
        } else {
            delayMatcher.reset(input)
        }
        if (!delayMatcher.find()) {
            return ActionHolder(input, 0L)
        }
        val delay = delayMatcher.group("delay")
        return try {
            val time = TimeAPI(delay)
            ActionHolder(input.replace(delayMatcher.group(), ""), time.getSeconds() * 20L)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
            ActionHolder(input, 0L)
        }
    }

    private fun load() {
        Stream.of<Action>(
                MessageAction(),
                BroadcastAction()
        ).forEach { actions[it.id] = it }
    }
}