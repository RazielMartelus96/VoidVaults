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


//TODO the idea is there can be more logic here in future that is detatched from the main determination of
//     typical Bukkit Events, so that way the determination listeners are not cluttered with other logic. Along with
//     that, asynchronous tasks can be run here as well to ensure no concurrency issues arise from overlapping async
//     tasks. This is ensured as all the async logic will be controlled centrally here, making it easier to locate
//     and debug any issues that may arise from async tasks.
public class VaultListener implements Listener {
    private JavaPlugin plugin;

    public VaultListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onVaultOpen(VaultOpenEvent event) {
        AsyncUtil.runAsync(plugin, event::log);
    }

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

    @EventHandler
    public void onContentChange(VaultContentsChangeEvent event) {
        AsyncUtil.runAsync(plugin, event::log);
    }



}
