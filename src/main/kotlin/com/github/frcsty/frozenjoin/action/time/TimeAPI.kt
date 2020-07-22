package com.github.frcsty.frozenjoin.action.time

import java.util.concurrent.TimeUnit

class TimeAPI {
    private var seconds: Long = 0

    constructor(time: String) {
        reparse(time)
    }

    constructor(seconds: Long) {
        this.seconds = seconds
    }

    fun reparse(time: String): TimeAPI {
        seconds = 0
        val scanner = TimeScanner(time
                .replace(" ", "")
                .replace("and", "")
                .replace(",", "")
                .toLowerCase())
        var next: Long
        while (scanner.hasNext()) {
            next = scanner.nextLong()
            seconds += when (scanner.nextString()) {
                "s", "sec", "secs", "second", "seconds" -> next
                "m", "min", "mins", "minute", "minutes" -> TimeUnit.MINUTES.toSeconds(next)
                "h", "hr", "hrs", "hour", "hours" -> TimeUnit.HOURS.toSeconds(next)
                "d", "dy", "dys", "day", "days" -> TimeUnit.DAYS.toSeconds(next)
                "w", "week", "weeks" -> TimeUnit.DAYS.toSeconds(next * DAYS_IN_WEEK)
                "mo", "mon", "mnth", "month", "months" -> TimeUnit.DAYS.toSeconds(next * DAYS_IN_MONTH)
                "y", "yr", "yrs", "year", "years" -> TimeUnit.DAYS.toSeconds(next * DAYS_IN_YEAR)
                else -> throw IllegalArgumentException()
            }
        }
        return this
    }

    val nanoseconds: Double
        get() = TimeUnit.SECONDS.toNanos(seconds).toDouble()

    val microseconds: Double
        get() = TimeUnit.SECONDS.toMicros(seconds).toDouble()

    val milliseconds: Double
        get() = TimeUnit.SECONDS.toMillis(seconds).toDouble()

    fun getSeconds(): Long {
        return seconds
    }

    val minutes: Double get() = seconds / 60.0

    val hours: Double get() = seconds / 3600.0

    val days: Double get() = seconds / 86400.0

    val weeks: Double get() = days / DAYS_IN_WEEK

    val months: Double get() = days / DAYS_IN_MONTH

    val years: Double get() = days / DAYS_IN_YEAR

    companion object {
        private const val DAYS_IN_WEEK: Long = 7
        private const val DAYS_IN_MONTH: Long = 30
        private const val DAYS_IN_YEAR: Long = 365
    }
}