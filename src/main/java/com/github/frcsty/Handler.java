package com.github.frcsty;

import com.github.frcsty.frozenactions.wrapper.ActionHandler;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public final class Handler {

    private final ActionHandler handler = new ActionHandler();

    public void execute(final Player player, final String... actions) {
        handler.execute(player, Arrays.asList(actions));
    }

    public void execute(final Player player, final List<String> actions) {
        handler.execute(player, actions);
    }

}
