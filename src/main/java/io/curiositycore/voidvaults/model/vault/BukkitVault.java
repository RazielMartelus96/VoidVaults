package io.curiositycore.voidvaults.model.vault;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

/**
 * Implementation of the {@linkplain Vault} interface, representing a vault that is stores {@linkplain
 * org.bukkit.inventory.ItemStack ItemStacks} in the form of a Bukkit {@linkplain Inventory} object.
 */
public class BukkitVault implements Vault{
    /**
     * The ID of the Vault within the database.
     */
    private int vaultId;

    /**
     * The UUID of the player who owns the Vault.
     */
    private UUID ownerId;

    /**
     * The size of the Vault.
     */
    private int size;

    /**
     * The {@linkplain Inventory} object that represents the Vault.
     */
    private Inventory inventory;

    @Override
    public int getVaultId() {
        return vaultId;
    }

    @Override
    public UUID getOwnerId() {
        return ownerId;
    }

    @Override
    public int getFreeSize() {
        return size - inventory.getSize();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Builder class for the {@linkplain BukkitVault} class, implementing the {@linkplain VaultBuilder} interface.
     */
    public static class BukkitVaultVaultBuilder implements VaultBuilder<BukkitVault> {
        private int vaultId;
        private UUID ownerId;
        private int size;
        private Inventory inventory;

        @Override
        public VaultBuilder<BukkitVault> withOwnerId(UUID ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        @Override
        public VaultBuilder<BukkitVault> withId(int vaultId) {
            this.vaultId = vaultId;
            return this;
        }

        @Override
        public VaultBuilder<BukkitVault> withSize(int size) {
            this.size = size;
            return this;
        }

        @Override
        public VaultBuilder<BukkitVault> withFreeSize(int freeSize) {
            return this;
        }

        @Override
        public VaultBuilder<BukkitVault> withInventory(Inventory inventory) {
            this.inventory = inventory;
            return this;
        }

        @Override
        public BukkitVault build() {
            BukkitVault vault = new BukkitVault();
            vault.vaultId = this.vaultId;
            vault.ownerId = this.ownerId;
            vault.size = this.size;
            vault.inventory = this.inventory;
            return vault;
        }
    }

}
