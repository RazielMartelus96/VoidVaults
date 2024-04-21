package io.curiositycore.voidvaults.persistence.hikari;

import io.curiositycore.voidvaults.persistence.queries.DatabaseSchemaOperations;

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
        String sql = DatabaseSchemaOperations.CREATE_PLAYER_VAULTS_TABLE.getQuery();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to create player_vaults table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createItemsTable() {
        String sql = DatabaseSchemaOperations.CREATE_ITEMS_TABLE.getQuery();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to create vault_items table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
