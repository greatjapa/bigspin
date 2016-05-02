package com.skatepark.bigspin;

import java.util.Set;

public interface I18N {

    String get(String key);

    String get(String key, String...args);

    Set<String> keys();

    Set<String> allKeys();

    int size();

    int total();

    boolean has(String key);

    //TODO put remove?

    I18N getParent();
}