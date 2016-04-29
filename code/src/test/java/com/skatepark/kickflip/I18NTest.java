package com.skatepark.kickflip;

import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Test for {@link I18N}
 *
 * @author Marcelo Oikawa
 */
public class I18NTest {

    @Test
    public void test()throws IOException{
        ResourceBundle rb = ResourceBundle.getBundle("idiom", new Locale("pt", "BR"));

        I18N i18n = new DefaultI18N(rb);
        i18n.get("key", "defaultValue");
    }


}
