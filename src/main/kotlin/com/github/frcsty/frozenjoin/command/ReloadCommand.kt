package com.github.frcsty.frozenjoin.command

import com.github.frcsty.frozenjoin.FrozenJoinPlugin
import com.github.frcsty.frozenjoin.configuration.MessageLoader
import com.github.frcsty.frozenjoin.util.color
import com.github.frcsty.frozenjoin.load.Loader
import com.github.frcsty.frozenjoin.load.Settings
import com.github.frcsty.frozenjoin.load.logInfo
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable

@Command("frozenjoin")
class ReloadCommand(private val plugin: FrozenJoinPlugin, private val loader: Loader, private val messageLoader: MessageLoader) : CommandBase() {

    companion object {
        private const val COMMAND: String = "reload"
        private const val PERMISSION: String = "join.command.base"
    }

    @SubCommand(COMMAND)
    @Permission(PERMISSION)
    fun reloadCommand(sender: CommandSender) {
        val startTime = System.currentTimeMillis()
        val message = messageLoader.getMessage("reloadMessage")

        loader.formatManager.formatsMap.clear()
        loader.formatManager.motdsMap.clear()

        object : BukkitRunnable() {
            override fun run() {
                plugin.reloadConfig()
                loader.formatManager.setFormats()
                messageLoader.load()
            }
        }.runTaskAsynchronously(plugin)

        val estimatedTime = System.currentTimeMillis() - startTime
        sender.sendMessage((message.replace("%time%", estimatedTime.toString()).color()))

        if (Settings.DEBUG) logInfo("Executor ${sender.name} executed action 'reload'")
    }
}