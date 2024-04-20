package io.curiositycore.voidvaults.persistence.serializer;

/**
 * Interface representing the general functionality of a converter that can serialize and deserialize objects for the
 * purpose of persistence, such as to a database. Implementations of this interface should be able to convert objects
 * of type T to a byte array and vice versa.
 * @param <T> Type parameter representing the type of object that can be serialized and deserialized by the
 */
public interface Serializer<T> {
    /**
     * Serializes the given object to a byte array.
     * @param object The object to serialize.
     * @return A byte array representing the serialized object.
     */
    byte[] serialize(T object);

    /**
     * Deserializes the given byte array into an object of type T.
     * @param serialized The byte array to deserialize.
     * @return An object of type T representing the deserialized byte array.
     */
    T deserialize(byte[] serialized);
}
