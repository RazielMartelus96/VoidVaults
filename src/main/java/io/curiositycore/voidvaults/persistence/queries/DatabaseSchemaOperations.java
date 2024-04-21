package io.curiositycore.voidvaults.persistence.queries;

/**
 * Enum containing SQL operations to be executed on the database for the purposes of Schema creation, such as creating
 * tables.
 */
public enum DatabaseSchemaOperations {
    /**
     * Operation to create the player_vaults table. Used to define each vault in terms of owner unique vault ID.
     */
    CREATE_PLAYER_VAULTS_TABLE(
            "CREATE TABLE IF NOT EXISTS player_vaults (" +
                    "vault_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "player_uuid CHAR(36) NOT NULL," +
                    "vault_data BLOB," +
                    "UNIQUE INDEX idx_player_uuid (player_uuid)," +
                    "INDEX idx_vault_id (vault_id)" +
                    ");"
    ),
    /**
     * Operation to create the vault_items table. Used to store the metadata of each stored item, relating it to its
     * parent vault.
     */
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

    /**
     * The SQL statement to be executed.
     */
    private final String query;

    /**
     * Constructor for the enum.
     * @param query The SQL statement to be executed.
     */
    DatabaseSchemaOperations(String query) {
        this.query = query;
    }
    /**
     * Getter for the SQL query string.
     * @return The query string.
     */
    public String getQuery() {
        return query;
    }

}
