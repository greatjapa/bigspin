package com.skatepark.kickflip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

public class DefaultI18N implements I18N, Serializable {

    private Map<String, String> values;

    private Function<String, String> callback;

    public DefaultI18N(ResourceBundle bundle){
        this();
        if (bundle == null){
            throw new IllegalArgumentException("bundle can't be null.");
        }
        Enumeration<String> keys = bundle.getKeys();
        while(keys.hasMoreElements()){
            String key = (String)keys.nextElement();
            String value = bundle.getString(key);
            values.put(key, value);
        }
    }

    private DefaultI18N(){
        values = new HashMap<>();
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public String get(Object object, String key) {
        return null;
    }

    @Override
    public String get(String key, String... args) {
        return null;
    }

    @Override
    public String get(Object object, String key, String... args) {
        return null;
    }

    @Override
    public Set<String> keys() {
        return null;
    }

    @Override
    public boolean has(String key) {
        return false;
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public void put(String key, String value) {

    }

    @Override
    public void append(I18N i18n) {

    }

    @Override
    public void append(ResourceBundle bundle) {

    }

    @Override
    public void append(Map<String, String> values) {

    }

    @Override
    public void append(Properties properties) {

    }
}
