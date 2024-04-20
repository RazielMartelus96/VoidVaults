package io.curiositycore.voidvaults.listeners;

import io.curiositycore.voidvaults.model.vault.VaultManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!VaultManager.getInstance().isVault(event.getInventory())) {
            return;
        }
        VaultManager.getInstance().callContentsChangeEvent(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!VaultManager.getInstance().isVault(event.getInventory())) {
            return;
        }
        VaultManager.getInstance().callVaultCloseEvent(event);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if(!VaultManager.getInstance().isVault(event.getInventory())) {
            return;
        }
        VaultManager.getInstance().callVaultOpenEvent(event);
    }
}
