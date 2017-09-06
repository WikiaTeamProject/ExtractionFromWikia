package applications.extractionPostprocessing.controller;

import applications.extractionPostprocessing.model.ResourceServiceResult;
import static org.junit.Assert.*;

import applications.extractionPostprocessing.util.DBpediaResourceServiceOnline;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class for {@link DBpediaResourceServiceOnline DBpediaResourceServiceOnline}.
 * This test requires internet and connects to DBpedia during the test.
 *
 * This test works as of September 2017.
 * If it fails, one reason can be that DBpedia received an update.
 *
 * Currently those test can be ignored because the SPARQL endpoint is not used.
 */
public class DBpediaResourceServiceOnlineTest {

    public static DBpediaResourceServiceOnline service = new DBpediaResourceServiceOnline();


    @Test
    // ignore test because SPARQL endpoint is not used currently
    public void getResourceAndRedirectInDBpedia() throws Exception {

        // try with redirect resource
        ResourceServiceResult result = service.getResourceAndRedirectInDBpedia("<http://dbpedia.org/resource/Hagrid>");
        assertTrue(result.resourceExists);
        //assertTrue(result.redirectResource.equals("<http://dbpedia.org/resource/Rubeus_Hagrid>")); // redirect not existant anymore with update of DBpedia

        // try with non-redirect resource
        result = service.getResourceAndRedirectInDBpedia("<http://dbpedia.org/resource/Rubeus_Hagrid>");
        assertTrue(result.resourceExists);
        assertNull(result.redirectResource);

        // non-existing resource
        result = service.getResourceAndRedirectInDBpedia("<http://dbpedia.org/resource/xasdfpasdfwnawe>");
        assertFalse(result.resourceExists);
        assertNull(result.redirectResource);

        // try with redirect resource (no tags)
        result = service.getResourceAndRedirectInDBpedia("http://dbpedia.org/resource/Hagrid");
        assertTrue(result.resourceExists);
        //assertTrue(result.redirectResource.equals("<http://dbpedia.org/resource/Rubeus_Hagrid>")); // redirect not existant anymore with update of DBpedia

        // try with non-redirect resource (no tags)
        result = service.getResourceAndRedirectInDBpedia("http://dbpedia.org/resource/Rubeus_Hagrid");
        assertTrue(result.resourceExists);
        assertNull(result.redirectResource);

        // non-existing resource (no tags)
        result = service.getResourceAndRedirectInDBpedia("http://dbpedia.org/resource/xasdfpasdfwnawe");
        assertFalse(result.resourceExists);
        assertNull(result.redirectResource);
    }


    @Test
    public void resourceExistsInDBpedia() throws Exception {
        // test with existing resource
        assertTrue(service.resourceExistsInDBpedia("<http://dbpedia.org/resource/Hagrid>"));

        // test with non-existing resource
        assertFalse(service.resourceExistsInDBpedia("<http://dbpedia.org/resource/aasdfaapowefl>"));
    }

    @Test
    public void trimTag() throws Exception {
        // test with real tag
        String result = service.trimTagIfTag("<http://dbpedia.org/resource/Game_of_Thrones_Wiki>");
        assertEquals(result, "http://dbpedia.org/resource/Game_of_Thrones_Wiki");

        // test with "false" tag
        result = service.trimTagIfTag("http://dbpedia.org/resource/Game_of_Thrones_Wiki");
        assertEquals(result, "http://dbpedia.org/resource/Game_of_Thrones_Wiki");
    }

}
