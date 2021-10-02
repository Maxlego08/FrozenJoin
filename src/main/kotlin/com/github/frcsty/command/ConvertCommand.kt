package com.github.frcsty.command

import com.github.frcsty.FrozenJoinPlugin
import com.github.frcsty.configuration.MessageLoader
import com.github.frcsty.convert.FileConverter
import com.github.frcsty.load.Settings
import com.github.frcsty.load.logInfo
import com.github.frcsty.util.replacePlaceholder
import java.io.File
import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender

@Command("frozenjoin")
@Alias("join", "fjoin")
class ConvertCommand(plugin: FrozenJoinPlugin, private val messageLoader: MessageLoader) : CommandBase() {

    private val data = plugin.dataFolder

    companion object {
        private const val COMMAND: String = "convert"
        private const val PERMISSION: String = "join.command.convert"
    }

    @SubCommand(COMMAND)
    @Permission(PERMISSION)
    fun convertCommand(sender: CommandSender, command: String) {
        when (command) {
            "generate" -> {
                generate(sender)
                if (Settings.DEBUG) logInfo("Executor ${sender.name} executed action 'convert generate'")
            }
            "start" -> {
                start(sender)
                if (Settings.DEBUG) logInfo("Executor ${sender.name} executed action 'convert start'")
            }
            "dump" -> {
                dump(sender)
                if (Settings.DEBUG) logInfo("Executor ${sender.name} executed action 'convert dump'")
            }
            else -> {
                sender.sendMessage(messageLoader.getMessage("convertArgumentMessage"))
            }
        }
    }

    private fun generate(sender: CommandSender) {
        val dir = File("$data/converter")

        if (dir.exists()) {
            sender.sendMessage(messageLoader.getMessage("convertDirectoryExistsMessage"))
            return
        }

        if (dir.mkdir()) {
            sender.sendMessage(messageLoader.getMessage("convertGeneratedDirectoryMessage"))
        }
    }

    private fun start(sender: CommandSender) {
        val startTime = System.currentTimeMillis()
        val dir = File("$data/converter")
        if (dir.exists().not() || dir.length().toInt() == 0) {
            sender.sendMessage(messageLoader.getMessage("convertDirectoryEmptyMessage"))
            return
        }

        val files = dir.listFiles()
        if (files == null) {
            sender.sendMessage(messageLoader.getMessage("convertDirectoryEmptyMessage"))
            return
        }

        for (file in files) {
            FileConverter().convertFile(file, data)
        }

        val estimatedTime = System.currentTimeMillis() - startTime
        sender.sendMessage(messageLoader.getMessage("convertSuccessfullyConvertedMessage").replacePlaceholder("%time%", estimatedTime.toString()))
    }

    private fun dump(sender: CommandSender) {
        val dir = File("$data/converter")

        if (dir.exists().not()) {
            sender.sendMessage(messageLoader.getMessage("convertDumpMissingDirectoryMessage"))
            return
        }

        if (dir.delete()) {
            sender.sendMessage(messageLoader.getMessage("convertDirectoryDeletedMessage"))
        }
    }

}