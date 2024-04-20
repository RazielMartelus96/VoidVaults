package io.curiositycore.voidvaults.model.vault;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

public interface Builder <T extends Vault>
{
    Builder<T> withOwnerId(UUID ownerId);
    Builder<T> withId(int vaultId);
    Builder<T> withSize(int size);
    Builder<T> withFreeSize(int freeSize);
    Builder<T> withInventory(Inventory inventory);

    T build();
}
