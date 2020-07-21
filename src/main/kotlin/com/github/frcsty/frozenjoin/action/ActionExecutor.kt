package com.github.frcsty.frozenjoin.action

import com.github.frcsty.frozenjoin.action.actions.Action
import com.github.frcsty.frozenjoin.action.actions.ActionData
import com.github.frcsty.frozenjoin.action.time.TimeAPI
import com.github.frcsty.frozenjoin.action.translator.TranslationException
import com.github.frcsty.frozenjoin.action.translator.Translator
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Consumer
import java.util.logging.Logger
import java.util.regex.Matcher
import java.util.regex.Pattern

internal class ActionExecutor(private val plugin: Plugin) {
    private val logger: Logger = plugin.logger
    private val actionDatas: MutableMap<String, ActionData> = HashMap()
    private val translators: MutableMap<Class<*>, Translator<*>> = HashMap()
    private val actionPattern = Pattern.compile("(.*) ?\\[(?<action>[A-Z]+?)] ?(?<arguments>.+)", Pattern.CASE_INSENSITIVE)
    private val chancePattern = Pattern.compile("\\[CHANCE=(?<chanceValue>\\d+)]", Pattern.CASE_INSENSITIVE)
    private val delayPattern = Pattern.compile("\\[DELAY=(?<delayValue>\\d+[a-z])]", Pattern.CASE_INSENSITIVE)
    private var actionMatcher: Matcher? = null
    private var chanceMatcher: Matcher? = null
    private var delayMatcher: Matcher? = null

    fun executeActions(player: Player, actions: List<String>) {
        actions.forEach(Consumer { action: String -> handleAction(player, action) })
    }

    private fun handleAction(player: Player, input: String) {
        actionMatcher = if (actionMatcher == null) actionPattern.matcher(input) else actionMatcher!!.reset(input)
        check(actionMatcher!!.matches()) { "action does not follow regex: $input" }
        if (!shouldExecuteChance(input)) {
            return
        }
        val action = actionMatcher!!.group("action").toUpperCase()
        val data = actionDatas[action] ?: throw IllegalStateException("action $action does not exist")
        val inputArguments = actionMatcher!!.group("arguments")
        val argSplit = inputArguments.split(";").dropLastWhile { it.isEmpty() }.toTypedArray()
        val arguments = getTranslatedArgs(player, argSplit, data.parameterTypes)
        val delay = getDelay(input)
        if (delay == 0L) {
            data.execute(player, arguments)
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, Runnable { data.execute(player, arguments) }, delay)
        }
    }

    private fun shouldExecuteChance(action: String): Boolean {
        chanceMatcher = if (chanceMatcher == null) chancePattern.matcher(action) else chanceMatcher!!.reset(action)
        if (!chanceMatcher!!.find()) {
            return true
        }
        val chance = chanceMatcher!!.group("chanceValue").toInt()
        val random = ThreadLocalRandom.current().nextInt(100) + 1
        return random <= chance
    }

    private fun getDelay(action: String): Long {
        delayMatcher = if (delayMatcher == null) delayPattern.matcher(action) else delayMatcher!!.reset(action)
        if (!delayMatcher!!.find()) {
            return 0L
        }
        val delay = delayMatcher!!.group("delayValue") ?: return 0L
        return try {
            val time = TimeAPI(delay)
            time.getSeconds().toLong() * 20
        } catch (ex: IllegalArgumentException) {
            logger.severe("$delay is not a valid time")
            0L
        }
    }

    private fun getTranslatedArgs(player: Player, inputs: Array<String>, parameterTypes: Array<out Class<*>>): Array<Any?> {
        check(inputs.size == parameterTypes.size) { String.format("entered arguments size does not match (given %d, expected %d)", inputs.size, parameterTypes.size) }
        val arguments = arrayOfNulls<Any>(parameterTypes.size + 1)
        arguments[0] = plugin
        for (i in inputs.indices) {
            val input = inputs[i]
            if (input == "") {
                arguments[i] = null
                continue
            }
            val translator = translators[parameterTypes[i]]
                    ?: throw IllegalStateException("translator does not exist for type $input")
            try {
                arguments[i + 1] = translator.translate(player, input)
            } catch (ex: TranslationException) {
                logger.severe("error while translating argument")
                ex.printStackTrace()
            }
        }
        return arguments
    }

    fun registerTranslator(translator: Translator<*>, vararg classes: Class<*>) {
        for (clazz in classes) {
            translators[clazz] = translator
        }
    }

    fun registerActionClass(key: String, actionClass: Class<out Action>, vararg parameterTypes: Class<*>) {
        try {
            val method = actionClass.getMethod("execute", *prependPlayerType(parameterTypes))
            actionDatas[key] = ActionData(key, method, parameterTypes)
        } catch (ex: NoSuchMethodException) {
            logger.severe("Unable to register action $key: missing execute(...) method")
        }
    }

    companion object {
        private fun prependPlayerType(array: Array<out Class<*>>): Array<Class<*>?> {
            val newArray: Array<Class<*>?> = arrayOfNulls(array.size + 2)
            newArray[0] = Player::class.java
            newArray[1] = Plugin::class.java
            System.arraycopy(array, 0, newArray, 2, array.size)
            return newArray
        }
    }

}