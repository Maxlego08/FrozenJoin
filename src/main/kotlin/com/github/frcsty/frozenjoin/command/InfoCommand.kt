package com.github.frcsty.frozenjoin.command

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.extension.color
import com.github.frcsty.frozenjoin.load.Settings
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender
import java.util.logging.Level

@Command("frozenjoin")
class InfoCommand(private val plugin: FrozenJoinPlugin): CommandBase() {

    override fun setAliases(aliases: MutableList<String>) {
        super.setAliases(Settings.ALIASES)
    }

    companion object {
        private const val PERMISSION: String = "join.command.base"
    }

    @Default
    @Permission(PERMISSION)
    fun infoCommand(sender: CommandSender) {
        val info: List<String> = plugin.config.getStringList("messages.info-message")

        if (info.isEmpty()) {
            Settings.LOGGER.log(Level.WARNING, "Configuration message 'messages.info-message' is incomplete!")
            return
        }

        for (line in info) {
            sender.sendMessage((line.replace("{version}", plugin.description.version)).color())
        }

        if (Settings.DEBUG) Settings.LOGGER.log(Level.INFO, String.format("Executor %s executed action 'info'", sender.name))
    }
}