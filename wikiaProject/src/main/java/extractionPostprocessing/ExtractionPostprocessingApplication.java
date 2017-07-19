package extractionPostprocessing;

import extractionPostprocessing.controller.*;
import extractionPostprocessing.controller.classmapper.ClassMapper_4;
import extractionPostprocessing.controller.propertymapper.PropertyMapper_3;
import extractionPostprocessing.controller.resourcemapper.ResourceMapper_4_1;

/**
 * This application will postprocess extracted wikia wikis.
 * Resource mappings are changed.
 */
public class ExtractionPostprocessingApplication {

    public static void main(String[] args) {
        
        // create one mapping file for all wikis and replace domain
        RedirectProcessor redirectProcessor = new RedirectProcessor();
        redirectProcessor.executeRedirectsForAllWikis();

        MappingExecutor mappingExecutor = new MappingExecutor(new ResourceMapper_4_1(), new PropertyMapper_3(), new ClassMapper_4());
        mappingExecutor.createMappingFilesForAllWikis();

    }

}
