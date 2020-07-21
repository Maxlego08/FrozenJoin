package com.github.frcsty.frozenjoin.action.translator

import org.bukkit.entity.Player

@FunctionalInterface
interface Translator<T> {
    @Throws(TranslationException::class)
    fun translate(player: Player, input: String): T
}