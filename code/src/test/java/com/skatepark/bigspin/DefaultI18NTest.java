package com.skatepark.bigspin;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;

/**
 * Test for {@link I18N}
 *
 * @author Marcelo Oikawa
 */
public class DefaultI18NTest {

    private static final String BASE_FILE = "base_en_US.properties";

    private static final String GUI_FILE = "gui_en_US.properties";

    @Test(expected = IllegalArgumentException.class)
    public void testResourceBundleNull() {
        try {
            new DefaultI18N((InputStream) null);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertiesNull() {
        try {
            new DefaultI18N((Reader) null);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetParent() {
        I18N guiI18N = createI18N();
        I18N baseI18N = guiI18N.getParent();

        Assert.assertNull(baseI18N.getParent());
        Assert.assertSame(baseI18N, guiI18N.getParent());
    }

    @Test
    public void testHas() {
        I18N guiI18N = createI18N();
        I18N baseI18N = guiI18N.getParent();

        Assert.assertTrue(baseI18N.has("ok"));
        Assert.assertTrue(baseI18N.has("no"));
        Assert.assertTrue(baseI18N.has("cancel"));

        Assert.assertFalse(baseI18N.has("no.message"));

        Assert.assertTrue(guiI18N.has("ok"));
        Assert.assertTrue(guiI18N.has("no"));
        Assert.assertTrue(guiI18N.has("cancel"));
        Assert.assertTrue(guiI18N.has("file"));
        Assert.assertTrue(guiI18N.has("tools"));
        Assert.assertTrue(guiI18N.has("rename"));

        Assert.assertFalse(guiI18N.has("no.message"));
    }

    @Test
    public void testSize() {
        I18N guiI18N = createI18N();
        I18N baseI18N = guiI18N.getParent();

        Assert.assertEquals(4, baseI18N.size());
        Assert.assertEquals(3, guiI18N.size());

        Assert.assertEquals(4, baseI18N.total());
        Assert.assertEquals(7, guiI18N.total());
    }

    @Test
    public void testTotal() {
        I18N guiI18N = createI18N();
        I18N baseI18N = guiI18N.getParent();

        Assert.assertEquals(4, baseI18N.total());
        Assert.assertEquals(7, guiI18N.total());
    }

    @Test
    public void testKeys() {
        I18N guiI18N = createI18N();
        I18N baseI18N = guiI18N.getParent();

        Set<String> baseKeys = baseI18N.keys();

        Assert.assertNotNull(baseKeys);
        Assert.assertTrue(baseKeys.contains("ok"));
        Assert.assertTrue(baseKeys.contains("no"));
        Assert.assertTrue(baseKeys.contains("cancel"));
        Assert.assertTrue(baseKeys.contains("rename"));
        Assert.assertFalse(baseKeys.contains("no.message"));

        Set<String> guiKeys = guiI18N.keys();

        Assert.assertNotNull(guiKeys);
        Assert.assertTrue(guiKeys.contains("file"));
        Assert.assertTrue(guiKeys.contains("tools"));
        Assert.assertFalse(guiKeys.contains("no.message"));
    }

    @Test
    public void testAllKeys() {
        I18N guiI18N = createI18N();
        I18N baseI18N = guiI18N.getParent();

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
        Assert.assertTrue(allKeys.contains("rename"));
        Assert.assertFalse(allKeys.contains("no.message"));
    }

    @Test
    public void testGet() {
        I18N guiI18N = createI18N();
        I18N baseI18N = guiI18N.getParent();

        Assert.assertEquals("Ok", baseI18N.get("ok"));
        Assert.assertEquals("No", baseI18N.get("no"));
        Assert.assertEquals("Cancel", baseI18N.get("cancel"));

        Assert.assertEquals("Yes", guiI18N.get("ok"));
        Assert.assertEquals("No", guiI18N.get("no"));
        Assert.assertEquals("Cancel", guiI18N.get("cancel"));
        Assert.assertEquals("File", guiI18N.get("file"));
        Assert.assertEquals("Tools", guiI18N.get("tools"));
    }

    @Test
    public void testGetWithArgs() {
        I18N guiI18N = createI18N();

        Assert.assertEquals("Rename {0}", guiI18N.get("rename"));
        Assert.assertEquals("Rename package", guiI18N.get("rename", "package"));
    }

    private I18N createI18N() {
        try {
            InputStream baseStream = getClass().getClassLoader().getResourceAsStream(BASE_FILE);
            InputStream guiStream = getClass().getClassLoader().getResourceAsStream(GUI_FILE);

            I18N baseI18N = new DefaultI18N(new InputStreamReader(baseStream, "UTF-8"));
            return new DefaultI18N(new InputStreamReader(guiStream, "UTF-8"), baseI18N);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
