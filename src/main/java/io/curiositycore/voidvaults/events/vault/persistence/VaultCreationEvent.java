package io.curiositycore.voidvaults.events.vault.persistence;

import io.curiositycore.voidvaults.events.vault.VaultEvent;
import io.curiositycore.voidvaults.events.vault.VaultEventType;
import io.curiositycore.voidvaults.model.vault.Vault;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Event that represents the creation of a {@linkplain Vault vault}. Typically occurs when either commands or initial
 * players join the server and create a new vault.
 */
public class VaultCreationEvent extends VaultEvent {

    /**
     * Constructs a new VaultCreationEvent with the given {@linkplain Vault}, {@linkplain Player}, and {@linkplain Location}.
     * @param vault The vault that is involved in the event.
     * @param player The player that is involved in the event.
     * @param location The location of the event.
     */
    public VaultCreationEvent(Vault vault, Player player, Location location) {
        super(vault, player, location);
    }

    @Override
    protected String getLogMessage() {
        return "Created vault at " + location.toString() + " for " + player.getName() + ".";
    }

    @Override
    protected VaultEventType getType() {
        return VaultEventType.CREATION;
    }
}
