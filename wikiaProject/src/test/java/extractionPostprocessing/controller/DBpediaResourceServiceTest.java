package extractionPostprocessing.controller;

import extractionPostprocessing.model.SPARQLresult;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for {@link extractionPostprocessing.controller.DBpediaResourceService DBpediaResourceService}.
 */

public class DBpediaResourceServiceTest {

    @Test
    public void getResourceAndRedirectInDBpedia() throws Exception {

        // try with redirect resource
        SPARQLresult result = DBpediaResourceService.getResourceAndRedirectInDBpedia("<http://dbpedia.org/resource/Hagrid>");
        assertTrue(result.resourceExists);
        assertTrue(result.redirectResource.equals("<http://dbpedia.org/resource/Rubeus_Hagrid>"));

        // try with non-redirect resource
        result = DBpediaResourceService.getResourceAndRedirectInDBpedia("<http://dbpedia.org/resource/Rubeus_Hagrid>");
        assertTrue(result.resourceExists);
        assertNull(result.redirectResource);

        // non-existing resource
        result = DBpediaResourceService.getResourceAndRedirectInDBpedia("<http://dbpedia.org/resource/xasdfpasdfwnawe>");
        assertFalse(result.resourceExists);
        assertNull(result.redirectResource);

        // try with redirect resource (no tags)
        result = DBpediaResourceService.getResourceAndRedirectInDBpedia("http://dbpedia.org/resource/Hagrid");
        assertTrue(result.resourceExists);
        assertTrue(result.redirectResource.equals("<http://dbpedia.org/resource/Rubeus_Hagrid>"));

        // try with non-redirect resource (no tags)
        result = DBpediaResourceService.getResourceAndRedirectInDBpedia("http://dbpedia.org/resource/Rubeus_Hagrid");
        assertTrue(result.resourceExists);
        assertNull(result.redirectResource);

        // non-existing resource (no tags)
        result = DBpediaResourceService.getResourceAndRedirectInDBpedia("http://dbpedia.org/resource/xasdfpasdfwnawe");
        assertFalse(result.resourceExists);
        assertNull(result.redirectResource);
    }


    @Test
    public void resourceExistsInDBpedia() throws Exception {
        // test with existing resource
        assertTrue(DBpediaResourceService.resourceExistsInDBpedia("<http://dbpedia.org/resource/Hagrid>"));

        // test with non-existing resource
        assertFalse(DBpediaResourceService.resourceExistsInDBpedia("<http://dbpedia.org/resource/aasdfaapowefl>"));
    }

    @Test
    public void trimTag() throws Exception {
        // test with real tag
        String result = DBpediaResourceService.trimTagIfTag("<http://dbpedia.org/resource/Game_of_Thrones_Wiki>");
        assertEquals(result, "http://dbpedia.org/resource/Game_of_Thrones_Wiki");

        // test with "false" tag
        result = DBpediaResourceService.trimTagIfTag("http://dbpedia.org/resource/Game_of_Thrones_Wiki");
        assertEquals(result, "http://dbpedia.org/resource/Game_of_Thrones_Wiki");
    }

}
