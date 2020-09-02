package com.github.frcsty

import com.github.frcsty.load.Loader
import org.bukkit.plugin.java.JavaPlugin

class FrozenJoinPlugin : JavaPlugin() {

    private val loader: Loader = Loader(this)

    override fun onEnable() {
        loader.initialize()
    }

    override fun onDisable() {
        loader.terminate()
    }

}