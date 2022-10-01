package com.github.frcsty.actions.load

import com.github.frcsty.actions.ActionHandler
import com.github.frcsty.actions.cache.PlaceholderCache

interface Loader {
    val settings: Settings
    val placeholderCache: PlaceholderCache
    val actionHandler: ActionHandler
}