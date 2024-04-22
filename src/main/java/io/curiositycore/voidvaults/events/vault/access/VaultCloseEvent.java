package io.curiositycore.voidvaults.events.vault.access;

import io.curiositycore.voidvaults.events.vault.VaultEvent;
import io.curiositycore.voidvaults.events.vault.VaultEventType;
import io.curiositycore.voidvaults.model.vault.Vault;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * Event that represents the closing of a {@linkplain Vault vault}. Typically, occurs when a player closes their vault.
 */
public class VaultCloseEvent extends VaultEvent {

    /**
     * Constructs a new VaultCloseEvent with the given {@linkplain Vault}, {@linkplain Player}, and {@linkplain Location}.
     * @param vault The vault that is involved in the event.
     * @param player The player that is involved in the event.
     * @param location The location of the event.
     */
    public VaultCloseEvent(Vault vault, Player player, Location location) {
        super(vault, player, location);
    }

    @Override
    protected String getLogMessage() {
        return "Closed vault at " + location.toString() + " for " + player.getName() + ".";
    }

    @Override
    protected VaultEventType getType() {
        return VaultEventType.CLOSE;
    }

}



