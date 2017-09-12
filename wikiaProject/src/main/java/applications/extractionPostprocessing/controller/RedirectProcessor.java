package applications.extractionPostprocessing.controller;

import loggingService.MessageLogger;
import org.apache.log4j.Priority;

import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class allows to process redirect files from all wikis in the root directory.
 */
public class RedirectProcessor {

    private static MessageLogger logger=new MessageLogger();
    private static final String MODULE="ExtractionPostprocessing";
    private static final String CLASS="RedirectProcessor";


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
            logger.logMessage(Priority.FATAL ,MODULE,CLASS,"Root Directory is not a directory. Aborting process.");
        }
    }

}
