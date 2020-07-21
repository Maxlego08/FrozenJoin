package com.github.frcsty.frozenjoin.action.translator

import org.bukkit.entity.Player

class ListTranslator : Translator<List<*>?> {
    @Throws(TranslationException::class)
    override fun translate(player: Player, input: String): List<String> {
        return listOf(*input.split("~").toTypedArray())
    }
}