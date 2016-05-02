package com.skatepark.bigspin;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultI18N implements I18N, Serializable {

    private Map<String, String> values;

    private I18N parent;

    private Function<String, String> callback;

    public DefaultI18N(ResourceBundle bundle) {
        this(bundle, null);
    }

    public DefaultI18N(ResourceBundle bundle, I18N parent) {
        this(bundle, parent, null);
    }

    public DefaultI18N(ResourceBundle bundle, I18N parent, Function<String, String> callback) {
        this(toMap(bundle), parent, callback);
    }

    public DefaultI18N(Properties props) {
        this(props, null);
    }

    public DefaultI18N(Properties props, I18N parent) {
        this(props, parent, null);
    }

    public DefaultI18N(Properties props, I18N parent, Function<String, String> callback) {
        this(toMap(props), parent, callback);
    }

    private DefaultI18N(Map<String, String> values, I18N parent, Function<String, String> callback) {
        this.values = values;
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

    private static Map<String, String> toMap(Properties props) {
        if (props == null) {
            throw new IllegalArgumentException("props can't be null.");
        }
        return props.entrySet().stream().collect(Collectors.toMap(
                e -> String.valueOf(e.getKey()),
                e -> String.valueOf(e.getValue())));
    }

    private static Map<String, String> toMap(ResourceBundle bundle) {
        if (bundle == null) {
            throw new IllegalArgumentException("bundle can't be null.");
        }
        Map<String, String> values = new HashMap<>();
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = bundle.getString(key);
            values.put(key, value);
        }
        return values;
    }
}
