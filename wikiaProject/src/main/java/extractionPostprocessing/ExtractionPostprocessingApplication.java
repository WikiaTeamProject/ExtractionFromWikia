package extractionPostprocessing;

import extractionPostprocessing.controller.*;
import extractionPostprocessing.controller.classmapper.ClassMapper_1;
import extractionPostprocessing.controller.propertymapper.PropertyMapper_2;
import extractionPostprocessing.controller.resourcemapper.ResourceMapper_4;

/**
 * This application will postprocess extracted wikia wikis.
 * Resource mappings are changed.
 */
public class ExtractionPostprocessingApplication {

    public static void main(String[] args) {
        
        // create one mapping file for all wikis and replace domain
        RedirectProcessor redirectProcessor = new RedirectProcessor();
        redirectProcessor.executeRedirectsForAllWikis();

        MappingExecutor mappingExecutor = new MappingExecutor(new ResourceMapper_4(), new PropertyMapper_2(), new ClassMapper_1());
        mappingExecutor.createMappingFilesForAllWikis();

    }

}
