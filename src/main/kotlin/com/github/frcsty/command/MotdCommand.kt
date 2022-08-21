package com.github.frcsty.command

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.configuration.MessageLoader
import com.github.frcsty.load.Loader
import com.github.frcsty.message.MessageFormatter
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.base.CommandBase
import org.bukkit.entity.Player

@Command("motd")
class MotdCommand(private val loader: Loader, private val messageLoader: MessageLoader, private val plugin: FrozenJoinPlugin) : CommandBase() {

    companion object {
        private const val PERMISSION: String = "join.command.motd"
    }

    @Default
    @Permission(PERMISSION)
    fun motdCommand(player: Player) = MessageFormatter.executeMotd(
        player,
        loader.formatManager,
        loader.actionHandler,
        command = true,
        message = messageLoader.getMessage("noMotdMessage"),
        plugin,
        loader.placeholderCache
    )

}