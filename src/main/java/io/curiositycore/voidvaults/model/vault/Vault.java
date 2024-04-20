package io.curiositycore.voidvaults.model.vault;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

public interface Vault {
    int getVaultId();
    UUID getOwnerId();
    int getFreeSize();
    int getSize();
    Inventory getInventory();
}
