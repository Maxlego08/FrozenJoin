package com.github.frcsty.frozenjoin.action.util

import org.bukkit.Bukkit
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

internal object Reflection {
    private val VERSION = Bukkit.getServer().javaClass.getPackage().name.split("\\.").toTypedArray()[3]
    val isV1_8: Boolean
        get() = VERSION.contains("v1_8")

    fun getNmsClass(className: String): Class<*> {
        return getClass("net.minecraft.server.$VERSION.$className")
    }

    fun getBukkitClass(className: String): Class<*> {
        return getClass("org.bukkit.$className")
    }

    fun getCraftBukkitClass(className: String): Class<*> {
        return getClass("org.bukkit.craftbukkit.$VERSION.$className")
    }

    private fun getClass(className: String): Class<*> {
        return try {
            Class.forName(className)
        } catch (ex: ClassNotFoundException) {
            throw RuntimeException(ex)
        }
    }

    fun <T> getConstructor(clazz: Class<T>, vararg parameterTypes: Class<*>?): Constructor<T> {
        return try {
            val constructor = clazz.getDeclaredConstructor(*parameterTypes)
            if (!constructor.isAccessible) {
                constructor.isAccessible = true
            }
            constructor
        } catch (ex: NoSuchMethodException) {
            throw RuntimeException(ex)
        }
    }

    fun getField(clazz: Class<*>, fieldName: String?): Field {
        return try {
            val field = clazz.getDeclaredField(fieldName)
            if (!field.isAccessible) {
                field.isAccessible = true
            }
            field
        } catch (ex: NoSuchFieldException) {
            throw RuntimeException(ex)
        }
    }

    fun getMethod(clazz: Class<*>, methodName: String?, vararg parameterTypes: Class<*>?): Method {
        return try {
            val method = clazz.getDeclaredMethod(methodName, *parameterTypes)
            if (!method.isAccessible) {
                method.isAccessible = true
            }
            method
        } catch (ex: NoSuchMethodException) {
            throw RuntimeException(ex)
        }
    }
}