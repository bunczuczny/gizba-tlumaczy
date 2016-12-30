package test.java.gizba.translator;

import junit.framework.TestCase;
import main.java.gizba.translator.Translator;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TranslatorTest extends TestCase {

    private final static String FETCH = "fetch";
    private final static String DOWNLAND_DOWNLAND = "download download";

    private final static Map<String, String> DK_EN = new HashMap<>();

    static {
        DK_EN.put("hent", FETCH);
        DK_EN.put("HENT", FETCH);
        DK_EN.put("Hent", FETCH);

        DK_EN.put("hent_hent", DOWNLAND_DOWNLAND);
        DK_EN.put("HENT_HENT", DOWNLAND_DOWNLAND);
        DK_EN.put("Hent_Hent", DOWNLAND_DOWNLAND);
        DK_EN.put("hent_Hent", DOWNLAND_DOWNLAND);
        DK_EN.put("Hent_hent", DOWNLAND_DOWNLAND);

        DK_EN.put("HentHent", DOWNLAND_DOWNLAND);
        DK_EN.put("hentHent", DOWNLAND_DOWNLAND);

        DK_EN.put("hentHent_hentHent", DOWNLAND_DOWNLAND + " " + DOWNLAND_DOWNLAND);
        DK_EN.put("hentHent hentHent", DOWNLAND_DOWNLAND + " " + DOWNLAND_DOWNLAND);
    }

    @Test
    public void testTranslate() {
        for (Map.Entry<String, String> stringStringEntry : DK_EN.entrySet()) {
            translateDkToEn(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
    }

    private void translateDkToEn(String before, String after) {
        Translator translator = new Translator();

        String result = translator.translate(before, "dk", "en");

        Assert.assertEquals(after, result.toLowerCase());
    }

}