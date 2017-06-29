package extractionPostprocessing;

import extractionPostprocessing.controller.*;

/**
 * This applicatrion will postprocess extracted wikia wikis.
 * Resource mappings are changed.
 */
public class ExtractionPostprocessingApplication {

    public static void main(String[] args) {
        
        // create one mapping file for all wikis and replace domain
        RedirectProcessor redirectProcessor = new RedirectProcessor();
        redirectProcessor.executeRedirectsForAllWikis();

        EntitiesMappingExecutor mappingExecutor = new EntitiesMappingExecutor(new ResourceMapper_4());
        mappingExecutor.createMappingFilesForAllWikis();

    }

}
