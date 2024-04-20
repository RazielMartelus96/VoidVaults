package io.curiositycore.voidvaults.persistence.queries;

public enum DatabaseQueries {
    SAVE_VAULT("INSERT INTO player_vaults (player_uuid) VALUES (?) " +
            "ON DUPLICATE KEY UPDATE player_uuid = VALUES(player_uuid)"),
    DELETE_VAULT("DELETE FROM player_vaults WHERE vault_id = ?"),
    SAVE_ITEM("INSERT INTO vault_items (vault_id, slot, item_data) VALUES (?, ?, ?) " +
                      "ON DUPLICATE KEY UPDATE item_data = ?"),
    GET_ITEMS_BY_VAULT_ID("SELECT slot, item_data FROM vault_items WHERE vault_id = ?"),
    DELETE_ITEM("DELETE FROM vault_items WHERE vault_id = ? AND slot = ?"),
    DELETE_ITEMS("DELETE FROM vault_items WHERE vault_id = ?"),
    GET_VAULT_ID_BY_PLAYER_UUID("SELECT vault_id FROM player_vaults WHERE player_uuid = ?");

    private final String query;

    DatabaseQueries(String query) {
        this.query = query;
    }
    public String getQuery() {
        return query;
    }



}
