package me.kubaw208.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

import java.util.Random;

public class Utils {

    /**
     * Colors the component message with hex colors
     * @return Component with hex colors message
     */
    public static Component hexComponent(String message) {
        var mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(message);
        return parsed;
    }

    /**
     * Generate random number between two values
     * @param lower Lowest number
     * @param upper Highest number
     * @return Random number between lower and upper variables
     */
    public static int getRandom(int lower, int upper) {
        return new Random().nextInt(upper - lower + 1) + lower;
    }

    /**
     * Cancel running task
     * @param object Task id
     * @return null
     */
    public static Object cancelTask(Object object) {
        if(object != null) Bukkit.getScheduler().cancelTask((int) object);
        return null;
    }

}