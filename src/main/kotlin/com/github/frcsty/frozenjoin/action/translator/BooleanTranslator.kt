package com.github.frcsty.frozenjoin.action.translator

import org.bukkit.entity.Player

class BooleanTranslator : Translator<Boolean?> {
    @Throws(TranslationException::class)
    override fun translate(player: Player, input: String): Boolean {
        return when (input.toLowerCase()) {
            "true" -> true
            "false" -> false
            else -> throw TranslationException("$input is not a valid boolean")
        }
    }
}