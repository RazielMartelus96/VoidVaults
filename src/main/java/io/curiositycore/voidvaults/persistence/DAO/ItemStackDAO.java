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

public class ItemStackDAO {
    private final DataSource dataSource;
    private final Serializer<ItemStack> itemSerializer;

    public ItemStackDAO(DataSource dataSource, Serializer<ItemStack> itemSerializer) {
        this.dataSource = dataSource;
        this.itemSerializer = itemSerializer;
    }

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

    public void deleteItem(int vaultId, int slot) throws SQLException {
        String sql = DatabaseQueries.DELETE_ITEM.getQuery();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vaultId);
            stmt.setInt(2, slot);
            stmt.executeUpdate();
        }
    }

    public void deleteItems(int vaultId) throws SQLException {
        String sql = DatabaseQueries.DELETE_ITEMS.getQuery();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vaultId);
            stmt.executeUpdate();
        }
    }
}

