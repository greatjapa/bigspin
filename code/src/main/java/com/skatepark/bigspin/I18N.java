package com.skatepark.bigspin;

import java.util.Set;

/**
 * This interface defines basic methods for access internationalized messages from an object.
 *
 * Why use an interface to define I18N object? Because you can build your own I18N mechanism. In
 * this library we provide {@link DefaultI18N} as a most common implementation that read a
 * property-like file as {@link java.io.InputStream} or {@link java.io.Reader}.
 *
 * All {@link I18N} instances may also define an optional {@link I18N} parent. So that, you can
 * build an hierarchical mechanism to internationalize your messages and you can redefine messages
 * if it's desired.
 *
 * @author Marcelo Oikawa
 * @see DefaultI18N
 */
public interface I18N {

    /**
     * Returns an internationalized message mapped by the given key.
     *
     * @param key key.
     * @return message.
     */
    String get(String key);

    /**
     * Returns an internationalized message mapped by the given key.
     *
     * @param key  key.
     * @param args arguments referenced by the format specifiers in the internationalized format
     *             string.
     * @return message.
     */
    String get(String key, String... args);

    /**
     * Returns all keys defined in this object.
     *
     * @return set of keys.
     */
    Set<String> keys();

    /**
     * Returns all keys defined in this hierarchical {@link I18N} objects.
     *
     * @return set of keys.
     */
    Set<String> allKeys();

    /**
     * Returns the number of keys defined in this object.
     *
     * @return number of keys.
     */
    int size();

    /**
     * Returns the number of keys defined in this hierarchical {@link I18N} objects.
     *
     * @return total of keys.
     */
    int total();

    /**
     * Verify if the given key is defined.
     *
     * @param key key.
     * @return true if the given key is defined, false otherwise.
     */
    boolean has(String key);

    /**
     * Returns the {@link I18N} parent.
     *
     * @return an {@link I18N} parent or null if not defined.
     */
    I18N getParent();
}