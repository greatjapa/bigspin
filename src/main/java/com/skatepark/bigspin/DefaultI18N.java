package com.skatepark.bigspin;

import java.io.File;
import java.io.FileInputStream;
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
     * Map with internationalized messages mapped by keys.
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
     * Constructs an {@link I18N} object with the file content.
     *
     * @param filePath path to file.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(String filePath) throws IOException {
        this(filePath, null, null);
    }

    /**
     * Constructs an {@link I18N} object with the file content.
     *
     * @param filePath path to file.
     * @param parent   parent.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(String filePath, I18N parent) throws IOException {
        this(filePath, parent, null);
    }

    /**
     * Constructs an {@link I18N} object with the file content.
     *
     * @param filePath path to file.
     * @param parent   parent.
     * @param callback callback used to build a message not found.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(String filePath, I18N parent, Function<String, String> callback) throws IOException {
        this(Objects.isNull(filePath) ? null : new File(filePath), parent, callback);
    }

    /**
     * Constructs an {@link I18N} object with the file content.
     *
     * @param file file.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(File file) throws IOException {
        this(file, null);
    }

    /**
     * Constructs an {@link I18N} object with the file content.
     *
     * @param file   file.
     * @param parent parent.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(File file, I18N parent) throws IOException {
        this(file, parent, null);
    }

    /**
     * Constructs an {@link I18N} object with the file content.
     *
     * @param file     file.
     * @param parent   parent.
     * @param callback callback used to build a message not found.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(File file, I18N parent, Function<String, String> callback) throws IOException {
        this(Objects.isNull(file) ? null : new FileInputStream(file), parent, callback);
    }

    /**
     * Constructs an {@link I18N} object with the stream content.
     *
     * @param stream stream.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(InputStream stream) throws IOException {
        this(stream, null);
    }

    /**
     * Constructs an {@link I18N} object with the stream content.
     *
     * @param stream stream.
     * @param parent parent.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(InputStream stream, I18N parent) throws IOException {
        this(stream, parent, null);
    }

    /**
     * Constructs an {@link I18N} object with the stream content.
     *
     * @param stream   stream.
     * @param parent   parent.
     * @param callback callback used to build a message not found.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(InputStream stream, I18N parent, Function<String, String> callback) throws IOException {
        this(Objects.isNull(stream) ? null : new InputStreamReader(stream), parent, callback);
    }

    /**
     * Constructs an {@link I18N} object with the reader content.
     *
     * @param reader reader.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(Reader reader) throws IOException {
        this(reader, null);
    }

    /**
     * Constructs an {@link I18N} object with the reader content.
     *
     * @param reader reader.
     * @param parent parent.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(Reader reader, I18N parent) throws IOException {
        this(reader, parent, null);
    }

    /**
     * Constructs an {@link I18N} object with the reader content.
     *
     * @param reader   reader.
     * @param parent   parent.
     * @param callback callback used to build a message not found.
     * @throws IOException If an input/output exception occur.
     */
    public DefaultI18N(Reader reader, I18N parent, Function<String, String> callback) throws IOException {
        Objects.requireNonNull(reader, "reader can't be null.");

        this.values = toMap(reader);
        this.parent = parent;
        this.callback = callback != null ? callback : key -> "<<" + key + ">>";
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
        return args != null ? MessageFormat.format(value, args) : value;
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
     * @throws IOException If an input/output exception occur.
     */
    private Map<String, String> toMap(Reader reader) throws IOException {
        Properties props = new Properties();
        props.load(reader);
        return props.entrySet().stream().collect(Collectors.toMap(
                e -> String.valueOf(e.getKey()),
                e -> String.valueOf(e.getValue())));
    }
}
