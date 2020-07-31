package com.github.frcsty.frozenjoin.util

import com.github.frcsty.frozenjoin.load.Settings
import net.luckperms.api.LuckPerms
import net.luckperms.api.model.user.User
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.logging.Level

private fun getProvider(): LuckPerms? {
    val registration = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)

    if (registration != null) {
        return registration.provider
    }

    Settings.LOGGER.log(Level.WARNING, "Failed to load LuckPerms API provider! (Ensure LuckPerms is properly installed!)")
    return null
}

fun Player.luckPermsCheck(permission: String): Boolean {
    val luckPermsAPI: LuckPerms = getProvider() ?: return false
    val user: User? = luckPermsAPI.userManager.getUser(this.uniqueId)
    if (user == null) {
        Settings.LOGGER.log(Level.WARNING, "Failed to load luckperms user data for ${this.name}!")
        return false
    }

    val contextManager = luckPermsAPI.contextManager
    val queryOptions = contextManager.getQueryOptions(user).orElse(contextManager.staticQueryOptions)

    val permissionData = user.cachedData.getPermissionData(queryOptions)
    val checkResult = permissionData.checkPermission(permission)

    return checkResult.asBoolean()
}