package io.curiositycore.voidvaults.persistence.DAO;

import io.curiositycore.voidvaults.persistence.queries.DatabaseQueries;
import io.curiositycore.voidvaults.persistence.serializer.Serializer;
import org.bukkit.inventory.ItemStack;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for interacting with the database to perform CRUD operations on {@linkplain ItemStack ItemStacks}.
 * This includes saving, updating, retrieving, and deleting ItemStack data.
 */
public class ItemStackDAO {

    /**
     * The {@linkplain DataSource} object used to connect to the database.
     */
    private final DataSource dataSource;

    /**
     * The {@linkplain Serializer} object used to serialize and deserialize {@linkplain ItemStack ItemStacks}.
     */
    private final Serializer<ItemStack> itemSerializer;

    /**
     * Constructor for the ItemStackDAO class. This constructor takes a {@linkplain DataSource} object and a {@linkplain
     * Serializer} object as parameters.
     * @param dataSource The {@linkplain DataSource} object used to connect to the database.
     * @param itemSerializer The {@linkplain Serializer} object used to serialize and deserialize {@linkplain ItemStack
     * ItemStacks}.
     */
    public ItemStackDAO(DataSource dataSource, Serializer<ItemStack> itemSerializer) {
        this.dataSource = dataSource;
        this.itemSerializer = itemSerializer;
    }

    /**
     * Saves an {@linkplain ItemStack} object in the database. If an item already exists in the slot, it is updated.
     * @param vaultId The ID of the vault where the item is to be saved.
     * @param slot The slot where the item is to be saved.
     * @param item The {@linkplain ItemStack} object to be saved.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public void saveItem(int vaultId, int slot, ItemStack item) throws SQLException {
        String sql = DatabaseQueries.SAVE_ITEM.getQuery();
        byte[] itemData = itemSerializer.serialize(item);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vaultId);
            stmt.setInt(2, slot);
            stmt.setBytes(3, itemData);
            stmt.executeUpdate();
        }
    }

    //TODO the logic doesnt quite work here as it doesnt get the slot number for each item.
    /**
     * Retrieves all {@linkplain ItemStack ItemStacks} in a vault from the database.
     * @param vaultId The ID of the vault from which the items are to be retrieved.
     * @return A {@linkplain List} of {@linkplain ItemStack ItemStacks} in the vault.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public List<ItemStack> getItemsByVaultId(int vaultId) throws SQLException {
        String sql = DatabaseQueries.GET_ITEMS_BY_VAULT_ID.getQuery();
        List<ItemStack> items = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vaultId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                byte[] itemData = rs.getBytes("item_data");
                ItemStack item = itemSerializer.deserialize(itemData);
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Deletes an {@linkplain ItemStack} object from the database.
     * @param vaultId The ID of the vault from which the item is to be deleted.
     * @param slot The slot where the item is to be deleted.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public void deleteItem(int vaultId, int slot) throws SQLException {
        String sql = DatabaseQueries.DELETE_ITEM.getQuery();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vaultId);
            stmt.setInt(2, slot);
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes all {@linkplain ItemStack ItemStacks} in a vault from the database. Typically used when deleting a vault
     * to prevent orphaned items.
     * @param vaultId The ID of the vault from which the items are to be deleted.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public void deleteItems(int vaultId) throws SQLException {
        String sql = DatabaseQueries.DELETE_ITEMS.getQuery();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vaultId);
            stmt.executeUpdate();
        }
    }
}

