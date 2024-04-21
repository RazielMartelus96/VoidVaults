package io.curiositycore.voidvaults.model.vault;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

/**
 * Interface representing the general functionality of a vault builder (as per the builder pattern).
 * The builder is used to allow the construction of a {@linkplain Vault} object in a more flexible way. The logic
 * behind this was that sometimes vaults are made from scratch by players (using commands or accessing vaults for
 * the first time), and also must be able to be loaded from the database.<p>
 * <i>This interface allows for the construction of Vaults for either scenario, without needing to have a large variety of
 * constructors in the Vault class itself.</i>
 * @param <T> The type of Vault that the builder will construct.
 */
public interface VaultBuilder<T extends Vault>
{
    /**
     * Sets the UUID of the owner of the Vault within the builder.
     * @param ownerId The UUID of the owner.
     * @return The amended builder object.
     */
    VaultBuilder<T> withOwnerId(UUID ownerId);

    /**
     * Sets the database ID of the Vault within the builder.
     * @param vaultId The ID of the Vault.
     * @return The amended builder object.
     */
    VaultBuilder<T> withId(int vaultId);

    /**
     * Sets the size of the Vault within the builder.
     * @param size The size of the Vault.
     * @return The amended builder object.
     */
    VaultBuilder<T> withSize(int size);

    /**
     * Sets the amount of free space in the Vault within the builder.
     * @param freeSize The Vault's free space.
     * @return The amended builder object.
     */
    VaultBuilder<T> withFreeSize(int freeSize);

    /**
     * Sets the {@linkplain Inventory} object that represents the Vault within the builder.
     * @param inventory The Vault's Inventory representation.
     * @return The amended builder object.
     */
    VaultBuilder<T> withInventory(Inventory inventory);

    /**
     * Builds the Vault object from the builder's set parameters.
     * @return The Vault object.
     */
    T build();
}
