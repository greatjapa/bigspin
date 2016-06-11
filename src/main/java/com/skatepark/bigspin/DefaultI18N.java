package com.skatepark.bigspin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A basic implementation of {@link I18N} interface.
 *
 * This object keeps all messages in-memory and it's {@link java.io.Serializable}, so you can send
 * to another VM, share this object with multiple distributed modules, etc. Instances may also
 * defines an optional parent and build an hierarchical mechanism to internationalize your
 * messages.
 *
 * If there isn't an message mapped by given key, this object returns a 'callback message' built by
 * a callback optionally defined on construction.
 *
 * @author Marcelo Oikawa
 */
public class DefaultI18N implements I18N, Serializable {

    /**
     * Map with internationalized messages.
     */
    private Map<String, String> values;

    /**
     * An optional parent.
     */
    private I18N parent;

    /**
     * Callback used to build a message not found.
     */
    private Function<String, String> callback;

    /**
     * Constructs an {@link I18N} object with file content.
     *
     * @param filePath path to file.
     */
    public DefaultI18N(String filePath) {
        this(filePath, null, null);
    }

    /**
     * Constructs an {@link I18N} object with file content.
     *
     * @param filePath path to file.
     * @param parent   parent.
     */
    public DefaultI18N(String filePath, I18N parent) {
        this(filePath, parent, null);
    }

    /**
     * Constructs an {@link I18N} object with file content.
     *
     * @param filePath path to file.
     * @param parent   parent.
     * @param callback callback used to build a message not found.
     */
    public DefaultI18N(String filePath, I18N parent, Function<String, String> callback) {
        this(Objects.isNull(filePath) ? null : new File(filePath), parent, callback);
    }

    /**
     * Constructs an {@link I18N} object with file content.
     *
     * @param file file.
     */
    public DefaultI18N(File file) {
        this(file, null);
    }

    /**
     * Constructs an {@link I18N} object with file content.
     *
     * @param file   file.
     * @param parent parent.
     */
    public DefaultI18N(File file, I18N parent) {
        this(file, parent, null);
    }

    /**
     * Constructs an {@link I18N} object with file content.
     *
     * @param file     file.
     * @param parent   parent.
     * @param callback callback used to build a message not found.
     */
    public DefaultI18N(File file, I18N parent, Function<String, String> callback) {
        this(toStream(file), parent, callback);
    }


    /**
     * Constructs an {@link I18N} object with stream content.
     *
     * @param stream stream.
     */
    public DefaultI18N(InputStream stream) {
        this(stream, null);
    }

    /**
     * Constructs an {@link I18N} object with stream content.
     *
     * @param stream stream.
     * @param parent parent.
     */
    public DefaultI18N(InputStream stream, I18N parent) {
        this(stream, parent, null);
    }

    /**
     * Constructs an {@link I18N} object with stream content.
     *
     * @param stream   stream.
     * @param parent   parent.
     * @param callback callback used to build a message not found.
     */
    public DefaultI18N(InputStream stream, I18N parent, Function<String, String> callback) {
        this(Objects.isNull(stream) ? null : new InputStreamReader(stream), parent, callback);
    }

    /**
     * Constructs an {@link I18N} object with reader content.
     *
     * @param reader reader.
     */
    public DefaultI18N(Reader reader) {
        this(reader, null);
    }

    /**
     * Constructs an {@link I18N} object with reader content.
     *
     * @param reader reader.
     * @param parent parent.
     */
    public DefaultI18N(Reader reader, I18N parent) {
        this(reader, parent, null);
    }

    /**
     * Constructs an {@link I18N} object with reader content.
     *
     * @param reader   reader.
     * @param parent   parent.
     * @param callback callback used to build a message not found.
     */
    public DefaultI18N(Reader reader, I18N parent, Function<String, String> callback) {
        this(toMap(reader), parent, callback);
    }

    /**
     * Constructs an {@link I18N} object with bundle content.
     *
     * @param bundle bundle.
     */
    public DefaultI18N(ResourceBundle bundle) {
        this(bundle, null);
    }

    /**
     * Constructs an {@link I18N} object with bundle content.
     *
     * @param bundle bundle.
     * @param parent parent.
     */
    public DefaultI18N(ResourceBundle bundle, I18N parent) {
        this(bundle, parent, null);
    }

    /**
     * Constructs an {@link I18N} object with bundle content.
     *
     * @param bundle   bundle.
     * @param parent   parent.
     * @param callback callback used to build a message not found.
     */
    public DefaultI18N(ResourceBundle bundle, I18N parent, Function<String, String> callback) {
        this(toMap(bundle), parent, callback);
    }

    /**
     * Constructs an {@link I18N} object with map content.
     *
     * @param values values.
     */
    public DefaultI18N(Map<String, String> values) {
        this(values, null, null);
    }

    /**
     * Constructs an {@link I18N} object with map content.
     *
     * @param values values.
     * @param parent parent.
     */
    public DefaultI18N(Map<String, String> values, I18N parent) {
        this(values, parent, null);
    }

    /**
     * Constructs an {@link I18N} object with map content.
     *
     * @param values   values.
     * @param parent   parent.
     * @param callback callback used to build a message not found.
     */
    public DefaultI18N(Map<String, String> values, I18N parent, Function<String, String> callback) {
        Objects.requireNonNull(values, "values can't be null.");
        this.values = values;
        this.parent = parent;
        this.callback = Objects.nonNull(callback) ? callback : key -> "<<" + key + ">>";
    }

    @Override
    public String get(String key) {
        return get(key, null);
    }

    @Override
    public String get(String key, String... args) {
        if (!has(key)) {
            return callback.apply(key);
        }

        String value = values.get(key);
        if (value == null && parent != null) {
            value = parent.get(key);
        }

        return Objects.isNull(args) ? value : MessageFormat.format(value, args);
    }

    @Override
    public Set<String> keys() {
        return values.keySet();
    }

    @Override
    public Set<String> allKeys() {
        Set<String> allKeys = new HashSet(values.keySet());
        if (parent != null) {
            allKeys.addAll(parent.allKeys());
        }
        return allKeys;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public int total() {
        return allKeys().size();
    }

    @Override
    public boolean has(String key) {
        return values.containsKey(key) || (parent != null && parent.has(key));
    }

    @Override
    public I18N getParent() {
        return parent;
    }

    /**
     * Read all {@link Reader} content and build a map with internationalized messages.
     *
     * @param reader reader.
     * @return map with internationalized messages.
     */
    private static Map<String, String> toMap(Reader reader) {
        if (Objects.isNull(reader)) {
            return null;
        }
        try {
            Properties props = new Properties();
            props.load(reader);
            return props.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> String.valueOf(e.getKey()),
                            e -> String.valueOf(e.getValue())));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Read all {@link ResourceBundle} content and build a map with internationalized messages.
     *
     * @param bundle bundle.
     * @return map with internationalized messages.
     */
    private static Map<String, String> toMap(ResourceBundle bundle) {
        if (Objects.isNull(bundle)) {
            return null;
        }
        return bundle.keySet().stream()
                .collect(Collectors.toMap(key -> key, key -> bundle.getString(key)));
    }

    /**
     * Convert file to input stream.
     *
     * @param file file.
     * @return input stream.
     */
    private static InputStream toStream(File file) {
        if (Objects.isNull(file)) {
            return null;
        }
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
