package io.curiositycore.voidvaults.events.vault.persistence;

import io.curiositycore.voidvaults.events.vault.VaultEvent;
import io.curiositycore.voidvaults.events.vault.VaultEventType;
import io.curiositycore.voidvaults.model.vault.Vault;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VaultContentsChangeEvent extends VaultEvent {
    private final Map<Integer, ItemStack> changedSlots;

    public VaultContentsChangeEvent(Vault vault, Map<Integer, ItemStack> changedSlots, Player player, Location location) {
        super(vault, player, location);
        this.changedSlots = new HashMap<>(changedSlots);
    }

    /**
     * Gets a map of slots and their new contents after the change.
     * If a slot is empty after the change, it should map to null or an empty ItemStack,
     * depending on how your system represents empty slots.
     *
     * @return A map where keys are slot indices and values are ItemStacks.
     */
    public Map<Integer, ItemStack> getChangedSlots() {
        return Collections.unmodifiableMap(changedSlots);
    }

    @Override
    protected String getLogMessage() {
        return "Changed vault contents at " + location.toString() + " for " + player.getName() + ".";
    }

    @Override
    protected VaultEventType getType() {
        return VaultEventType.ITEM_CHANGE;
    }
}
