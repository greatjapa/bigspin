package com.skatepark.kickflip;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

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

    private DefaultI18N(Map<String, String> values, I18N parent, Function<String, String> callback) {
        this.values = values;
        this.parent = parent;
        this.callback = callback != null ? callback : key -> "<<" + key + ">>";
    }

    @Override
    public String get(String key) {
        if (!has(key)) {
            return callback.apply(key);
        }
        return values.get(key);
    }

    @Override
    public String get(String key, String... args) {
        if (!has(key)) {
            return callback.apply(key);
        }
        return MessageFormat.format(values.get(key), args);
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
    public void remove(String key) {
        values.remove(key);
    }

    @Override
    public void put(String key, String value) {
        values.put(key, value);
    }

    @Override
    public I18N getParent() {
        return parent;
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
