package io.curiositycore.voidvaults.persistence.serializer;

import org.bukkit.UnsafeValues;
import org.bukkit.inventory.ItemStack;

/**
 * Serialiser for ItemStacks that currently uses the existing {@linkplain UnsafeValues Unsafe Values} implementation
 * within paper. In future this paper implementation may not exist as it is currently marked as deprecated.
 */
public class ItemStackSerializer implements Serializer<ItemStack>{
    /**
     * The {@linkplain UnsafeValues Unsafe Values} implementation to use for serialisation and deserialisation of the
     * ItemStacks.
     */
    private final UnsafeValues unsafeValues;

    /**
     * Constructs a new ItemStackSerializer with the given {@linkplain UnsafeValues Unsafe Values} implementation.
     * @param unsafeValues The UnsafeValues implementation.
     */
    public ItemStackSerializer(UnsafeValues unsafeValues) {
        this.unsafeValues = unsafeValues;
    }

    @Override
    public byte[] serialize(ItemStack itemStack) {
        return this.unsafeValues.serializeItem(itemStack);
    }

    @Override
    public ItemStack deserialize(byte[] serializedData) {
        return this.unsafeValues.deserializeItem(serializedData);
    }
}
