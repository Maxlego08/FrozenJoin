package com.github.frcsty.frozenjoin.command

import com.github.frcsty.frozenjoin.configuration.MessageLoader
import com.github.frcsty.frozenjoin.util.*
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command("frozenjoin")
class FormatCommand(private val messageLoader: MessageLoader) : CommandBase() {

    companion object {
        private const val SET_COMMAND = "set"
        private const val REMOVE_COMMAND = "remove"
        private const val PERMISSION = "frozenjoin.command.format"
        private const val JOIN_COMMAND = "join"
        private const val QUIT_COMMAND = "quit"
    }

    @SubCommand(SET_COMMAND)
    @Permission(PERMISSION)
    fun formatSetCommand(sender: CommandSender, target: String, argument: String, message: Array<String>) {
        val player: Player? = Bukkit.getPlayer(target)
        val msg = message.joinToString(" ")
        if (player == null) {
            sender.sendMessage(messageLoader.getMessage("customMessageInvalidPlayerMessage"))
            return
        }

        when (argument) {
            JOIN_COMMAND,
            QUIT_COMMAND -> {
                player.setCustomMessage(JOIN_COMMAND, message = msg)
                player.setCustomMessage(QUIT_COMMAND, message = msg)
            }
            else -> {
                sender.sendMessage(messageLoader.getMessage("customMessageInvalidArgumentMessage"))
                return
            }
        }

        sender.sendTranslatedMessage(player, messageLoader.getMessage("customMessageSetTargetMessage")
                .replacePlaceholder("%type%", argument)
                .replacePlaceholder("%message%", msg.color()))
    }

    @SubCommand(REMOVE_COMMAND)
    @Permission(PERMISSION)
    fun formatRemoveCommand(sender: CommandSender, target: String, argument: String) {
        val player: Player? = Bukkit.getPlayer(target)
        if (player == null) {
            sender.sendMessage(messageLoader.getMessage("customMessageInvalidPlayerMessage"))
            return
        }

        when (argument) {
            JOIN_COMMAND,
            QUIT_COMMAND -> {
                player.removeCustomMessage(type = argument)
            }
            else -> {
                sender.sendMessage(messageLoader.getMessage("customMessageInvalidArgumentMessage"))
                return
            }
        }

        sender.sendTranslatedMessage(player, messageLoader.getMessage("customMessageRemoveTargetMessage")
                .replacePlaceholder("%type%", argument))
    }
}