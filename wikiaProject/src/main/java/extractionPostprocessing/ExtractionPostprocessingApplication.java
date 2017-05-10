package extractionPostprocessing;

import extractionPostprocessing.controller.EntitiesMapping;
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

        if (root.isDirectory()) {
            for (File directory : root.listFiles()) {
                if (directory.isDirectory()) {

                    // unzip all bz2 files
                    ExtractionBz2.extractExtractorResultFiles(directory.getAbsolutePath(), directory.getAbsolutePath());
                }
            }
        }

        // create one mapping file out of all extracted files for each wiki
        EntitiesMapping.extractAllWikiaDbpediaEntitiesMapping();


    }


}
