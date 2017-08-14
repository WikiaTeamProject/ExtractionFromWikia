package applications.extractionPostprocessing.controller;

import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class allows to process redirect files from all wikis in the root directory.
 */
public class RedirectProcessor {

    private static Logger logger = Logger.getLogger(RedirectProcessor.class.getName());


    /**
     * Process redirect files from all wikis in the root directory.
     */
    public void executeRedirectsForAllWikis(){

        // get root directory
        String pathToRootDirectory = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/postProcessedWikis";
        File rootDirectory = new File(pathToRootDirectory);

        if(rootDirectory.isDirectory()) {

            for (File f : rootDirectory.listFiles()) {
                if (f.isDirectory()) {
                    RedirectProcessorSingleWiki processor = new RedirectProcessorSingleWiki(f);
                    processor.executeRedirects();
                }
            }
        } else {
            logger.severe("Root Directory is not a directory. Aborting process.");
        }
    }

}
