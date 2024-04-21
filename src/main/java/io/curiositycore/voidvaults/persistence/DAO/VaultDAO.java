package io.curiositycore.voidvaults.persistence.DAO;

import io.curiositycore.voidvaults.model.vault.BukkitVault;
import io.curiositycore.voidvaults.model.vault.Vault;
import io.curiositycore.voidvaults.persistence.queries.DatabaseQueries;
import io.curiositycore.voidvaults.persistence.serializer.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Data Access Object for interacting with the database to perform CRUD operations on {@linkplain Vault Vaults}. This
 * includes saving, updating, retrieving, and deleting vault data.
 */
public class VaultDAO {
    /**
     * The {@linkplain DataSource} object used to connect to the database.
     */
    private final DataSource dataSource;

    /**
     * The {@linkplain Serializer} object used to serialize and deserialize {@linkplain ItemStack ItemStacks}.
     */
    private final Serializer<ItemStack> itemSerializer;

    /**
     * The {@linkplain ItemStackDAO} object used to interact with the database to perform CRUD operations on {@linkplain
     * ItemStack ItemStacks}.
     */
    private final ItemStackDAO itemStackDAO;


    /**
     * Constructor for the VaultDAO class. This constructor takes a {@linkplain DataSource} object and a {@linkplain
     * Serializer} object as parameters and initializes the {@linkplain ItemStackDAO} object.
     * @param dataSource The {@linkplain DataSource} object used to connect to the database.
     * @param itemSerializer The {@linkplain Serializer} object used to serialize and deserialize {@linkplain ItemStack
     * ItemStacks}.
     */
    public VaultDAO(DataSource dataSource, Serializer<ItemStack> itemSerializer) {
        this.dataSource = dataSource;
        this.itemSerializer = itemSerializer;
        this.itemStackDAO = new ItemStackDAO(dataSource, itemSerializer);

    }

    //TODO this has flawed logic as Players can have multiple vaults.
    /**
     * Retrieves a {@linkplain Vault} object for a player with the given UUID from the database.
     * @param playerUuid The UUID of the player whose vault is to be retrieved.
     * @return An {@linkplain Optional} containing the {@linkplain Vault} object if it exists, or an empty {@linkplain
     * Optional} if the vault does not exist.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public Optional<Vault> getVaultByPlayerUUID(UUID playerUuid) throws SQLException {
        Optional<Integer> vaultId = getVaultIdByPlayerUUID(playerUuid);
        if (vaultId.isEmpty()) {
            return Optional.empty();
        }
        List<ItemStack> items = this.itemStackDAO.getItemsByVaultId(vaultId.get());
        return Optional.of(createVault(playerUuid, items));
    }

    /**
     * Saves a {@linkplain Vault} object for a player with the given UUID to the database.
     * @param playerUuid The UUID of the player whose vault is to be saved.
     * @param vault The {@linkplain Vault} object to be saved.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public void saveVaultForPlayer(UUID playerUuid, Vault vault) throws SQLException {
        // Upsert vault data
        int vaultId = upsertVault(playerUuid, vault);
        // Process each item in the vault
        ItemStack[] contents = vault.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                this.itemStackDAO.saveItem(vaultId, i, contents[i]);  // Save each item at its respective slot
            }
        }
    }
    //TODO once again flawed logic, as Players can have multiple vaults.
    /**
     * Deletes a {@linkplain Vault} object for a player with the given UUID from the database.
     * @param playerUuid The UUID of the player whose vault is to be deleted.
     * @param vault The {@linkplain Vault} object to be deleted.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public void deleteVaultForPlayer(UUID playerUuid, Vault vault) throws SQLException {
        Optional<Integer> vaultId = getVaultIdByPlayerUUID(playerUuid);
        if (vaultId.isEmpty()) {
            return;
        }
        this.itemStackDAO.deleteItems(vaultId.get());
        deleteVault(vaultId.get());
    }

    //TODO once again the logic is flawed as Players can have multiple vaults.
    /**
     * Upserts a {@linkplain Vault} object for a player with the given UUID to the database. If the player already has a
     * vault, the data is updated.
     * @param playerUuid The UUID of the player whose vault is to be upserted.
     * @param vault The {@linkplain Vault} object to be upserted.
     * @return
     */
    private int upsertVault(UUID playerUuid, Vault vault) {
        final String sql = DatabaseQueries.SAVE_VAULT.getQuery();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, playerUuid.toString());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return getVaultIdByPlayerUUID(playerUuid).orElseThrow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Indicate failure
    }

    //TODO once again the logic is flawed, as Players can have multiple vaults.
    /**
     * Retrieves the ID of a vault for a player with the given UUID from the database.
     * @param playerUuid The UUID of the player whose vault ID is to be retrieved.
     * @return An {@linkplain Optional} containing the ID of the vault if it exists, or an empty {@linkplain Optional} if
     * the vault does not exist.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    private Optional<Integer> getVaultIdByPlayerUUID(UUID playerUuid) throws SQLException {
        String sql = DatabaseQueries.GET_VAULT_ID_BY_PLAYER_UUID.getQuery();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerUuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt("vault_id"));
                }
            }
        }
        return Optional.empty();
    }

    //TODO this logic is flawed as the itemstacks are not good enough, the slot index for each item needs to be
    //     stored in the database as well.
    /**
     * Creates a {@linkplain Vault} object with the given player UUID and list of {@linkplain ItemStack ItemStacks}.
     * @param playerUUID The UUID of the player who owns the vault.
     * @param items The list of {@linkplain ItemStack ItemStacks} to be stored in the vault.
     * @return The {@linkplain Vault} object created.
     */
    private Vault createVault(UUID playerUUID, List<ItemStack> items) {
        BukkitVault.BukkitVaultVaultBuilder builder = new BukkitVault.BukkitVaultVaultBuilder();
        Inventory inventory = Bukkit.createInventory(null, 27);
        items.forEach(inventory::addItem);
        builder.withSize(27)
                .withFreeSize(27-items.size())
                .withOwnerId(playerUUID)
                .withId(1).withInventory(inventory);
        return builder.build();
    }

    /**
     * Deletes a vault with the given ID from the database.
     * @param vaultId The ID of the vault to be deleted.
     */
    private void deleteVault(int vaultId) {
        String sql = DatabaseQueries.DELETE_VAULT.getQuery();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vaultId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
