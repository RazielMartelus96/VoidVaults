package io.curiositycore.voidvaults.persistence.queries;

/**
 * Enum containing all the SQL queries used by the plugin. This includes queries for saving and retrieving data from the
 * database, along with queries for deleting data.
 * */
public enum DatabaseQueries {
    /**
     * Query to save a player's vault data. If the player already has a vault, the data is updated.
     */
    SAVE_VAULT("INSERT INTO player_vaults (player_uuid) VALUES (?) " +
            "ON DUPLICATE KEY UPDATE player_uuid = VALUES(player_uuid)"),

    /**
     * Query to delete a player's vault data.
     */
    DELETE_VAULT("DELETE FROM player_vaults WHERE vault_id = ?"),

    /**
     * Query to save an item in a player's vault. If an item already exists in the slot, it is updated.
     */
    SAVE_ITEM("INSERT INTO vault_items (vault_id, slot, item_data) VALUES (?, ?, ?) " +
                      "ON DUPLICATE KEY UPDATE item_data = ?"),

    /**
     * Query to get all items in a player's vault.
     */
    GET_ITEMS_BY_VAULT_ID("SELECT slot, item_data FROM vault_items WHERE vault_id = ?"),

    /**
     * Query to delete an item from a player's vault.
     */
    DELETE_ITEM("DELETE FROM vault_items WHERE vault_id = ? AND slot = ?"),

    /**
     * Query to delete all items from a player's vault.
     */
    DELETE_ITEMS("DELETE FROM vault_items WHERE vault_id = ?"),

    /**
     * Query to get a Vault's ID via a Player's UUID.
     */
    GET_VAULT_ID_BY_PLAYER_UUID("SELECT vault_id FROM player_vaults WHERE player_uuid = ?");

    /**
     * The SQL query string required to perform the operation.
     */
    private final String query;

/**
     * Constructor for the enum.
     * @param query The SQL query string required to perform the operation.
     */
    DatabaseQueries(String query) {
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
