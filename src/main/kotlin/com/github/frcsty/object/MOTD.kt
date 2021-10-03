package com.github.frcsty.`object`

class MOTD(
        val identifier: String = "",
        val message: List<String> = emptyList(),
        val delay: String = "0s",
        val delayOnCommand: Boolean = false,
        val priority: Int = 0,
        val permission: String = ""
)