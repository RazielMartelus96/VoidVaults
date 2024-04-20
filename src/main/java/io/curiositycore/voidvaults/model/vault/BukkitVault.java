package io.curiositycore.voidvaults.model.vault;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class BukkitVault implements Vault{
    private int vaultId;
    private UUID ownerId;
    private int size;
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

    public static class BukkitVaultBuilder implements Builder<BukkitVault>{
        private int vaultId;
        private UUID ownerId;
        private int size;
        private Inventory inventory;

        @Override
        public Builder<BukkitVault> withOwnerId(UUID ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        @Override
        public Builder<BukkitVault> withId(int vaultId) {
            this.vaultId = vaultId;
            return this;
        }

        @Override
        public Builder<BukkitVault> withSize(int size) {
            this.size = size;
            return this;
        }

        @Override
        public Builder<BukkitVault> withFreeSize(int freeSize) {
            return this;
        }

        @Override
        public Builder<BukkitVault> withInventory(Inventory inventory) {
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
