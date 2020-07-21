package com.github.frcsty.frozenjoin.action.util;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

final class Reflection {
    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    static boolean isV1_8() {
        return VERSION.contains("v1_8");
    }

    static Class<?> getNmsClass(final String className) {
        return getClass("net.minecraft.server." + VERSION + "." + className);
    }

    static Class<?> getBukkitClass(final String className) {
        return getClass("org.bukkit." + className);
    }

    static Class<?> getCraftBukkitClass(final String className) {
        return getClass("org.bukkit.craftbukkit." + VERSION + "." + className);
    }

    private static Class<?> getClass(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    static <T> Constructor<T> getConstructor(final Class<T> clazz, final Class<?>... parameterTypes) {
        try {
            final Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);

            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            return constructor;
        } catch (final NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    static Field getField(final Class<?> clazz, final String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            return field;
        } catch (final NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        }
    }

    static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes) {
        try {
            final Method method = clazz.getDeclaredMethod(methodName, parameterTypes);

            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            return method;
        } catch (final NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }
}
