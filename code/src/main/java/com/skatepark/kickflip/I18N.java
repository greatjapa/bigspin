package com.skatepark.kickflip;

import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public interface I18N {

    String get(String key);

    String get(String key, String...args);

    Set<String> keys();

    boolean has(String key);

    void put(String key, String value);

    void remove(String key);

    I18N getParent();
}