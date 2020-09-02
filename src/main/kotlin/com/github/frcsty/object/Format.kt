package com.github.frcsty.`object`

class Format(
        val identifier: String = "",
        val priority: Int = 0,
        val type: String = "",
        val permission: String = "",
        val joinActions: List<String> = emptyList(),
        val leaveActions: List<String> = emptyList(),
        val isInverted: Boolean = false
)