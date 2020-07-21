package com.github.frcsty.frozenjoin.action.util;

import com.github.frcsty.frozenjoin.action.Color;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.github.frcsty.frozenjoin.action.util.Reflection.*;

public final class UtilPlayer {
    private static Class<?> craftPlayerClass;
    private static Method getHandleMethod;
    private static Class<?> entityPlayerClass;
    private static Field playerConnectionField;
    private static Method sendPacketMethod;
    private static Method chatSerializerMethod;
    private static Constructor<?> packetPlayOutChatConstructor;
    private static Constructor<?> packetPlayOutTitleConstructor;
    private static Object titleEnum;
    private static Object subtitleEnum;
    private static Method sendTitleMethod;
    private static Method spigotMethod;
    private static Method sendMessageMethod;

    static {
        if (Reflection.isV1_8()) {
            craftPlayerClass = getCraftBukkitClass("entity.CraftPlayer");
            getHandleMethod = getMethod(craftPlayerClass, "getHandle");

            entityPlayerClass = getNmsClass("EntityPlayer");
            playerConnectionField = getField(entityPlayerClass, "playerConnection");

            final Class<?> packetClass = getNmsClass("Packet");
            final Class<?> playerConnectionClass = getNmsClass("PlayerConnection");
            sendPacketMethod = getMethod(playerConnectionClass, "sendPacket", packetClass);

            final Class<?> iChatBaseComponentClass = getNmsClass("IChatBaseComponent");
            final Class<?> chatSerializerClass = getNmsClass("IChatBaseComponent$ChatSerializer");
            chatSerializerMethod = getMethod(chatSerializerClass, "a", String.class);

            final Class<?> packetPlayOutChatClass = getNmsClass("PacketPlayOutChat");
            packetPlayOutChatConstructor = getConstructor(packetPlayOutChatClass, iChatBaseComponentClass, byte.class);

            final Class<?> packetPlayOutTitleClass = getNmsClass("PacketPlayOutTitle");
            final Class<?> enumTitleActionClass = getNmsClass("PacketPlayOutTitle$EnumTitleAction");

            try {
                titleEnum = getField(enumTitleActionClass, "TITLE").get(null);
                subtitleEnum = getField(enumTitleActionClass, "SUBTITLE").get(null);
            } catch (IllegalAccessException ignored) {
            }

            packetPlayOutTitleConstructor = getConstructor(packetPlayOutTitleClass, enumTitleActionClass, iChatBaseComponentClass, int.class, int.class, int.class);
        } else {
            final Class<?> playerClass = getBukkitClass("entity.Player");
            sendTitleMethod = getMethod(playerClass, "sendTitle", String.class, String.class, int.class, int.class, int.class);

            spigotMethod = getMethod(playerClass, "spigot");
            final Class<?> spigotClass = getBukkitClass("entity.Player$Spigot");

            sendMessageMethod = getMethod(spigotClass, "sendMessage", ChatMessageType.class, BaseComponent.class);
        }
    }

    public static void sendActionbar(final Collection<? extends Player> players, final String msg) {
        if (Reflection.isV1_8()) {
            try {
                final Object iChatBaseComponent = createIChatBaseComponent(msg);
                final Object packetPlayOutChat = packetPlayOutChatConstructor.newInstance(iChatBaseComponent, (byte) 2);

                players.forEach(player -> sendPacket(player, packetPlayOutChat));
            } catch (final ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                final TextComponent component = new TextComponent(msg);

                for (final Player player : players) {
                    final Object playerSpigot = spigotMethod.invoke(player);
                    sendMessageMethod.invoke(playerSpigot, ChatMessageType.ACTION_BAR, component);
                }
            } catch (final ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void sendTitle(final Collection<? extends Player> players, final String title, final String subtitle, final int fadeIn, final int stay, final int fadeOut) {
        if (Reflection.isV1_8()) {
            try {
                final Object titleBaseComponent = createIChatBaseComponent(title);
                final Object titlePacket = packetPlayOutTitleConstructor.newInstance(titleEnum, titleBaseComponent, fadeIn, stay, fadeOut);
                Object subtitlePacket = null;

                if (subtitle != null) {
                    final Object subtitleBaseComponent = createIChatBaseComponent(subtitle);
                    subtitlePacket = packetPlayOutTitleConstructor.newInstance(subtitleEnum, subtitleBaseComponent, fadeIn, stay, fadeOut);
                }

                for (final Player player : players) {
                    sendPacket(player, titlePacket);

                    if (subtitlePacket != null) {
                        sendPacket(player, subtitlePacket);
                    }
                }
            } catch (final ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                for (final Player player : players) {
                    sendTitleMethod.invoke(player, title, subtitle, fadeIn, stay, fadeOut);
                }
            } catch (final ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static Object createIChatBaseComponent(final String text) throws ReflectiveOperationException {
        return chatSerializerMethod.invoke(null, "{\"text\":\"" + text + "\"}");
    }

    private static void sendPacket(final Player player, final Object packet) {
        try {
            final Object playerConnection = playerConnectionField.get(
                    entityPlayerClass.cast(
                            getHandleMethod.invoke(
                                    craftPlayerClass.cast(player)
                            )
                    )
            );

            sendPacketMethod.invoke(playerConnection, packet);
        } catch (final ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    public static void setPlayerItem(final Player player, final String itemString, final String display, final List<String> lore, final int amount, final int slot) {
        final String[] vars = itemString.split(",");
        final boolean useData = vars.length == 2;
        final Material material = useData ? Material.getMaterial(vars[1]) : Material.getMaterial(itemString);
        final int data = useData ? Integer.valueOf(vars[1]) : 0;

        ItemStack item;
        if (useData) {
            item = new ItemStack(Objects.requireNonNull(material), amount, (short) data);
        } else {
            item = new ItemStack(Objects.requireNonNull(material), amount);
        }

        final ItemMeta meta = item.getItemMeta();
        if (display != null) {
            meta.setDisplayName(Color.colorize(display));
        }
        if (!lore.isEmpty()) {
            meta.setLore(Color.colorize(lore));
        }

        item.setItemMeta(meta);

        if (player.getInventory().getItem(slot) == null) {
            player.getInventory().setItem(slot, item);
        } else {
            player.getInventory().addItem(item);
        }
    }
}