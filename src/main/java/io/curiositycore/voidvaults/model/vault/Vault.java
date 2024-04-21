package io.curiositycore.voidvaults.model.vault;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

/**
 * <p>Interface representing the general functionality of a vault. An {@linkplain Inventory inventory} used to store items
 * persistently, even between servers. The vault is owned by a player, and has a limited size, as defined by the
 * config file.</p>
 * <i>The idea of this being an interface is for the purposes of allowing different types of Vault to be defined in
 * the future, such as maybe vaults that store virtual items, but represented visually by {@linkplain
 * org.bukkit.inventory.ItemStack ItemStacks}, hence imporving UX</i>
 */
public interface Vault {
    /**
     * Returns the ID of the vault within the database. Will return false if the Vault has yet to be saved to the
     * database.
     * @return The ID of the vault.
     */
    int getVaultId();

    /**
     * Returns the UUID of the player who owns the Vault.
     * @return The Owner's UUID.
     */
    UUID getOwnerId();

    /**
     * Returns the amount of free space in the Vault.
     * @return The Vault's free space.
     */
    int getFreeSize();

    /**
     * Returns the total size of the Vault.
     * @return The Vault's size.
     */
    int getSize();

    /**
     * Returns the {@linkplain Inventory} object that represents the Vault.
     * @return The Vault's Inventory representation.
     */
    Inventory getInventory();
}
