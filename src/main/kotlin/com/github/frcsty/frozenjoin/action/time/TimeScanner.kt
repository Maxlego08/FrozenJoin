package com.github.frcsty.frozenjoin.action.time

import java.util.*
import java.util.function.Predicate

internal class TimeScanner(time: String) {
    private val time: CharArray
    private var index = 0
    operator fun hasNext(): Boolean {
        return index < time.size - 1
    }

    fun nextLong(): Long {
        return String(next(Predicate { ch: Char? -> Character.isDigit(ch!!) })).toLong()
    }

    fun nextString(): String {
        return String(next(Predicate { codePoint: Char -> Character.isAlphabetic(codePoint.toInt()) }))
    }

    private fun next(whichSatisfies: Predicate<Char>): CharArray {
        val startIndex = index
        while (++index < time.size && whichSatisfies.test(time[index]));
        return Arrays.copyOfRange(time, startIndex, index)
    }

    init {
        this.time = time.toCharArray()
    }
}