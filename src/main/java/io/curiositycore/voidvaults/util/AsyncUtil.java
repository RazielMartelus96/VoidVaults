package io.curiositycore.voidvaults.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class AsyncUtil {

    public static void runAsync(JavaPlugin plugin, Runnable task) {
        new BukkitRunnable() {
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    // Log the exception to server logs, consider using plugin.getLogger() for plugin-specific logs
                    plugin.getLogger().severe("An error occurred in an asynchronous task: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
    public static void runTaskAsync(JavaPlugin plugin, Runnable task, Runnable onSuccess, Consumer<Exception> onError) {
        new BukkitRunnable() {
            public void run() {
                try {
                    task.run();
                    onSuccess.run(); // Call the success callback
                } catch (Exception e) {
                    onError.accept(e); // Handle exceptions using the error consumer
                    plugin.getLogger().severe("An error occurred in an asynchronous task: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
