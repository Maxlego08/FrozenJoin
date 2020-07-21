package com.github.frcsty.frozenjoin.`object`

import java.util.*

class Format {
    var priority = 0
        private set
    var type: String? = ""
        private set
    var permission: String? = ""
        private set
    var joinActions: List<String> = Collections.emptyList()
        private set
    var leaveActions: List<String> = Collections.emptyList()
        private set
    var isInverted = false

    fun withPriority(value: Int): Format {
        priority = value
        return this
    }

    private fun setType(value: String?) {
        type = value
    }

    fun withType(value: String?): Format {
        setType(value)
        return this
    }

    private fun setPermission(value: String?) {
        permission = value
    }

    fun withPermission(value: String?): Format {
        setPermission(value)
        return this
    }

    private fun setJoinActions(values: List<String>) {
        joinActions = values
    }

    fun withJoinActions(values: List<String>): Format {
        setJoinActions(values)
        return this
    }

    private fun setLeaveActions(values: List<String>) {
        leaveActions = values
    }

    fun withLeaveActions(values: List<String>): Format {
        setLeaveActions(values)
        return this
    }

    fun isInverted(value: Boolean): Format {
        isInverted = value
        return this
    }

    companion object {
        fun create(): Format {
            return Format()
        }
    }
}