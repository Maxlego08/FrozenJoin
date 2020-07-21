package com.github.frcsty.frozenjoin.action.util;

public final class UtilArray {

    public static Object[] prepend(final Object[] array, final Object value) {
        final Object[] newArray = new Object[array.length + 1];

        newArray[0] = value;
        System.arraycopy(array, 0, newArray, 1, array.length);

        return newArray;
    }
}
