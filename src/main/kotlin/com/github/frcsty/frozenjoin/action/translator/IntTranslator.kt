package com.github.frcsty.frozenjoin.action.translator

import org.bukkit.entity.Player

class IntTranslator : Translator<Int?> {
    @Throws(TranslationException::class)
    override fun translate(player: Player, input: String): Int {
        return try {
            input.toInt()
        } catch (ex: NumberFormatException) {
            throw TranslationException("$input is not a valid integer")
        }
    }
}