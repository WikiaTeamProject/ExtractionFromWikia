package applications;

import applications.extraction.Extractor;
import applications.wikiaDumpDownload.controller.WikiaDumpDownloadThreadImpl;

/**
 * This class combines all subapplications into one single process.
 */
public class SingleProcessApplication {

    /**
     * This main method runs all processes to retrieve mappings for all existing Wikia wikis.
     *  - Retrieving an overview of all Wikia wikis
     *  - Downloading existing wikis
     *  - Extracting wikis with the DBpedia applications.extraction framework
     *  - Creating one mapping file per wiki
     *
     * @param args
     */
    public static void main(String[] args) {

        // first check prerequisites for all processes
        if (! checkPrerequisites()) return;

        // metadata will be saved and all existing wikia dumps are downloaded
        WikiaDumpDownloadThreadImpl.downloadWikiaDumps();

        // run the DBpedia applications.extraction framework
        Extractor extractor = new Extractor();
        extractor.extractAllWikis();

        // create mappings
        // RedirectProcessor redirectProcessor = new RedirectProcessor();
        // redirectProcessor.executeRedirectsForAllWikis();
        // MappingExecutor mappingExecutor = new MappingExecutor(new ResourceMapper_1(), new PropertyMapper_2(), new ClassMapper_1());
        // mappingExecutor.createMappingFilesForAllWikis();

    }

    private static boolean checkPrerequisites() {

        return WikiaDumpDownloadThreadImpl.checkPrerequisites() & Extractor.checkPrerequisites();
    }
}
