package com.github.frcsty.frozenjoin.action.util

import com.github.frcsty.frozenjoin.extension.color
import org.bukkit.entity.Player

object UtilMessage {
    private const val CENTER_PX = 154
    private const val MAX_PX = 250
    fun sendCenteredMessage(player: Player, msg: String) {
        var message = msg.color()
        var messagePxSize = 0
        var previousCode = false
        var isBold = false
        var charIndex = 0
        var lastSpaceIndex = 0
        var toSendAfter: String? = null
        var recentColorCode = ""
        for (c in message.toCharArray()) {
            if (c == '§') {
                previousCode = true
                continue
            } else if (previousCode) {
                previousCode = false
                recentColorCode = "§$c"
                if (c == 'l' || c == 'L') {
                    isBold = true
                    continue
                } else {
                    isBold = false
                }
            } else if (c == ' ') {
                lastSpaceIndex = charIndex
            } else {
                val dfi = DefaultFontInfo.getDefaultFontInfo(c)
                messagePxSize += if (isBold) dfi.boldLength else dfi.length
                messagePxSize++
            }
            if (messagePxSize >= MAX_PX) {
                toSendAfter = recentColorCode + message.substring(lastSpaceIndex + 1)
                message = message.substring(0, lastSpaceIndex + 1)
                break
            }
            charIndex++
        }
        val halvedMessageSize = messagePxSize / 2
        val toCompensate = CENTER_PX - halvedMessageSize
        val spaceLength = DefaultFontInfo.SPACE.length + 1
        var compensated = 0
        val sb = StringBuilder()
        while (compensated < toCompensate) {
            sb.append(" ")
            compensated += spaceLength
        }
        player.sendMessage(sb.toString() + message)
        toSendAfter?.let { sendCenteredMessage(player, it) }
    }
}