package com.skatepark.kickflip;

import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public interface I18N {

    String get(String key);

    String get(Object object, String key);

    String get(String key, String...args);

    String get(Object object, String key, String...args);

    Set<String> keys();

    boolean has(String key);

    void remove(String key);

    void put(String key, String value);

    void append(I18N i18n);

    void append(ResourceBundle bundle);

    void append(Map<String, String> values);

    void append(Properties properties);
}