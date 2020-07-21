package com.github.frcsty.frozenjoin.action

import com.github.frcsty.frozenjoin.action.actions.*
import com.github.frcsty.frozenjoin.action.translator.*
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class ActionUtil private constructor(plugin: Plugin) {
    private val executor: ActionExecutor = ActionExecutor(plugin)

    fun executeActions(player: Player, vararg actions: String) {
        executor.executeActions(player, listOf(*actions))
    }

    fun executeActions(player: Player, actions: List<String>) {
        executor.executeActions(player, actions)
    }

    private fun registerTranslators() { // Java Types
        registerTranslator(StringTranslator(), String::class.java)
        registerTranslator(BooleanTranslator(), Boolean::class.java)
        registerTranslator(IntTranslator(), Int::class.java, Int::class.java)
        registerTranslator(DecimalTranslator(), Double::class.java, Double::class.java)
        // Bukkit Types
        registerTranslator(SoundTranslator(), Sound::class.java)
        registerTranslator(WorldTranslator(), World::class.java)
    }

    private fun registerActionClasses() {
        registerActionClass("MESSAGE", MessageAction::class.java, String::class.java)
        registerActionClass("BROADCAST", BroadcastAction::class.java, String::class.java)
        registerActionClass("CENTERMESSAGE", CenterMessageAction::class.java, String::class.java)
        registerActionClass("CENTERBROADCAST", CenterBroadcastAction::class.java, String::class.java)
        registerActionClass("JSONMESSAGE", JsonMessageAction::class.java, String::class.java)
        registerActionClass("JSONBROADCAST", JsonBroadcastAction::class.java, String::class.java)
        registerActionClass("PLAYERCOMMAND", PlayerCommandAction::class.java, String::class.java)
        registerActionClass("CONSOLECOMMAND", ConsoleCommandAction::class.java, String::class.java)
        registerActionClass("SOUND", SoundAction::class.java, Sound::class.java, Double::class.java, Double::class.java)
        registerActionClass("SOUNDBROADCAST", SoundBroadcastAction::class.java, Sound::class.java, Double::class.java, Double::class.java)
        registerActionClass("ACTIONBARMESSAGE", ActionbarMessageAction::class.java, String::class.java)
        registerActionClass("ACTIONBARBROADCAST", ActionbarBroadcastAction::class.java, String::class.java)
        registerActionClass("TITLEMESSAGE", TitleMessageAction::class.java, String::class.java, String::class.java, Int::class.java, Int::class.java, Int::class.java)
        registerActionClass("TITLEBROADCAST", TitleBroadcastAction::class.java, String::class.java, String::class.java, Int::class.java, Int::class.java, Int::class.java)
        registerActionClass("BUNGEE", BungeeAction::class.java, String::class.java)
        registerActionClass("TELEPORT", TeleportAction::class.java, World::class.java, Double::class.java, Double::class.java, Double::class.java, Float::class.java, Float::class.java)
    }

    private fun registerTranslator(translator: Translator<*>, vararg classes: Class<*>) {
        executor.registerTranslator(translator, *classes)
    }

    private fun registerActionClass(key: String, actionClass: Class<out Action>, vararg parameterTypes: Class<*>) {
        executor.registerActionClass(key, actionClass, *parameterTypes)
    }

    companion object {
        fun init(plugin: Plugin): ActionUtil {
            return ActionUtil(plugin)
        }
    }

    init {
        registerTranslators()
        registerActionClasses()
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord")
    }
}