package io.curiositycore.voidvaults.events.vault.persistence;

import io.curiositycore.voidvaults.events.vault.VaultEvent;
import io.curiositycore.voidvaults.events.vault.VaultEventType;
import io.curiositycore.voidvaults.model.vault.Vault;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class VaultCreationEvent extends VaultEvent {
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
