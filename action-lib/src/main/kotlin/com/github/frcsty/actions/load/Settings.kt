package com.github.frcsty.actions.load

import java.util.logging.Logger

interface Settings {
    val cacheUpdateInterval: Long
    var cachedPlaceholders: List<String>
    val debug: Boolean
    val metrics: Boolean

    val logger: Logger
    val pluginVersion: String
}