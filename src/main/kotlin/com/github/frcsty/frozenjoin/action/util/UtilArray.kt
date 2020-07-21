package com.github.frcsty.frozenjoin.action.util

object UtilArray {
    fun prepend(array: Array<Any?>, value: Any?): Array<Any?> {
        val newArray = arrayOfNulls<Any>(array.size + 1)
        newArray[0] = value
        System.arraycopy(array, 0, newArray, 1, array.size)
        return newArray
    }
}