package com.github.frcsty.util

import com.github.frcsty.load.Settings
import net.luckperms.api.LuckPerms
import net.luckperms.api.model.user.User
import org.bukkit.entity.Player
import java.util.logging.Level

fun Player.luckPermsCheck(permission: String, provider: LuckPerms?): Boolean {
    val luckPermsAPI: LuckPerms = provider ?: return false
    val user: User? = luckPermsAPI.userManager.getUser(this.uniqueId)
    if (user == null) {
        Settings.LOGGER.log(Level.WARNING, "Failed to load luckperms user data for ${this.name}!")
        return false
    }

    val contextManager = luckPermsAPI.contextManager
    val queryOptions = contextManager.getQueryOptions(user).orElse(contextManager.staticQueryOptions)

    val checkResult = user.cachedData.getPermissionData(queryOptions).checkPermission(permission)
    return checkResult.asBoolean()
}