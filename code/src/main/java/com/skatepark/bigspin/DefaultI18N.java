package com.skatepark.bigspin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultI18N implements I18N, Serializable {

    private Map<String, String> values;

    private I18N parent;

    private Function<String, String> callback;

    public DefaultI18N(InputStream stream) throws IOException{
        this(stream, null);
    }

    public DefaultI18N(InputStream stream, I18N parent)throws IOException {
        this(stream, parent, null);
    }

    public DefaultI18N(InputStream stream, I18N parent, Function<String, String> callback) throws IOException{
        this(stream == null ? null : new InputStreamReader(stream), parent, callback);
    }

    public DefaultI18N(Reader reader) throws IOException{
        this(reader, null);
    }

    public DefaultI18N(Reader reader, I18N parent)throws IOException {
        this(reader, parent, null);
    }

    public DefaultI18N(Reader reader, I18N parent, Function<String, String> callback) throws IOException {
        if (reader == null) {
            throw new IllegalArgumentException("reader can't be null.");
        }
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
        return size() + (parent == null ? 0 : parent.total());
    }

    @Override
    public boolean has(String key) {
        return values.containsKey(key) || (parent != null && parent.has(key));
    }

    @Override
    public I18N getParent() {
        return parent;
    }

    private Map<String, String> toMap(Reader reader) throws IOException {
        Properties props = new Properties();
        props.load(reader);
        return props.entrySet().stream().collect(Collectors.toMap(
                e -> String.valueOf(e.getKey()),
                e -> String.valueOf(e.getValue())));
    }
}
