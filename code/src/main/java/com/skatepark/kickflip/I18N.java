package com.skatepark.kickflip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Marcelo Oikawa
 */
public class I18N implements Serializable {

    private Map<String, String> values;

    public I18N(ResourceBundle bundle){
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

    private I18N(){
        values = new HashMap<>();
    }
}
