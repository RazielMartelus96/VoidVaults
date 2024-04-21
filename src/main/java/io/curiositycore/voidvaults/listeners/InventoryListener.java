package io.curiositycore.voidvaults.listeners;

import io.curiositycore.voidvaults.model.vault.VaultManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * <p>Listener class for handling {@linkplain org.bukkit.event.inventory.InventoryEvent Inventory Events} called within the
 * plugin. Typically used to determine when a player interacts with a vault inventory, and calls the appropriate
 * {@linkplain io.curiositycore.voidvaults.events.vault.VaultEvent Vault Event}.</p>
 * <i>Developer Note: Inventory.getHolder is BANNED from this listener. It is a silly method that causes ridiculous
 * bottlenecks if not utilised correctly (which, 9/10, "correctly" means "literally use any other way of doing the
 * check you are attempting to do").</i>
 */
public class InventoryListener implements Listener {

    /**
     * Determines if an Inventory Click Event is related to a vault inventory, and calls the appropriate event if so.
     * @param event The InventoryClickEvent event.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!VaultManager.getInstance().isVault(event.getInventory())) {
            return;
        }
        VaultManager.getInstance().callContentsChangeEvent(event);
    }

    /**
     * Determines if an Inventory Close Event is related to a vault inventory, and calls the appropriate event if so.
     * @param event The InventoryCloseEvent event.
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!VaultManager.getInstance().isVault(event.getInventory())) {
            return;
        }
        VaultManager.getInstance().callVaultCloseEvent(event);
    }

    /**
     * Determines if an Inventory Open Event is related to a vault inventory, and calls the appropriate event if so.
     * @param event The InventoryOpenEvent event.
     */
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if(!VaultManager.getInstance().isVault(event.getInventory())) {
            return;
        }
        VaultManager.getInstance().callVaultOpenEvent(event);
    }
}
