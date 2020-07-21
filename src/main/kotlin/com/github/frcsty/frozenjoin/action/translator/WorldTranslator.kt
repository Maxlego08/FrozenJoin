package com.github.frcsty.frozenjoin.action.translator

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player

class WorldTranslator : Translator<World?> {
    @Throws(TranslationException::class)
    override fun translate(player: Player, input: String): World? {
        return Bukkit.getWorld(input)
    }
}