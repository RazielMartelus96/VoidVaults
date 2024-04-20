package io.curiositycore.voidvaults.listeners;

import io.curiositycore.voidvaults.model.vault.VaultManager;
import io.curiositycore.voidvaults.persistence.DatabaseManager;
import io.curiositycore.voidvaults.util.AsyncUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerListener implements Listener {
    private JavaPlugin plugin;
    private DatabaseManager databaseManager;
    public PlayerListener(JavaPlugin plugin) {

    }
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
