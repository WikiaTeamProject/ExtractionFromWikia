package extraction;

import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 *
 * This class will perform the extraction of the previously downloaded wikis.
 *
 * Prerequisites
 * - DBpedia Extraction Framework is available on the machine.
 *   @see <a href="https://github.com/dbpedia/extraction-framework">https://github.com/dbpedia/extraction-framework</a>
 * - The DBpedia extraction framework was successfully built.
 * - The wikia wikis that shall be extracted were already downloaded and can be found in
 *   /src/main/resources/files/wikiDumps.downloaded
 *
 */
public class Extractor {

    private static Logger logger = Logger.getLogger(Extractor.class.getName());
    private File extractionFrameworkDirectory;
    private String extractionDefaultPropertiesFilePath;


    public Extractor(){

        // get the path to the dbpedia extraction framework
        String filepath = ResourceBundle.getBundle("config").getString("pathToExtractionFramework");
        extractionFrameworkDirectory = new File(filepath);
        extractionDefaultPropertiesFilePath = extractionFrameworkDirectory.getAbsolutePath()
                                              + "\\dump\\extraction.default.properties";

        // make sure prerequisites are fulfilled
        checkPrerequisites();

    }


    /**
     * Method creates the required file strucutre for the extraction.
     */
    private void createExtractionFileStructureForWiki(File wikiFile){

    }

    /**
     * This method works just like {@link Extractor#createExtractionFileStructureForWiki(File)}
     * @see Extractor#createExtractionFileStructureForWiki(File)
     * @param pathToWiki
     */
    private void createExtractionFileStructureForWiki(String pathToWiki){
        createExtractionFileStructureForWiki(new File(pathToWiki));
    }


    /**
     * Method will generate a properties file and save it within the dbpedia extraction framework
     */
    private void generateAndSavePropertiesFile(String baseDirectory, String source){
        // TODO: Implement
    }



    /**
     * There are various prerequisites. To allow for a stable program, the prerequisites are checked in this method.
     * @return
     */
    private boolean checkPrerequisites(){

        // check if path to dbpedia extraction framework is valid
        if(extractionFrameworkDirectory.exists()){

            // check whether specified path is a directory
            if(!extractionFrameworkDirectory.isDirectory()){
                logger.severe("Filepath to dbpedia extraction framework is not a directory!" +
                                    "Link to the root directory!");
                return false;
            }

        } else {
            logger.severe("Filepath to dbpedia extraction framework does not exist!");
            return false;
        }

        // TODO (optional): Check if some wikis were downloaded

        return true;
    }

}
