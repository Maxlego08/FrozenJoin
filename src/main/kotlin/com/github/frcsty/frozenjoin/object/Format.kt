package com.github.frcsty.frozenjoin.`object`

import org.bukkit.event.EventPriority
import java.util.*

class Format (
        val priority: Int = 0,
        val type: String = "",
        val permission: String = "",
        val joinActions: List<String> = emptyList(),
        val leaveActions: List<String> = emptyList(),
        val isInverted: Boolean = false
)