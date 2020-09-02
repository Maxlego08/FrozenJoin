package com.github.frcsty.`object`

class MOTD(
        val identifier: String = "",
        val message: List<String> = emptyList(),
        val priority: Int = 0,
        val permission: String = ""
)