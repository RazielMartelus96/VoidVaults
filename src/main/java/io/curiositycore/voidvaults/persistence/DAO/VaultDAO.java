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

public class VaultDAO {
    private final DataSource dataSource;
    private final Serializer<ItemStack> itemSerializer;
    private final ItemStackDAO itemStackDAO;

    public VaultDAO(DataSource dataSource, Serializer<ItemStack> itemSerializer) {
        this.dataSource = dataSource;
        this.itemSerializer = itemSerializer;
        this.itemStackDAO = new ItemStackDAO(dataSource, itemSerializer);

    }

    public Optional<Vault> getVaultByPlayerUUID(UUID playerUuid) throws SQLException {
        Optional<Integer> vaultId = getVaultIdByPlayerUUID(playerUuid);
        if (vaultId.isEmpty()) {
            return Optional.empty();
        }
        List<ItemStack> items = this.itemStackDAO.getItemsByVaultId(vaultId.get());
        return Optional.of(createVault(playerUuid, items));
    }

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

    public void deleteVaultForPlayer(UUID playerUuid, Vault vault) throws SQLException {
        Optional<Integer> vaultId = getVaultIdByPlayerUUID(playerUuid);
        if (vaultId.isEmpty()) {
            return;
        }
        this.itemStackDAO.deleteItems(vaultId.get());
        deleteVault(vaultId.get());
    }
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

    private Vault createVault(UUID playerUUID, List<ItemStack> items) {
        BukkitVault.BukkitVaultBuilder builder = new BukkitVault.BukkitVaultBuilder();
        Inventory inventory = Bukkit.createInventory(null, 27);
        items.forEach(inventory::addItem);
        builder.withSize(27)
                .withFreeSize(27-items.size())
                .withOwnerId(playerUUID)
                .withId(1).withInventory(inventory);
        return builder.build();
    }

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
