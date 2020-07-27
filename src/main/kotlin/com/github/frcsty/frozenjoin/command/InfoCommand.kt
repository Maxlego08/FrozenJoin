package com.github.frcsty.frozenjoin.command

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.extension.color
import com.github.frcsty.frozenjoin.load.Settings
import com.github.frcsty.frozenjoin.load.logInfo
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender
import java.util.logging.Level

@Command("frozenjoin")
class InfoCommand(private val plugin: FrozenJoinPlugin): CommandBase() {

    companion object {
        private const val PERMISSION: String = "join.command.base"
    }

    init {
        setAliases(plugin.config.getStringList("settings.alias"))
    }

    @Default
    @Permission(PERMISSION)
    fun infoCommand(sender: CommandSender) {
        val info: List<String> = plugin.config.getStringList("messages.infoMessage")

        if (info.isEmpty()) {
            Settings.LOGGER.log(Level.WARNING, "Configuration message 'messages.infoMessage' is incomplete!")
            return
        }

        for (line in info) {
            sender.sendMessage((line.replace("{version}", plugin.description.version)).color())
        }

        if (Settings.DEBUG) logInfo("Executor ${sender.name} executed action 'info'")
    }
}