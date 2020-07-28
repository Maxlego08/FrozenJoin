package com.github.frcsty.frozenjoin.action

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.action.actions.*
import com.github.frcsty.frozenjoin.action.time.TimeAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

private val ACTION_PATTERN = Regex("(.*) ?\\[(?<action>[A-Z]+?)] ?(?<arguments>.+)", RegexOption.IGNORE_CASE)
private val DELAY_PATTERN = Regex("\\[DELAY=(?<delay>\\d+[a-z])]", RegexOption.IGNORE_CASE)
private val CHANCE_PATTERN = Regex("\\[CHANCE=(?<chance>\\d+)]", RegexOption.IGNORE_CASE)
private val RANDOM = SplittableRandom()

class ActionHandler(private val plugin: FrozenJoinPlugin) {

    private val actions: Map<String, Action> = setOf(
            ActionbarBroadcastAction,
            ActionbarMessageAction,
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
    ).associateBy(Action::id)


    fun execute(player: Player, input: List<String>) {
        input.forEach { execute(player, it) }
    }

    fun execute(player: Player, input: String) {
        val actionHolder = getDelayAction(hasChanceAction(input) ?: return)
        val inputAction = actionHolder.action
        val match = ACTION_PATTERN.matchEntire(inputAction)

        if (match == null) {
            println("Action does not match regex $inputAction")
            return
        }

        val arguments = match.groups["arguments"]?.value ?: return
        val delay = actionHolder.delay

        val actionName = match.groups["action"]?.value?.toUpperCase()

        val action = actions[actionName] ?: return

        Bukkit.getScheduler().runTaskLater(
                plugin,
                Runnable { action.run(player, arguments) },
                delay
        )
    }

    /*
    TODO the contract of this function is a bit vague. If there's no match, we return the input,
    but then we return null based on if a chance succeeded or not?
     */
    private fun hasChanceAction(input: String): String? {
        val match = CHANCE_PATTERN.matchEntire(input) ?: return input
        val chanceGroup = match.groups["chance"] ?: return null
        val chance = chanceGroup.value.toInt()

        val randomValue = RANDOM.nextInt(100) + 1

        return if (randomValue <= chance) {
            input.replace(chanceGroup.value, "")
        } else null
    }

    private fun getDelayAction(input: String): ActionHolder {
        val match = DELAY_PATTERN.matchEntire(input) ?: return ActionHolder(input, 0L)

        val delayGroup = match.groups["delay"] ?: return ActionHolder(input, 0L)
        val delay = delayGroup.value

        return try {
            val time = TimeAPI(delay) //this line is kinda gross...
            ActionHolder(
                    action = input.replace(delayGroup.value, ""),
                    delay = time.seconds * 20L
            )
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
            ActionHolder(input, 0L)
        }
    }
}
