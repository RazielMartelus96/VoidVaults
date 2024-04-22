package io.curiositycore.voidvaults.events.vault;

import io.curiositycore.voidvaults.events.vault.VaultEventType;
import io.curiositycore.voidvaults.util.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import io.curiositycore.voidvaults.model.vault.Vault;

import java.util.Date;

/**
 * Abstract that represents the generic {@linkplain Event events} that involve {@linkplain Vault vaults}. All events
 * that involve vaults should extend this class.
 */
public abstract class VaultEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The vault that is involved in the event.
     */
    protected Vault vault;

    /**
     * The player that is involved in the event.
     */
    protected final Player player;

    /**
     * The location of the event.
     */
    protected final Location location;

    /**
     * The timestamp of the event.
     */
    protected final Date timestamp;

    /**
     * The type of Vault event.
     */
    protected final VaultEventType type;


    /**
     * Constructs a new VaultEvent.
     * @param vault The vault that is involved in the event.
     * @param player The player that is involved in the event.
     * @param location The location of the event.
     */
    public VaultEvent(Vault vault, Player player, Location location) {
        this.vault = vault;
        this.player = player;
        this.location = location;
        this.timestamp = new Date();
        this.type = getType();
    }

    /**
     * Gets the player that is involved in the event.
     * @return The involved player
     */
    public Vault getVault() {
        return vault;
    }

    /**
     * Logs the event to the plugin's logger.
     */
    public void log() {
        Logger.getInstance().log(this.type, getLogMessage());
    }

    /**
     * Abstract method that returns the log message for the event.
     * @return The log message.
     */
    protected abstract String getLogMessage();

    /**
     * Abstract method that returns the type of the event.
     * @return The type of the event.
     */
    protected abstract VaultEventType getType();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
