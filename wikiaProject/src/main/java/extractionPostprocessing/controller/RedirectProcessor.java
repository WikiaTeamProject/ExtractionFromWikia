package extractionPostprocessing.controller;

import java.io.File;
import java.util.ResourceBundle;

/**
 * This class allows to process redirect files from all wikis in the root directory.
 */
public class RedirectProcessor {

    /**
     * Process redirect files from all wikis in the root directory.
     */
    public void executeRedirectsForAllWikis(){

        // get root directory
        String pathToRootDirectory = ResourceBundle.getBundle("config").getString("pathToRootDirectory");
        File rootDirectory = new File(pathToRootDirectory);

        for(File f : rootDirectory.listFiles()){
            if(f.isDirectory()){
                RedirectProcessorSingleWiki processor = new RedirectProcessorSingleWiki(f);
                processor.executeRedirects();
            }
        }

    }

}
