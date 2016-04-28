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
        System.out.println(rb);
        System.out.println(rb instanceof Serializable);
        System.out.println(rb.getString("button"));

        ObjectOutputStream o = new ObjectOutputStream(System.out);
        o.writeObject(rb);
    }


}
