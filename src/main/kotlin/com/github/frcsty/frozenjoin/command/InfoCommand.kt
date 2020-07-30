package com.github.frcsty.frozenjoin.command

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.configuration.MessageLoader
import com.github.frcsty.frozenjoin.extension.color
import com.github.frcsty.frozenjoin.load.Settings
import com.github.frcsty.frozenjoin.load.logInfo
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender

@Command("frozenjoin")
class InfoCommand(private val plugin: FrozenJoinPlugin, private val messageLoader: MessageLoader) : CommandBase() {

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
            sender.sendMessage((line.replace("{version}", plugin.description.version)).color())
        }

        if (Settings.DEBUG) logInfo("Executor ${sender.name} executed action 'info'")
    }
}