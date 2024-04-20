package io.curiositycore.voidvaults.persistence.hikari;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private final DataSource dataSource;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Initialize the database by creating necessary tables and setting up the initial configuration.
     */
    public void initializeDatabase() {
        createPlayerVaultsTable();
        createItemsTable();
    }

    private void createPlayerVaultsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_vaults (" +
                "vault_id INT AUTO_INCREMENT PRIMARY KEY," +
                "player_uuid CHAR(36) NOT NULL," +
                "UNIQUE INDEX idx_player_uuid (player_uuid)," +
                "INDEX idx_vault_id (vault_id)" +
                ");";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to create player_vaults table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createItemsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS vault_items (" +
                "item_id INT AUTO_INCREMENT PRIMARY KEY," +
                "vault_id INT NOT NULL," +
                "item_data BLOB NOT NULL," +  // Serialized item stack data
                "FOREIGN KEY (vault_id) REFERENCES player_vaults(vault_id)" +
                " ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to create vault_items table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
