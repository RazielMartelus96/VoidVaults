package io.curiositycore.voidvaults.listeners;

import io.curiositycore.voidvaults.persistence.DatabaseManager;
import io.curiositycore.voidvaults.util.AsyncUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Listener class for handling {@linkplain org.bukkit.event.player.PlayerEvent Player Events} called within the plugin.
 */
public class PlayerListener implements Listener {
    /**
     * Instance of the VoidVaults plugin.
     */
    private JavaPlugin plugin;

    /**
     * Instance of the Database Manager.
     */
    private DatabaseManager databaseManager;

    /**
     * Constructs a new PlayerListener with the given {@linkplain JavaPlugin} object. Whilst also initialising the
     * DatabaseManager via the singleton pattern.
     * @param plugin The JavaPlugin object.
     */
    public PlayerListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.databaseManager = DatabaseManager.getInstance();

    }

    /**
     * Handles the {@linkplain PlayerJoinEvent} event by initialising the player's vaults asynchronously.
     * @param event The PlayerJoinEvent event.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        AsyncUtil.runAsync(this.plugin, () -> {
            try {
                this.databaseManager.initialisePlayerVault(event.getPlayer().getUniqueId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //TODO this logic in the database handler needs to have a way to prevent reaccessing the vault until the
    //     saving process called here is completed.
    /**
     * Handles the {@linkplain PlayerQuitEvent} event by saving the player's vaults asynchronously.
     * @param event The PlayerQuitEvent event.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        AsyncUtil.runAsync(this.plugin, () -> {
            try {
                this.databaseManager.saveVaultsForPlayer(event.getPlayer().getUniqueId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
