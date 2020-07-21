package com.github.frcsty.frozenjoin.`object`

import java.util.*

class MOTD {
    var message: List<String> = Collections.emptyList()
        private set
    var priority = 0
        private set
    var permission: String? = ""
        private set

    private fun setMessage(values: List<String>) {
        this.message = values
    }

    fun withMessage(values: List<String>): MOTD {
        setMessage(values)
        return this
    }

    private fun setPriority(value: Int) {
        this.priority = value
    }

    fun withPriority(value: Int): MOTD {
        setPriority(value)
        return this
    }

    private fun setPermission(value: String?) {
        this.permission = value
    }

    fun withPermission(value: String?): MOTD {
        setPermission(value)
        return this
    }

    companion object {
        fun create(): MOTD {
            return MOTD()
        }
    }
}