package extractionPostprocessing;

import extractionPostprocessing.controller.EntitiesMappingExecutor;
import extractionPostprocessing.controller.Mapper_3;
import extractionPostprocessing.controller.RedirectProcessor;
import utils.ExtractionBz2;

import java.io.File;
import java.util.ResourceBundle;

/**
 * This applicatrion will postprocess extracted wikia wikis.
 * Resource mappings are changed.
 */
public class ExtractionPostprocessingApplication {

    public static void main(String[] args) {


        String pathToRootDirectory = ResourceBundle.getBundle("config").getString("pathToRootDirectory");
        File root = new File(pathToRootDirectory);


        // evaluate whether this is really needed.
        if (root.isDirectory()) {
            for (File directory : root.listFiles()) {
                if (directory.isDirectory()) {

                    // unzip all bz2 files
                    // ExtractionBz2.extractDBpediaExtractorResultFiles(directory.getAbsolutePath(), directory.getAbsolutePath());
                }
            }
        }

        // create one mapping file out of all extracted files for each wiki
        RedirectProcessor rp = new RedirectProcessor(pathToRootDirectory + "/GoT_Wikia");
        rp.executeRedirects();

        EntitiesMappingExecutor mappingExecutor = new EntitiesMappingExecutor(new Mapper_3());
        mappingExecutor.createMappingFilesForAllWikis();

    }

}
