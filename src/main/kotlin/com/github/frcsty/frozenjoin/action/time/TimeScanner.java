package com.github.frcsty.frozenjoin.action.time;

import java.util.Arrays;
import java.util.function.Predicate;

class TimeScanner {
    private char[] time;
    private int index = 0;

    TimeScanner(String time) {
        this.time = time.toCharArray();
    }

    boolean hasNext() {
        return index < time.length - 1;
    }

    long nextLong() {
        return Long.parseLong(String.valueOf(next(Character::isDigit)));
    }

    String nextString() {
        return String.valueOf(next(Character::isAlphabetic));
    }

    private char[] next(Predicate<Character> whichSatisfies) {
        int startIndex = index;
        while (++index < time.length && whichSatisfies.test(time[index])) ;

        return Arrays.copyOfRange(time, startIndex, index);
    }
}
