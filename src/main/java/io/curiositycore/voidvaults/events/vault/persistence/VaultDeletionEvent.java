package io.curiositycore.voidvaults.events.vault.persistence;

import io.curiositycore.voidvaults.events.vault.VaultEvent;
import io.curiositycore.voidvaults.events.vault.VaultEventType;
import io.curiositycore.voidvaults.model.vault.Vault;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Event that represents the deletion of a {@linkplain Vault vault}. Typically occurs when a player deletes their vault
 * or when a system deletes a vault.
 */
public class VaultDeletionEvent extends VaultEvent {

    /**
     * Constructs a new VaultDeletionEvent with the given {@linkplain Vault}, {@linkplain Player}, and {@linkplain Location}.
     * @param vault The vault that is involved in the event.
     * @param player The player that is involved in the event.
     * @param location The location of the event.
     */
    public VaultDeletionEvent(Vault vault, Player player, Location location) {
        super(vault, player, location);
    }

    @Override
    protected String getLogMessage() {
        String deleter = player == null ? "System" : player.getName();
        if(location == null) {
            return deleter + " deleted vault owning to " + Bukkit.getOfflinePlayer(vault.getOwnerId()) + ".";
        }
        return deleter + " deleted vault at " + location + " owning to " + Bukkit.getOfflinePlayer(vault.getOwnerId()) + ".";

    }

    @Override
    protected VaultEventType getType() {
        return VaultEventType.DELETION;
    }
}
