package com.github.frcsty.frozenjoin.util

import org.bukkit.entity.Player


/*
Player Data
 */
private val entityPlayerClass by lazy {
    Reflection.getNMSClass("EntityPlayer")
}

private val playerConnectionClass by lazy {
    Reflection.getNMSClass("PlayerConnection")
}

private val playerConnectionField by lazy {
    entityPlayerClass.getField("playerConnection")
}

private val craftPlayerClass by lazy {
    Reflection.getCraftBukkitClass("entity.CraftPlayer")
}

private val packetClass by lazy {
    Reflection.getNMSClass("Packet")
}

private val sendPacketMethod by lazy {
    playerConnectionClass.getMethod("sendPacket", packetClass)
}

private val getHandleMethod by lazy {
    craftPlayerClass.getMethod("getHandle")
}

private val chatSerializerMethod by lazy {
    Reflection.getNMSClass("IChatBaseComponent\$ChatSerializer").getMethod("a", String::class.java)
}

private val enumTitleActionClass by lazy {
    Reflection.getNMSClass("PacketPlayOutTitle\$EnumTitleAction")
}

private val iChatBaseComponentClass by lazy {
    Reflection.getNMSClass("IChatBaseComponent")
}

private val packetPlayOutTitleConstructor by lazy {
    Reflection.getNMSClass("PacketPlayOutTitle")
            .getConstructor(enumTitleActionClass, iChatBaseComponentClass, Int::class.java, Int::class.java, Int::class.java)
}


private val titleEnum by lazy {
    enumTitleActionClass.getField("TITLE").get(null)
}
private val subtitleEnum by lazy {
    enumTitleActionClass.getField("SUBTITLE").get(null)
}

fun Player.sendPlayerTitle(title: String, subtitle: String?, fadeIn: Int, stay: Int, fadeOut: Int) {
    if (Reflection.is1_8) {
        val titleBaseComponent = createIChatBaseComponent(title)
        val titlePacket: Any = packetPlayOutTitleConstructor.newInstance(titleEnum, titleBaseComponent, fadeIn, stay, fadeOut)

        sendPacket(titlePacket)

        if (subtitle != null) {
            val subtitleBaseComponent: Any = createIChatBaseComponent(subtitle)
            val subtitlePacket = packetPlayOutTitleConstructor.newInstance(subtitleEnum, subtitleBaseComponent, fadeIn, stay, fadeOut)
            sendPacket(subtitlePacket)
        }

    } else {
        this.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
    }
}


private fun Player.sendPacket(packet: Any) {
    val entityPlayer = entityPlayerClass.cast(getHandleMethod.invoke(this))
    val playerConnection = playerConnectionField.get(entityPlayer)

    sendPacketMethod.invoke(playerConnection, packet)

}

private fun createIChatBaseComponent(text: String): Any {
    return chatSerializerMethod(null, """{"text":"$text"}""")
}
