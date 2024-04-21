package io.curiositycore.voidvaults.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

/**
 * Utility class for running tasks asynchronously. This is typically utilised within the {@linkplain
 * io.curiositycore.voidvaults.listeners.VaultListener Vault Listener}, which has been designated to be the centralised
 * location of all Asynchronous logic within the plugin.
 */
public class AsyncUtil {

    /**
     * Run a task asynchronously, catching any exceptions that may occur and logging them to the server logs.
     * @param plugin The plugin instance that the task is being run for.
     * @param task The task to run asynchronously.
     */
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

    /**
     * Run a task asynchronously, catching any exceptions that may occur and logging them to the server logs. Once
     * the task completes successfully, it then runs a success callback task.
     * @param plugin The plugin instance that the task is being run for.
     * @param task The task to run asynchronously.
     * @param onSuccess The success callback task. Ran upon successful execution of the primary task.
     */
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
