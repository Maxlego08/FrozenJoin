package com.github.frcsty.frozenjoin.action;

import com.github.frcsty.frozenjoin.action.actions.Action;
import com.github.frcsty.frozenjoin.action.actions.ActionData;
import com.github.frcsty.frozenjoin.action.time.TimeAPI;
import com.github.frcsty.frozenjoin.action.translator.TranslationException;
import com.github.frcsty.frozenjoin.action.translator.Translator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ActionExecutor {
    private final Plugin plugin;
    private final Logger logger;
    private final Map<String, ActionData> actionDatas = new HashMap<>();
    private final Map<Class<?>, Translator<?>> translators = new HashMap<>();
    private final Pattern actionPattern = Pattern.compile("(.*) ?\\[(?<action>[A-Z]+?)] ?(?<arguments>.+)", Pattern.CASE_INSENSITIVE);
    private final Pattern chancePattern = Pattern.compile("\\[CHANCE=(?<chanceValue>\\d+)]", Pattern.CASE_INSENSITIVE);
    private final Pattern delayPattern = Pattern.compile("\\[DELAY=(?<delayValue>\\d+[a-z])]", Pattern.CASE_INSENSITIVE);
    private Matcher actionMatcher;
    private Matcher chanceMatcher;
    private Matcher delayMatcher;

    ActionExecutor(Plugin plugin) {
        this.plugin = plugin;
        logger = plugin.getLogger();
    }

    private static Class<?>[] prependPlayerType(Class<?>[] array) {
        Class<?>[] newArray = new Class<?>[array.length + 2];

        newArray[0] = Player.class;
        newArray[1] = Plugin.class;
        System.arraycopy(array, 0, newArray, 2, array.length);

        return newArray;
    }

    void executeActions(Player player, List<String> actions) {
        actions.forEach(action -> handleAction(player, action));
    }

    private void handleAction(Player player, String input) {
        actionMatcher = actionMatcher == null ? actionPattern.matcher(input) : actionMatcher.reset(input);

        if (!actionMatcher.matches()) {
            throw new IllegalStateException("action does not follow regex: " + input);
        }

        if (!shouldExecuteChance(input)) {
            return;
        }

        String action = actionMatcher.group("action").toUpperCase();
        ActionData data = actionDatas.get(action);

        if (data == null) {
            throw new IllegalStateException("action " + action + " does not exist");
        }

        String inputArguments = actionMatcher.group("arguments");
        String[] argSplit = inputArguments.split(";", -1);

        Object[] arguments = getTranslatedArgs(player, argSplit, data.getParameterTypes());
        long delay = getDelay(input);
        if (delay == 0L) {
            data.execute(player, arguments);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, () -> data.execute(player, arguments), delay);
        }
    }

    private boolean shouldExecuteChance(String action) {
        chanceMatcher = chanceMatcher == null ? chancePattern.matcher(action) : chanceMatcher.reset(action);

        if (!chanceMatcher.find()) {
            return true;
        }

        int chance = Integer.parseInt(chanceMatcher.group("chanceValue"));
        int random = ThreadLocalRandom.current().nextInt(100) + 1;

        return random <= chance;
    }

    private long getDelay(String action) {
        delayMatcher = delayMatcher == null ? delayPattern.matcher(action) : delayMatcher.reset(action);

        if (!delayMatcher.find()) {
            return 0L;
        }

        String delay = delayMatcher.group("delayValue");

        if (delay == null) {
            return 0L;
        }

        try {
            TimeAPI time = new TimeAPI(delay);
            return (long) time.getSeconds() * 20;
        } catch (IllegalArgumentException ex) {
            logger.severe(delay + " is not a valid time");
            return 0L;
        }
    }

    private Object[] getTranslatedArgs(Player player, String[] inputs, Class<?>[] parameterTypes) {
        if (inputs.length != parameterTypes.length) {
            throw new IllegalStateException(String.format("entered arguments size does not match (given %d, expected %d)", inputs.length, parameterTypes.length));
        }

        Object[] arguments = new Object[parameterTypes.length + 1];
        arguments[0] = plugin;

        for (int i = 0; i < inputs.length; i++) {
            String input = inputs[i];

            if (input.equals("")) {
                arguments[i] = null;
                continue;
            }

            Translator<?> translator = translators.get(parameterTypes[i]);

            if (translator == null) {
                throw new IllegalStateException("translator does not exist for type " + input);
            }

            try {
                arguments[i + 1] = translator.translate(player, input);
            } catch (TranslationException ex) {
                logger.severe("error while translating argument");
                ex.printStackTrace();
            }
        }

        return arguments;
    }

    void registerTranslator(Translator<?> translator, Class<?>... classes) {
        for (Class<?> clazz : classes) {
            translators.put(clazz, translator);
        }
    }

    void registerActionClass(String key, Class<? extends Action> actionClass, Class<?>... parameterTypes) {
        try {
            Method method = actionClass.getMethod("execute", prependPlayerType(parameterTypes));

            actionDatas.put(key, new ActionData(key, method, parameterTypes));
        } catch (NoSuchMethodException ex) {
            logger.severe("Unable to register action " + key + ": missing execute(...) method");
        }
    }
}
