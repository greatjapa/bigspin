package com.skatepark.kickflip;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Test for {@link I18N}
 *
 * @author Marcelo Oikawa
 */
public class DefaultI18NTest {

    private static final String BASE_FILE = "base";

    private static final String GUI_FILE = "gui";

    @Test(expected = IllegalArgumentException.class)
    public void testResourceBundleNull(){
        new DefaultI18N(null);
    }

    @Test
    public void testGetParent(){
        ResourceBundle baseBundle = ResourceBundle.getBundle(BASE_FILE, new Locale("pt", "BR"));

        I18N first = new DefaultI18N(baseBundle);
        I18N second = new DefaultI18N(baseBundle, first);

        Assert.assertNull(first.getParent());
        Assert.assertSame(first, second.getParent());
    }

    @Test
    public void testHas(){
        ResourceBundle baseBundle = ResourceBundle.getBundle(BASE_FILE, new Locale("pt", "BR"));
        ResourceBundle guiBundle = ResourceBundle.getBundle(GUI_FILE, new Locale("pt", "BR"));

        I18N baseI18N = new DefaultI18N(baseBundle);
        I18N guiI18N = new DefaultI18N(guiBundle, baseI18N);

        Assert.assertTrue(baseI18N.has("ok"));
        Assert.assertTrue(baseI18N.has("no"));
        Assert.assertTrue(baseI18N.has("cancel"));

        Assert.assertFalse(baseI18N.has("file"));

        Assert.assertTrue(guiI18N.has("ok"));
        Assert.assertTrue(guiI18N.has("no"));
        Assert.assertTrue(guiI18N.has("cancel"));
        Assert.assertTrue(guiI18N.has("file"));
        Assert.assertTrue(guiI18N.has("tools"));

        Assert.assertFalse(guiI18N.has("no.message"));
    }

    @Test
    public void testSize(){
        ResourceBundle baseBundle = ResourceBundle.getBundle(BASE_FILE, new Locale("pt", "BR"));
        ResourceBundle guiBundle = ResourceBundle.getBundle(GUI_FILE, new Locale("pt", "BR"));

        I18N baseI18N = new DefaultI18N(baseBundle);
        I18N guiI18N = new DefaultI18N(guiBundle, baseI18N);

        Assert.assertEquals(3, baseI18N.size());
        Assert.assertEquals(2, guiI18N.size());

        Assert.assertEquals(3, baseI18N.total());
        Assert.assertEquals(5, guiI18N.total());
    }

    @Test
    public void testTotal(){
        ResourceBundle baseBundle = ResourceBundle.getBundle(BASE_FILE, new Locale("pt", "BR"));
        ResourceBundle guiBundle = ResourceBundle.getBundle(GUI_FILE, new Locale("pt", "BR"));

        I18N baseI18N = new DefaultI18N(baseBundle);
        I18N guiI18N = new DefaultI18N(guiBundle, baseI18N);

        Assert.assertEquals(3, baseI18N.total());
        Assert.assertEquals(5, guiI18N.total());
    }

    @Test
    public void testKeys(){
        ResourceBundle baseBundle = ResourceBundle.getBundle(BASE_FILE, new Locale("pt", "BR"));
        ResourceBundle guiBundle = ResourceBundle.getBundle(GUI_FILE, new Locale("pt", "BR"));

        I18N baseI18N = new DefaultI18N(baseBundle);
        I18N guiI18N = new DefaultI18N(guiBundle, baseI18N);

        Set<String> baseKeys = baseI18N.keys();

        Assert.assertNotNull(baseKeys);
        Assert.assertTrue(baseKeys.contains("ok"));
        Assert.assertTrue(baseKeys.contains("no"));
        Assert.assertTrue(baseKeys.contains("cancel"));
        Assert.assertFalse(baseKeys.contains("no.message"));

        Set<String> guiKeys = guiI18N.keys();

        Assert.assertNotNull(guiKeys);
        Assert.assertTrue(guiKeys.contains("file"));
        Assert.assertTrue(guiKeys.contains("tools"));
        Assert.assertFalse(guiKeys.contains("no.message"));
    }

    @Test
    public void testAllKeys(){
        ResourceBundle baseBundle = ResourceBundle.getBundle(BASE_FILE, new Locale("pt", "BR"));
        ResourceBundle guiBundle = ResourceBundle.getBundle(GUI_FILE, new Locale("pt", "BR"));

        I18N baseI18N = new DefaultI18N(baseBundle);
        I18N guiI18N = new DefaultI18N(guiBundle, baseI18N);

        Set<String> allBaseKeys = baseI18N.allKeys();

        Assert.assertNotNull(allBaseKeys);
        Assert.assertTrue(allBaseKeys.contains("ok"));
        Assert.assertTrue(allBaseKeys.contains("no"));
        Assert.assertTrue(allBaseKeys.contains("cancel"));
        Assert.assertFalse(allBaseKeys.contains("no.message"));

        Set<String> allKeys = guiI18N.allKeys();

        Assert.assertNotNull(allKeys);

        Assert.assertTrue(allKeys.contains("ok"));
        Assert.assertTrue(allKeys.contains("no"));
        Assert.assertTrue(allKeys.contains("cancel"));
        Assert.assertTrue(allKeys.contains("file"));
        Assert.assertTrue(allKeys.contains("tools"));
        Assert.assertFalse(allKeys.contains("no.message"));
    }

    @Test
    public void testPut(){
        ResourceBundle baseBundle = ResourceBundle.getBundle(BASE_FILE, new Locale("pt", "BR"));
        ResourceBundle guiBundle = ResourceBundle.getBundle(GUI_FILE, new Locale("pt", "BR"));

        I18N baseI18N = new DefaultI18N(baseBundle);
        I18N guiI18N = new DefaultI18N(guiBundle, baseI18N);

        Assert.assertEquals(3, baseI18N.total());
        Assert.assertEquals(5, guiI18N.total());
        Assert.assertFalse(guiI18N.has("edit"));

        guiI18N.put("edit", "Editar");

        Assert.assertEquals(3, baseI18N.total());
        Assert.assertEquals(6, guiI18N.total());
        Assert.assertTrue(guiI18N.has("edit"));
        Assert.assertEquals("Editar", guiI18N.get("edit"));
    }

    @Test
    public void testRemove(){
        ResourceBundle baseBundle = ResourceBundle.getBundle(BASE_FILE, new Locale("pt", "BR"));
        ResourceBundle guiBundle = ResourceBundle.getBundle(GUI_FILE, new Locale("pt", "BR"));

        I18N baseI18N = new DefaultI18N(baseBundle);
        I18N guiI18N = new DefaultI18N(guiBundle, baseI18N);

        Assert.assertEquals(3, baseI18N.total());
        Assert.assertEquals(5, guiI18N.total());
        Assert.assertTrue(guiI18N.has("file"));

        guiI18N.remove("file");

        Assert.assertEquals(3, baseI18N.total());
        Assert.assertEquals(4, guiI18N.total());
        Assert.assertFalse(guiI18N.has("file"));
        Assert.assertEquals("<<file>>", guiI18N.get("file"));

        //TODO testGet and getGet with format
        //TODO implement get and get with format using parent
    }

}
