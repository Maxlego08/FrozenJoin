package com.github.frcsty.frozenjoin.`object`

class Format(
        val priority: Int = 0,
        val type: String = "",
        val permission: String = "",
        val joinActions: List<String> = emptyList(),
        val leaveActions: List<String> = emptyList(),
        val isInverted: Boolean = false
)