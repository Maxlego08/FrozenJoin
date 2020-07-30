package com.github.frcsty.frozenjoin.load

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.`object`.FormatManager
import com.github.frcsty.frozenjoin.action.ActionHandler
import com.github.frcsty.frozenjoin.command.*
import com.github.frcsty.frozenjoin.configuration.MessageLoader
import com.github.frcsty.frozenjoin.extension.color
import com.github.frcsty.frozenjoin.listener.PlayerJoinListener
import com.github.frcsty.frozenjoin.listener.PlayerQuitListener
import me.mattstudios.mf.base.CommandManager
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit.getServer
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import java.util.logging.Level

class Loader(private val plugin: FrozenJoinPlugin) {

    val actionHandler: ActionHandler = ActionHandler(plugin)
    val formatManager = FormatManager(plugin)
    private val messageLoader = MessageLoader(plugin)

    fun initialize() {
        plugin.saveDefaultConfig()
        messageLoader.load()

        if (Settings.METRICS) {
            val metrics = Metrics(plugin, 6743)
            if (metrics.isEnabled) {
                Settings.LOGGER.log(Level.INFO, "bStats Metrics Running!")
            }
        }

        if (Settings.HEX_USE) {
            logInfo("Hex support enabled! (#hex)")
        }

        val manager = CommandManager(plugin)
        manager.register(
                HelpCommand(messageLoader),
                InfoCommand(plugin, messageLoader),
                MotdCommand(
                        loader = this,
                        messageLoader = messageLoader),
                ReloadCommand(plugin, this, messageLoader),
                ConvertCommand(plugin, messageLoader)
        )

        formatManager.setFormats()

        registerMessages(manager, messageLoader)
        registerListeners(PlayerJoinListener(this), PlayerQuitListener(this))
    }

    private fun registerMessages(manager: CommandManager, messages: MessageLoader) {
        val handler = manager.messageHandler
        with(handler) {
            register("cmd.no.console") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("player-only-message").color())
            }
            register("cmd.no.permission") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("deny-message").color())
            }
            register("cmd.no.exists") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("unknown-command-message").color())
            }
            register("cmd.wrong.usage") { sender: CommandSender ->
                sender.sendMessage(messages.getMessage("usage-message").color())
            }
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
    }
}