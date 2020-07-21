package com.github.frcsty.frozenjoin.command

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.extension.color
import com.github.frcsty.frozenjoin.load.Settings
import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender
import java.util.logging.Level

@Command("frozenjoin")
class HelpCommand(private val plugin: FrozenJoinPlugin): CommandBase() {

    companion object {
        private const val COMMAND: String = "help"
        private const val PERMISSION: String = "join.command.help"
    }

    @SubCommand(COMMAND)
    @Permission(PERMISSION)
    fun helpCommand(sender: CommandSender) {
        val help: List<String> = plugin.config.getStringList("messages.help-message")

        if (help.isEmpty()) {
            Settings.LOGGER.log(Level.WARNING, "Configuration message 'messages.help-message' is incomplete!")
            return
        }

        for (line in help) {
            sender.sendMessage(line.color())
        }

        if (Settings.DEBUG) Settings.LOGGER.log(Level.INFO, String.format("Executor %s executed action 'help'", sender.name))
    }
}