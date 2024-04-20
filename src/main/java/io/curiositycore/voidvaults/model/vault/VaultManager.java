package io.curiositycore.voidvaults.model.vault;

import io.curiositycore.voidvaults.events.vault.access.VaultOpenEvent;
import io.curiositycore.voidvaults.events.vault.persistence.VaultContentsChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VaultManager {
    private static VaultManager instance;

    public static VaultManager getInstance() {
        if (instance == null) {
            instance = new VaultManager();
        }
        return instance;
    }

    private Map<Inventory,Vault> vaults;
    private Map<UUID, List<Vault>> playerVaults;

    public void registerVault(Vault vault) {
        vaults.put(vault.getInventory(), vault);
    }

    public void unregisterVault(Vault vault) {
        vaults.remove(vault.getInventory());
    }

    public List<Vault> getVaultForPlayer(UUID player) {
        return playerVaults.get(player);
    }

    public boolean isVault(Inventory inventory) {
        return vaults.containsKey(inventory);
    }

    //TODO needs better logic for removals and additions of items, basically a check to see if the item is being added
    //     or removed and then call the appropriate event
    public void callContentsChangeEvent(InventoryClickEvent inventoryClickEvent) {
        Vault vault = vaults.get(inventoryClickEvent.getInventory());
        Map<Integer, ItemStack> changedSlots = Map.of(inventoryClickEvent.getSlot(), inventoryClickEvent.getCurrentItem());
        Bukkit.getPluginManager().callEvent(new VaultContentsChangeEvent(vault,
                changedSlots,
                (Player) inventoryClickEvent.getWhoClicked(),
                inventoryClickEvent.getInventory().getLocation()));
    }

    public void callVaultOpenEvent(InventoryOpenEvent event) {
        Vault vault = vaults.get(event.getInventory());
        if(vault == null) {
            throw new IllegalArgumentException("Inventory is not a vault");
        }
        Bukkit.getPluginManager().callEvent(new VaultOpenEvent(vault, (Player) event.getPlayer(), event.getInventory().getLocation()));
    }

    public void callVaultCloseEvent(InventoryCloseEvent event) {
        Vault vault = vaults.get(event.getInventory());
        if(vault == null) {
            throw new IllegalArgumentException("Inventory is not a vault");
        }
        Bukkit.getPluginManager().callEvent(new VaultOpenEvent(vault, (Player) event.getPlayer(), event.getInventory().getLocation()));
    }


}
