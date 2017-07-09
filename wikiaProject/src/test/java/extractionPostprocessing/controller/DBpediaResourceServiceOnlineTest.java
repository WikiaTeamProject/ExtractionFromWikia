package extractionPostprocessing.controller;

import extractionPostprocessing.model.SPARQLresult;
import static org.junit.Assert.*;

import extractionPostprocessing.util.DBpediaResourceServiceOnline;
import org.junit.Test;

/**
 * Test class for {@link DBpediaResourceServiceOnline DBpediaResourceServiceOnline}.
 */

public class DBpediaResourceServiceOnlineTest {

    @Test
    public void getResourceAndRedirectInDBpedia() throws Exception {

        // try with redirect resource
        SPARQLresult result = DBpediaResourceServiceOnline.getResourceAndRedirectInDBpedia("<http://dbpedia.org/resource/Hagrid>");
        assertTrue(result.resourceExists);
        assertTrue(result.redirectResource.equals("<http://dbpedia.org/resource/Rubeus_Hagrid>"));

        // try with non-redirect resource
        result = DBpediaResourceServiceOnline.getResourceAndRedirectInDBpedia("<http://dbpedia.org/resource/Rubeus_Hagrid>");
        assertTrue(result.resourceExists);
        assertNull(result.redirectResource);

        // non-existing resource
        result = DBpediaResourceServiceOnline.getResourceAndRedirectInDBpedia("<http://dbpedia.org/resource/xasdfpasdfwnawe>");
        assertFalse(result.resourceExists);
        assertNull(result.redirectResource);

        // try with redirect resource (no tags)
        result = DBpediaResourceServiceOnline.getResourceAndRedirectInDBpedia("http://dbpedia.org/resource/Hagrid");
        assertTrue(result.resourceExists);
        assertTrue(result.redirectResource.equals("<http://dbpedia.org/resource/Rubeus_Hagrid>"));

        // try with non-redirect resource (no tags)
        result = DBpediaResourceServiceOnline.getResourceAndRedirectInDBpedia("http://dbpedia.org/resource/Rubeus_Hagrid");
        assertTrue(result.resourceExists);
        assertNull(result.redirectResource);

        // non-existing resource (no tags)
        result = DBpediaResourceServiceOnline.getResourceAndRedirectInDBpedia("http://dbpedia.org/resource/xasdfpasdfwnawe");
        assertFalse(result.resourceExists);
        assertNull(result.redirectResource);
    }


    @Test
    public void resourceExistsInDBpedia() throws Exception {
        // test with existing resource
        assertTrue(DBpediaResourceServiceOnline.resourceExistsInDBpedia("<http://dbpedia.org/resource/Hagrid>"));

        // test with non-existing resource
        assertFalse(DBpediaResourceServiceOnline.resourceExistsInDBpedia("<http://dbpedia.org/resource/aasdfaapowefl>"));
    }

    @Test
    public void trimTag() throws Exception {
        // test with real tag
        String result = DBpediaResourceServiceOnline.trimTagIfTag("<http://dbpedia.org/resource/Game_of_Thrones_Wiki>");
        assertEquals(result, "http://dbpedia.org/resource/Game_of_Thrones_Wiki");

        // test with "false" tag
        result = DBpediaResourceServiceOnline.trimTagIfTag("http://dbpedia.org/resource/Game_of_Thrones_Wiki");
        assertEquals(result, "http://dbpedia.org/resource/Game_of_Thrones_Wiki");
    }

}
