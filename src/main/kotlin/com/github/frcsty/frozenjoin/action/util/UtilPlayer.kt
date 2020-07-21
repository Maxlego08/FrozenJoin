package com.github.frcsty.frozenjoin.action.util

import com.github.frcsty.frozenjoin.extension.color
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

object UtilPlayer {
    private var craftPlayerClass: Class<*>? = null
    private var getHandleMethod: Method? = null
    private var entityPlayerClass: Class<*>? = null
    private var playerConnectionField: Field? = null
    private var sendPacketMethod: Method? = null
    private var chatSerializerMethod: Method? = null
    private var packetPlayOutChatConstructor: Constructor<*>? = null
    private var packetPlayOutTitleConstructor: Constructor<*>? = null
    private var titleEnum: Any? = null
    private var subtitleEnum: Any? = null
    private var sendTitleMethod: Method? = null
    private var spigotMethod: Method? = null
    private var sendMessageMethod: Method? = null

    fun sendActionbar(players: Collection<Player>, msg: String) {
        if (Reflection.isV1_8) {
            try {
                val iChatBaseComponent = createIChatBaseComponent(msg)
                val packetPlayOutChat = packetPlayOutChatConstructor!!.newInstance(iChatBaseComponent, 2.toByte())
                players.forEach { player: Player -> sendPacket(player, packetPlayOutChat) }
            } catch (ex: ReflectiveOperationException) {
                ex.printStackTrace()
            }
        } else {
            try {
                val component = TextComponent(msg)
                for (player in players) {
                    val playerSpigot = spigotMethod!!.invoke(player)
                    sendMessageMethod!!.invoke(playerSpigot, ChatMessageType.ACTION_BAR, component)
                }
            } catch (ex: ReflectiveOperationException) {
                ex.printStackTrace()
            }
        }
    }

    fun sendTitle(players: Collection<Player>, title: String, subtitle: String?, fadeIn: Int, stay: Int, fadeOut: Int) {
        if (Reflection.isV1_8) {
            try {
                val titleBaseComponent = createIChatBaseComponent(title)
                val titlePacket = packetPlayOutTitleConstructor!!.newInstance(titleEnum, titleBaseComponent, fadeIn, stay, fadeOut)
                var subtitlePacket: Any? = null
                if (subtitle != null) {
                    val subtitleBaseComponent = createIChatBaseComponent(subtitle)
                    subtitlePacket = packetPlayOutTitleConstructor!!.newInstance(subtitleEnum, subtitleBaseComponent, fadeIn, stay, fadeOut)
                }
                for (player in players) {
                    sendPacket(player, titlePacket)
                    subtitlePacket?.let { sendPacket(player, it) }
                }
            } catch (ex: ReflectiveOperationException) {
                ex.printStackTrace()
            }
        } else {
            try {
                for (player in players) {
                    sendTitleMethod!!.invoke(player, title, subtitle, fadeIn, stay, fadeOut)
                }
            } catch (ex: ReflectiveOperationException) {
                ex.printStackTrace()
            }
        }
    }

    @Throws(ReflectiveOperationException::class)
    private fun createIChatBaseComponent(text: String): Any {
        return chatSerializerMethod!!.invoke(null, "{\"text\":\"$text\"}")
    }

    private fun sendPacket(player: Player, packet: Any) {
        try {
            val playerConnection = playerConnectionField!![entityPlayerClass!!.cast(
                    getHandleMethod!!.invoke(
                            craftPlayerClass!!.cast(player)
                    )
            )]
            sendPacketMethod!!.invoke(playerConnection, packet)
        } catch (ex: ReflectiveOperationException) {
            ex.printStackTrace()
        }
    }

    fun setPlayerItem(player: Player, itemString: String, display: String?, lore: List<String?>, amount: Int, slot: Int) {
        val vars = itemString.split(",").toTypedArray()
        val useData = vars.size == 2
        val material = (if (useData) Material.getMaterial(vars[1]) else Material.getMaterial(itemString))
                ?: Material.STONE
        val data = if (useData) Integer.valueOf(vars[1]) else 0
        val item: ItemStack

        item = if (useData) {
            ItemStack(material, amount, data.toShort())
        } else {
            ItemStack(material, amount)
        }
        val meta = item.itemMeta
        if (display != null) {
            meta.setDisplayName(display.color())
        }
        if (lore.isNotEmpty()) {
            meta.lore = lore
        }
        item.itemMeta = meta
        if (player.inventory.getItem(slot) == null) {
            player.inventory.setItem(slot, item)
        } else {
            player.inventory.addItem(item)
        }
    }

    init {
        if (Reflection.isV1_8) {
            craftPlayerClass = Reflection.getCraftBukkitClass("entity.CraftPlayer")
            getHandleMethod = Reflection.getMethod(craftPlayerClass!!, "getHandle")
            entityPlayerClass = Reflection.getNmsClass("EntityPlayer")
            playerConnectionField = Reflection.getField(entityPlayerClass!!, "playerConnection")
            val packetClass = Reflection.getNmsClass("Packet")
            val playerConnectionClass = Reflection.getNmsClass("PlayerConnection")
            sendPacketMethod = Reflection.getMethod(playerConnectionClass, "sendPacket", packetClass)
            val iChatBaseComponentClass = Reflection.getNmsClass("IChatBaseComponent")
            val chatSerializerClass = Reflection.getNmsClass("IChatBaseComponent\$ChatSerializer")
            chatSerializerMethod = Reflection.getMethod(chatSerializerClass, "a", String::class.java)
            val packetPlayOutChatClass = Reflection.getNmsClass("PacketPlayOutChat")
            packetPlayOutChatConstructor = Reflection.getConstructor(packetPlayOutChatClass, iChatBaseComponentClass, Byte::class.javaPrimitiveType)
            val packetPlayOutTitleClass = Reflection.getNmsClass("PacketPlayOutTitle")
            val enumTitleActionClass = Reflection.getNmsClass("PacketPlayOutTitle\$EnumTitleAction")
            try {
                titleEnum = Reflection.getField(enumTitleActionClass, "TITLE")[null]
                subtitleEnum = Reflection.getField(enumTitleActionClass, "SUBTITLE")[null]
            } catch (ignored: IllegalAccessException) {
            }
            packetPlayOutTitleConstructor = Reflection.getConstructor(packetPlayOutTitleClass, enumTitleActionClass, iChatBaseComponentClass, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
        } else {
            val playerClass = Reflection.getBukkitClass("entity.Player")
            sendTitleMethod = Reflection.getMethod(playerClass, "sendTitle", String::class.java, String::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            spigotMethod = Reflection.getMethod(playerClass, "spigot")
            val spigotClass = Reflection.getBukkitClass("entity.Player\$Spigot")
            sendMessageMethod = Reflection.getMethod(spigotClass, "sendMessage", ChatMessageType::class.java, BaseComponent::class.java)
        }
    }
}