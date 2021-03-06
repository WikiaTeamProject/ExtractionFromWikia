package applications.extractionPostprocessing.controller;

import applications.extractionPostprocessing.model.ResourceServiceResult;
import static org.junit.Assert.*;

import applications.extractionPostprocessing.util.DBpediaResourceServiceOffline;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import testOrchestration.CheckPrerequisitesTest;

/**
 * Test class for {@link applications.extractionPostprocessing.util.DBpediaResourceServiceOffline DBpediaResourceServiceOnline}.
 * Make sure you allocate at least 8 gigabytes of RAM to the JVM.
 * Important: The test assumes that all DBpedia files (ontology, pageids, redirects) are those of 2016-10.
 */

@Ignore
public class DBpediaResourceServiceOfflineTest {

    public static DBpediaResourceServiceOffline service = DBpediaResourceServiceOffline.getDBpediaResourceServiceOfflineObject();

    @BeforeClass
    public static void checkPrerequisites(){
        // making sure that the required test files exist.
        new CheckPrerequisitesTest().checkPrerequisites();
    }


    @Test
    public void getResourceAndRedirectInDBpedia() throws Exception {

        // try with redirect resource
        ResourceServiceResult result = service.getResourceAndRedirectInDBpedia("<http://dbpedia.org/resource/Hagrid>");
        assertTrue(result.resourceExists);
        assertTrue(result.redirectResource.equals("<http://dbpedia.org/resource/Rubeus_Hagrid>"));

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
        assertTrue(result.redirectResource.equals("<http://dbpedia.org/resource/Rubeus_Hagrid>"));

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
        assertTrue(service.resourceExistsInDBpediaIgnoreCase("<http://dbpedia.org/resource/Hagrid>"));

        // test with non-existing resource
        assertFalse(service.resourceExistsInDBpediaIgnoreCase("<http://dbpedia.org/resource/aasdfaapowefl>"));
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
