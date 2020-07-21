package com.github.frcsty.frozenjoin.action.translator

import org.bukkit.entity.Player

class DecimalTranslator : Translator<Double?> {
    @Throws(TranslationException::class)
    override fun translate(player: Player, input: String): Double {
        return try {
            input.toDouble()
        } catch (ex: NumberFormatException) {
            throw TranslationException("$input is not a valid decimal")
        }
    }
}