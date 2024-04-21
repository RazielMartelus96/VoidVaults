package io.curiositycore.voidvaults.listeners;

import io.curiositycore.voidvaults.events.vault.access.VaultCloseEvent;
import io.curiositycore.voidvaults.events.vault.access.VaultOpenEvent;
import io.curiositycore.voidvaults.events.vault.persistence.VaultContentsChangeEvent;
import io.curiositycore.voidvaults.events.vault.persistence.VaultCreationEvent;
import io.curiositycore.voidvaults.events.vault.persistence.VaultDeletionEvent;
import io.curiositycore.voidvaults.persistence.DatabaseManager;
import io.curiositycore.voidvaults.util.AsyncUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;


/**
 * Listener class for handling {@linkplain io.curiositycore.voidvaults.events.vault.VaultEvent Vault Events} called
 * within the plugin.
 */
public class VaultListener implements Listener {
    /**
     * Instance of the VoidVaults plugin.
     */
    private final JavaPlugin plugin;

    /**
     * Constructs a new VaultListener with the given {@linkplain JavaPlugin} object.
     * @param plugin The JavaPlugin object.
     */
    public VaultListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the {@linkplain VaultOpenEvent} event by logging the event asynchronously.
     * @param event The VaultOpenEvent event.
     */
    @EventHandler
    public void onVaultOpen(VaultOpenEvent event) {
        AsyncUtil.runAsync(plugin, event::log);
    }

    /**
     * Handles the {@linkplain VaultCloseEvent} event by logging the event asynchronously and saving the vault data to
     * the database.
     * @param event The VaultCloseEvent event.
     */
    @EventHandler
    public void onVaultClose(VaultCloseEvent event) {
        AsyncUtil.runTaskAsync(plugin, event::log, () -> {
            try {
                DatabaseManager.getInstance().saveVaultsForPlayer(event.getVault().getOwnerId(), event.getVault());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            },
                Throwable::printStackTrace
        );
    }

    //TODO might actually be better to save the vault data when closing the inventory? As there is that chance of a
    //     player closing the inventory too quickly and the data not being initially saved before the vault close
    //     event is called.
    /**
     * Handles the {@linkplain VaultCreationEvent} event by logging the event asynchronously and saving the vault
     * data to the database.
     * @param event The VaultCreationEvent event.
     */
    @EventHandler
    public void onVaultCreation(VaultCreationEvent event) {
        AsyncUtil.runTaskAsync(plugin, event::log, () -> {
            try {
                DatabaseManager.getInstance().saveVaultsForPlayer(event.getVault().getOwnerId(), event.getVault());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            },
                Throwable::printStackTrace
        );
        AsyncUtil.runAsync(plugin, event::log);
    }

    /**
     * Handles the {@linkplain VaultDeletionEvent} event by logging the event asynchronously and deleting the vault
     * data from the database.
     * @param event The VaultDeletionEvent event.
     */
    @EventHandler
    public void onVaultDeletion(VaultDeletionEvent event) {
        AsyncUtil.runTaskAsync(plugin, event::log, () -> {
            try {
                DatabaseManager.getInstance().deleteVaultForPlayer(event.getVault().getOwnerId(), event.getVault());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            },
                Throwable::printStackTrace
        );
        AsyncUtil.runAsync(plugin, event::log);
    }

    /**
     * Handles the {@linkplain VaultContentsChangeEvent} event by logging the event asynchronously.
     * @param event The VaultContentsChangeEvent event.
     */
    @EventHandler
    public void onContentChange(VaultContentsChangeEvent event) {
        AsyncUtil.runAsync(plugin, event::log);
    }



}
