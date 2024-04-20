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

public abstract class VaultEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    protected Vault vault;
    protected final Player player;
    protected final Location location;
    protected final Date timestamp;
    protected final VaultEventType type;

    public VaultEvent(Vault vault, Player player, Location location) {
        this.vault = vault;
        this.player = player;
        this.location = location;
        this.timestamp = new Date();
        this.type = getType();
    }

    public Vault getVault() {
        return vault;
    }
    public void log() {
        Logger.getInstance().log(this.type, getLogMessage());
    }
    protected abstract String getLogMessage();
    protected abstract VaultEventType getType();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
