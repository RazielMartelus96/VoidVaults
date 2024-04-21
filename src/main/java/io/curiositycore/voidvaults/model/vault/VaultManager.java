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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manager responsible for the caching, registering and general functionality of {@linkplain Vault Vaults} within the
 * plugin. The idea behind this class is to allow for a centralised location for all Vault related logic, preventing
 * confusing responsibility structure elsewhere in the plugin.
 */
public class VaultManager {

    /**
     * Static instance of the VaultManager, as per the Singleton pattern.
     */
    private static VaultManager instance;

    /**
     * Gets the instance of the VaultManager, creating one if it does not exist, as per the Singleton pattern.
     * @return The instance of the VaultManager.
     */

    public static VaultManager getInstance() {
        if (instance == null) {
            instance = new VaultManager();
        }
        return instance;
    }

    /**
     * <p>Map of all Vaults within the plugin, keyed by their {@linkplain Inventory} representation. This allows for
     * quick access to the Vault object when an Inventory is interacted with.</p>
     * <i>Most Vault plugins attempt to use the UUID of the player, then checking the Manager's caches to find any
     * potential Vaults. This Map based approach is more efficient, as it is essentially O(1) time complexity.</i>
     */
    private Map<Inventory,Vault> vaults = new HashMap<>();

    /**
     * <p>Map of all Vaults owned by players, keyed by the player's {@linkplain UUID}. This is typically for when a player
     * is accessing the Inventory Menu for selecting which Vault to open, hence a quick way to access all Vaults owned
     * by the player is ideal.</p>
     * <i>The reasoning for this cache is purely decorative for menus, and in turn would not be used in listeners,
     * hence the List of Vaults is an acceptable bottleneck as it is called upon much less frequently than the vaults
     * map.</i>
     */
    private Map<UUID, List<Vault>> playerVaults = new HashMap<>();


    /**
     * Registers a Vault with the VaultManager, adding it to both of the caches.
     * @param vault The Vault to be registered.
     */
    public void registerVault(Vault vault) {
        vaults.put(vault.getInventory(), vault);
        playerVaults.putIfAbsent(vault.getOwnerId(), List.of());
        if(playerVaults.get(vault.getOwnerId()).contains(vault)) {
            return;
        }
        playerVaults.get(vault.getOwnerId()).add(vault);
    }

    /**
     * Unregisters a Vault with the VaultManager, removing it from both of the caches. Typically done at the point which
     * the player either deletes the Vault, or logs out of the server, to prevent memory leaks.
     * @param vault The Vault to be unregistered.
     */
    public void unregisterVault(Vault vault) {
        vaults.remove(vault.getInventory());
        playerVaults.get(vault.getOwnerId()).remove(vault);
    }

    /**
     * <p>Gets the Vaults for a particular player, based on their Unique Identifier. This is typically used for constructing
     * the Inventory Menu for selecting which Vault to open. Additionally used for when a player requires their vaults
     * saved to the database.</p>
     * <i>Note: This method should not be used in methods that directly manipulate the Vaults returned in the List,
     * as it could lead to unexpected behaviour if called across the plugin.</i>
     * @param player The UUID of the player to get the Vaults for.
     * @return A List of Vaults owned by the player.
     */
    public List<Vault> getVaultsForPlayer(UUID player) {
        return playerVaults.get(player);
    }

    /**
     * Checks if an Inventory is a Vault, based on the VaultManager's cache. This is typically called in the {@linkplain
     * io.curiositycore.voidvaults.listeners.InventoryListener Inventory Listener} to ensure that the {@linkplain
     * Inventory Inventory} being interacted with is a Vault.
     * @param inventory The Inventory to check.
     * @return True if the Inventory is a Vault, false otherwise.
     */
    public boolean isVault(Inventory inventory) {
        return vaults.containsKey(inventory);
    }

    //TODO needs better logic for removals and additions of items, basically a check to see if the item is being added
    //     or removed and then call the appropriate event

    /**
     * Calls the {@linkplain VaultContentsChangeEvent} event, which is used to notify the plugin that the contents of a
     * Vault have changed. This is typically called in the {@linkplain
     * io.curiositycore.voidvaults.listeners.InventoryListener Inventory Listener} when it is detected that an
     * Inventory that has been clicked is a Vault.
     * @param inventoryClickEvent The InventoryClickEvent that triggered the call.
     */
    public void callContentsChangeEvent(InventoryClickEvent inventoryClickEvent) {
        Vault vault = vaults.get(inventoryClickEvent.getInventory());
        Map<Integer, ItemStack> changedSlots = Map.of(inventoryClickEvent.getSlot(), inventoryClickEvent.getCurrentItem());
        Bukkit.getPluginManager().callEvent(new VaultContentsChangeEvent(vault,
                changedSlots,
                (Player) inventoryClickEvent.getWhoClicked(),
                inventoryClickEvent.getInventory().getLocation()));
    }

    /**
     * Calls the {@linkplain VaultOpenEvent} event, which is used to notify the plugin that a Vault has been opened.
     * This is typically called in the {@linkplain io.curiositycore.voidvaults.listeners.InventoryListener Inventory
     * Listener} when it is detected that an Inventory that has been opened is a Vault.
     * @param event The InventoryOpenEvent that triggered the call.
     */
    public void callVaultOpenEvent(InventoryOpenEvent event) {
        Vault vault = vaults.get(event.getInventory());
        if(vault == null) {
            throw new IllegalArgumentException("Inventory is not a vault");
        }
        Bukkit.getPluginManager().callEvent(new VaultOpenEvent(vault, (Player) event.getPlayer(), event.getInventory().getLocation()));
    }

    /**
     * Calls the {@linkplain VaultOpenEvent} event, which is used to notify the plugin that a Vault has been opened.
     * This is typically called in the {@linkplain io.curiositycore.voidvaults.listeners.InventoryListener Inventory
     * Listener} when it is detected that an Inventory that has been opened is a Vault.
     * @param event The InventoryOpenEvent that triggered the call.
     */
    public void callVaultCloseEvent(InventoryCloseEvent event) {
        Vault vault = vaults.get(event.getInventory());
        if(vault == null) {
            throw new IllegalArgumentException("Inventory is not a vault");
        }
        Bukkit.getPluginManager().callEvent(new VaultOpenEvent(vault, (Player) event.getPlayer(), event.getInventory().getLocation()));
    }


}
