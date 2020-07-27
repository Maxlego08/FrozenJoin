package com.github.frcsty.frozenjoin.action.time

import java.util.concurrent.TimeUnit

private const val DAYS_IN_WEEK: Long = 7
private const val DAYS_IN_MONTH: Long = 30
private const val DAYS_IN_YEAR: Long = 365

class TimeAPI(val seconds: Long) {
    constructor(time: String) : this(parseTime(time))

    val nanoseconds: Double by lazy {
        TimeUnit.SECONDS.toNanos(seconds).toDouble()
    }

    val microseconds: Double by lazy {
        TimeUnit.SECONDS.toMicros(seconds).toDouble()
    }

    val milliseconds: Double by lazy {
        TimeUnit.SECONDS.toMillis(seconds).toDouble()
    }

    val minutes: Double by lazy { seconds / 60.0 }

    val hours: Double by lazy { seconds / 3600.0 }

    val days: Double by lazy { seconds / 86400.0 }

    val weeks: Double by lazy { days / DAYS_IN_WEEK }

    val months: Double by lazy { days / DAYS_IN_MONTH }

    val years: Double by lazy { days / DAYS_IN_YEAR }
}


private fun parseTime(time: String): Long {
    var seconds = 0L
    val scanner = TimeScanner(
            time
                    .replace(" ", "")
                    .replace("and", "")
                    .replace(",", "")
                    .toLowerCase()
    )
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
    return seconds
}