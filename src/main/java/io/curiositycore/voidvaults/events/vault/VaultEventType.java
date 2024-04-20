package io.curiositycore.voidvaults.events.vault;

/**
 * Enum representing the different type of {@linkplain VaultEvent Vault Events}
 * within the plugin.
 */
public enum VaultEventType {
    /**
     * Represents the creation of a {@linkplain io.curiositycore.voidvaults.model.vault.Vault Vault}.
     */
    CREATION,
    /**
     * Represents the deletion of a {@linkplain io.curiositycore.voidvaults.model.vault.Vault Vault}.
     */
    DELETION,
    /**
     * Represents the opening of a {@linkplain io.curiositycore.voidvaults.model.vault.Vault Vault}.
     */
    OPEN,
    /**
     * Represents the closing of a {@linkplain io.curiositycore.voidvaults.model.vault.Vault Vault}.
     */
    CLOSE,
    /**
     * Represents the change of an item within a {@linkplain io.curiositycore.voidvaults.model.vault.Vault Vault}.
     */
    ITEM_CHANGE;

    public String getEventName() {
        return capitalizeWords(this.name().toLowerCase().replaceAll("_", " "));
    }

    private String capitalizeWords(String input) {
        // Split the input into words
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                result.append(word.substring(1)).append(" ");
            }
        }
        return result.toString().trim();
    }

}
