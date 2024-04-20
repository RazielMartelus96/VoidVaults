package io.curiositycore.voidvaults.persistence.queries;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public enum DatabaseSchemaOperations {
    CREATE_PLAYER_VAULTS_TABLE(
            "CREATE TABLE IF NOT EXISTS player_vaults (" +
                    "vault_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "player_uuid CHAR(36) NOT NULL," +
                    "vault_data BLOB," +
                    "UNIQUE INDEX idx_player_uuid (player_uuid)," +
                    "INDEX idx_vault_id (vault_id)" +
                    ");"
    ),
    CREATE_ITEMS_TABLE(
            "CREATE TABLE IF NOT EXISTS vault_items (" +
                    "item_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "vault_id INT NOT NULL," +
                    "slot INT NOT NULL," +  // This is the new column to store the slot index
                    "item_data BLOB NOT NULL," +
                    "FOREIGN KEY (vault_id) REFERENCES player_vaults(vault_id)" +
                    " ON DELETE CASCADE ON UPDATE CASCADE," +
                    "UNIQUE (vault_id, slot)" +  // Ensure that each slot in a vault is unique
                    ");"
    );


    private final String sql;

    DatabaseSchemaOperations(String sql) {
        this.sql = sql;
    }

    public void execute(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(this.sql);
        } catch (SQLException e) {
            System.err.println("Failed to execute operation " + this.name() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
