package com.github.frcsty.load

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.`object`.FormatManager
import com.github.frcsty.command.*
import com.github.frcsty.configuration.MessageLoader
import com.github.frcsty.library.ActionHandler
import com.github.frcsty.listener.base.PlayerJoinListener
import com.github.frcsty.listener.base.PlayerQuitListener
import com.github.frcsty.placeholder.PositionPlaceholder
import com.github.frcsty.position.PositionStorage
import com.github.frcsty.util.color
import me.mattstudios.mf.base.CommandManager
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit.getServer
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener

class Loader(private val plugin: FrozenJoinPlugin) {

    val actionHandler = ActionHandler(plugin)
    val formatManager = FormatManager(plugin)
    val positionStorage = PositionStorage
    private val messageLoader = MessageLoader(plugin)

    fun initialize() {
        plugin.saveDefaultConfig()
        positionStorage.initialize(plugin)
        messageLoader.load()

        if (Settings.METRICS) {
            val metrics = Metrics(plugin, 6743)
        }

        val manager = CommandManager(plugin)

        registerCompletions(
            manager,
            Pair("#format-argument", listOf("join", "quit")),
            Pair("#format-message", listOf("<message>"))
        )

        manager.register(
                HelpCommand(messageLoader),
                InfoCommand(messageLoader),
                MotdCommand(
                        loader = this,
                        messageLoader = messageLoader,
                        plugin = plugin),
                ReloadCommand(plugin, this, messageLoader),
                ConvertCommand(plugin, messageLoader)
        )

        actionHandler.loadDefault()
        manager.register(FormatCommand(messageLoader))

        formatManager.setFormats()

        registerMessages(manager, messageLoader)
        registerListeners(PlayerJoinListener(this, plugin), PlayerQuitListener(this))

        PositionPlaceholder(this).register()
    }

    private fun registerMessages(manager: CommandManager, messages: MessageLoader) {
        val handler = manager.messageHandler
        with(handler) {
            register("cmd.no.console") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("playerOnlyMessage").color())
            }
            register("cmd.no.permission") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("denyMessage").color())
            }
            register("cmd.no.exists") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("unknownCommandMessage").color())
            }
            register("cmd.wrong.usage") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("usageMessage").color())
            }
        }
    }

    private fun registerCompletions(manager: CommandManager, vararg completions: Pair<String, List<String>>) {
        completions.forEach { pair ->
            manager.completionHandler.register(pair.first) { pair.second }
        }
    }

    private fun registerListeners(vararg listeners: Listener) {
        listeners.forEach { listener: Listener? ->
            if (listener != null)
                getServer().pluginManager.registerEvents(listener, plugin)
        }
    }

    fun terminate() {
        plugin.reloadConfig()
        positionStorage.terminate(plugin)
    }
}