package extractionPostprocessing;

import extractionPostprocessing.controller.EntitiesMapping;
import extractionPostprocessing.controller.MappingsEvaluation;
import utils.ExtractionBz2;

import java.io.File;
import java.util.ResourceBundle;


public class ExtractionPostprocessingApplication {

    public static void main(String[] args) {


        // Example on how to call the extractor for one file
        /**
        String testFile2 = "C:/Users/D060249/Desktop/TMP/GoT_Wikia_Dump/enwiki/20170331/enwiki-20170331-article-templates-nested.ttl.bz2";
        String newFile2 = "C:/Users/D060249/Desktop";
        ExtractionBz2.extract(testFile2, newFile2);
        **/

        // Example on how to call the extractor for multiple files
        /**
        String testFile3 = "C:/Users/D060249/Desktop/TMP/GoT_Wikia_Dump/enwiki/20170331/";
        String newFile3 = "C:/Users/D060249/Desktop/got_wiki";

        ExtractionBz2.extractExtractorResultFiles(testFile3, newFile3);
         **/

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
