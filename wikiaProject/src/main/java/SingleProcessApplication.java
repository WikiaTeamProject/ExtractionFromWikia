import extraction.Extractor;
import extractionPostprocessing.controller.MappingExecutor;
import extractionPostprocessing.controller.RedirectProcessor;
import extractionPostprocessing.controller.classmapper.ClassMapper_1;
import extractionPostprocessing.controller.propertymapper.PropertyMapper_2;
import extractionPostprocessing.controller.resourcemapper.ResourceMapper_1;
import wikiaDumpDownload.controller.WikiaDumpDownloadThreadImpl;
import wikiaStatistics.controller.MetadataThreadImpl;

/**
 * This class combines all subapplications into one single process.
 */
public class SingleProcessApplication {

    /**
     * This main method runs all processes to retrieve mappings for all existing Wikia wikis.
     *  - Retrieving an overview of all Wikia wikis
     *  - Downloading existing wikis
     *  - Extracting wikis with the DBpedia extraction framework
     *  - Creating one mapping file per wiki
     *
     *  Prerequisites: Please first maintain all attributes in the resources > config.properties file
     * @param args
     */
    public static void main(String[] args) {

        // metadata will be downloaded and files saved
        MetadataThreadImpl.downloadWikiaMetadata();

        // download all existing wiki dumps
        WikiaDumpDownloadThreadImpl.downloadWikiaDumps();

        // run the DBpedia extraction framework
        Extractor extractor = new Extractor();
        extractor.extractAllWikis();

        // create mappings
        RedirectProcessor redirectProcessor = new RedirectProcessor();
        redirectProcessor.executeRedirectsForAllWikis();
        MappingExecutor mappingExecutor = new MappingExecutor(new ResourceMapper_1(), new PropertyMapper_2(), new ClassMapper_1());
        mappingExecutor.createMappingFilesForAllWikis();

    }
}
