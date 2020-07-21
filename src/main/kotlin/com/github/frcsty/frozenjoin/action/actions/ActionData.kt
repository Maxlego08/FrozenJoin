package com.github.frcsty.frozenjoin.action.actions

import com.github.frcsty.frozenjoin.action.util.UtilArray.prepend
import org.bukkit.entity.Player
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class ActionData(val key: String, private val executionMethod: Method, val parameterTypes: Array<out Class<*>>) {
    fun execute(player: Player, parameters: Array<Any?>) {
        var params = parameters
        params = prepend(params, player)
        try {
            executionMethod.invoke(null, params)
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        } catch (ex: InvocationTargetException) {
            ex.printStackTrace()
        }
    }
}