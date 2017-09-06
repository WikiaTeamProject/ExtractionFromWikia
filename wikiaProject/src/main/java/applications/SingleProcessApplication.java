package applications;

import applications.extraction.Extractor;
import applications.extractionPostprocessing.controller.MappingExecutor;
import applications.extractionPostprocessing.controller.RedirectProcessor;
import applications.extractionPostprocessing.controller.classmapper.ClassMapper_4;
import applications.extractionPostprocessing.controller.propertymapper.PropertyMapper_3;
import applications.extractionPostprocessing.controller.resourcemapper.ResourceMapper_4_1;
import applications.wikiaDumpDownload.controller.WikiaDumpDownloadThreadImpl;

import java.util.Arrays;
import java.util.List;

/**
 * This class combines all subapplications into one single process.
 */
public class SingleProcessApplication {

    /**
     * This main method runs all processes to retrieve mappings for all existing Wikia wikis.
     * - Retrieving an overview of all Wikia wikis (1) or only a list (2)
     * - Downloading existing wikis
     * - Extracting wikis with the DBpedia applications.extraction framework
     * - Creating one mapping file per wiki
     *
     * @param args
     */
    public static void main(String[] args) {

        // first check prerequisites for all processes
        // for (1): call checkPrerequisites(true)
        // for (2): call checkPrerequisites(false)
        if (!WikiaDumpDownloadThreadImpl.checkPrerequisites(false)) return;

        // (1) metadata will be saved and all existing wikia dumps are downloaded
//        WikiaDumpDownloadThreadImpl.downloadWikiaDumps();

        // (2) download only list of urls
        List<String> urls = Arrays.asList("http://gameofthrones.wikia.com", "http://bakerstreet.wikia.com");
        WikiaDumpDownloadThreadImpl.downloadWikiaDumps(urls);

        // run the DBpedia extraction framework
        Extractor extractor = new Extractor();
        extractor.extractAllWikis();

        // create mappings
        RedirectProcessor redirectProcessor = new RedirectProcessor();
        redirectProcessor.executeRedirectsForAllWikis();
        MappingExecutor mappingExecutor = new MappingExecutor(new ResourceMapper_4_1(), new PropertyMapper_3(), new ClassMapper_4());
        mappingExecutor.createMappingFilesForAllWikis();

    }

}
