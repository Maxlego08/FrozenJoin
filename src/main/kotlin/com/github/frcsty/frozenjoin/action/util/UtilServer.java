package com.github.frcsty.frozenjoin.action.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public final class UtilServer {

    public static void writeToBungee(final Player player, final Plugin plugin, final String... args) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        Arrays.stream(args).forEach(output::writeUTF);

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }
}
