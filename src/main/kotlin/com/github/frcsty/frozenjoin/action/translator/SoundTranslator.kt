package com.github.frcsty.frozenjoin.action.translator

import org.bukkit.Sound
import org.bukkit.entity.Player

class SoundTranslator : Translator<Sound?> {
    @Throws(TranslationException::class)
    override fun translate(player: Player, input: String): Sound {
        return try {
            Sound.valueOf(input.toUpperCase())
        } catch (ex: IllegalArgumentException) {
            throw TranslationException(input.toUpperCase() + " is not a valid sound")
        }
    }
}