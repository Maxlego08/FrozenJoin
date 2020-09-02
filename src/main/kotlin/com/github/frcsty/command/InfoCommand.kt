package com.github.frcsty.command

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.configuration.MessageLoader
import com.github.frcsty.load.Settings
import com.github.frcsty.load.logInfo
import com.github.frcsty.util.color
import com.github.frcsty.util.replacePlaceholder
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender

@Command("frozenjoin")
class InfoCommand(plugin: FrozenJoinPlugin, private val messageLoader: MessageLoader) : CommandBase() {

    companion object {
        private const val PERMISSION: String = "join.command.base"
    }

    init {
        setAliases(plugin.config.getStringList("settings.alias"))
    }

    @Default
    @Permission(PERMISSION)
    fun infoCommand(sender: CommandSender) {
        val lines = messageLoader.getMessageList("infoMessage")

        for (line in lines) {
            sender.sendMessage((line.replacePlaceholder("{version}", Settings.PLUGIN_VERSION)).color())
        }

        if (Settings.DEBUG) logInfo("Executor ${sender.name} executed action 'info'")
    }
}