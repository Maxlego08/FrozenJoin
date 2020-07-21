package com.github.frcsty.frozenjoin.command

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.extension.color
import com.github.frcsty.frozenjoin.load.Settings
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable
import java.util.logging.Level

@Command("frozenjoin")
class ReloadCommand(private val plugin: FrozenJoinPlugin): CommandBase() {

    companion object {
        private const val COMMAND: String = "reload"
        private const val PERMISSION: String = "join.command.base"
    }

    @SubCommand(COMMAND)
    @Permission(PERMISSION)
    fun reloadCommand(sender: CommandSender) {
        val startTime = System.currentTimeMillis()
        val messages = plugin.config.getConfigurationSection("messages")

        if (messages == null) {
            Settings.LOGGER.log(Level.WARNING, "Configuration section 'messages' not found!")
            return
        }

        val message = messages.getString("reload-message")

        if (message == null) {
            Settings.LOGGER.log(Level.WARNING, "Configuration message 'messages.reload-message' is incomplete!")
            return
        }

        val estimatedTime = System.currentTimeMillis() - startTime
        sender.sendMessage((message.replace("%time%", estimatedTime.toString()).color()))

        if (Settings.DEBUG) Settings.LOGGER.log(Level.INFO, String.format("Executor %s executed action 'reload'", sender.name))
    }

    init {
        object : BukkitRunnable() {
            override fun run() {
                plugin.reloadConfig()

            }
        }.runTaskAsynchronously(plugin)
    }
}