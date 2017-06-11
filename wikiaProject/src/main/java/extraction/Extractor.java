package extraction;

import extraction.model.WikiaWikiProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.text.SimpleDateFormat;

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


    public Extractor() {

        // get the path to the dbpedia extraction framework
        String filepath = ResourceBundle.getBundle("config").getString("pathToExtractionFramework");
        extractionFrameworkDirectory = new File(filepath);
        extractionDefaultPropertiesFilePath = extractionFrameworkDirectory.getAbsolutePath()
                                              + "\\dump\\extraction.default.properties";

        // make sure prerequisites are fulfilled
        checkPrerequisites();

    }


    /**
     * Method creates the required file structure for the extraction.
     */
    private void createExtractionFileStructureForWiki(File wikiFile) {

//        TODO: think about commons wiki

    }

    /**
     * This method works just like {@link Extractor#createExtractionFileStructureForWiki(File)}
     * @see Extractor#createExtractionFileStructureForWiki(File)
     * @param pathToWiki
     */
    private void createExtractionFileStructureForWiki(String pathToWiki) {
        createExtractionFileStructureForWiki(new File(pathToWiki));
    }


    /**
     * Method will generate a properties file and save it within the dbpedia extraction framework
     */
    private WikiaWikiProperties extractPropertiesForaWiki(String wikiFilePath) {
        // TODO: Implement

        WikiaWikiProperties wikiProperties=null;
        // get the path to the dbpedia extraction framework


        try {
            String line = "";
            String fileContents = "";
            String languageCode = "";
            String wikiName="";
            String wikiPath=wikiFilePath;
            int lineNumber = 0;


            FileReader fr = new FileReader(wikiFilePath);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null && lineNumber <= 20) {

                fileContents += line;
                lineNumber++;
            }

            if(fileContents.length()>0) {

                languageCode = fileContents.substring(fileContents.indexOf("xml:lang=") + 10, fileContents.indexOf(">", fileContents.indexOf("xml:lang=") + 10) - 1);

                wikiName = (fileContents.substring(fileContents.indexOf("<sitename>") + 10, fileContents.indexOf("</sitename>", fileContents.indexOf("<sitename>") + 10) - 1)).trim().replace(" ", "_");

                wikiProperties=new WikiaWikiProperties(wikiName, languageCode,wikiPath);

            }
            br.close();
            fr.close();


        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

        return wikiProperties;

    }

    /**
     *
     *
     */
    public HashMap<String,WikiaWikiProperties> extractPropertiesForAllWikis() {

        //WikiaWikisProperties
        HashMap<String,WikiaWikiProperties> wikiProperties=new HashMap<String,WikiaWikiProperties>();


        String wikisFilePath= ResourceBundle.getBundle("config").getString("dumpsdirectory");

        try {
            File wikisFilesFolder = new File(wikisFilePath);

            //get list of wikis in a folder
            File[] listOfFolders = wikisFilesFolder.listFiles();

            for(File formatFolder:listOfFolders){

                if(formatFolder.isDirectory()) {

                    File[] listOfFiles=formatFolder.listFiles();

                    for (int i = 0; i < listOfFiles.length; i++) {

                        if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".xml")) {

                            WikiaWikiProperties properties = extractPropertiesForaWiki(listOfFiles[i].getPath());

                            if (wikiProperties != null)
                                wikiProperties.put(listOfFiles[i].getName(), properties);
                        }
                    }
                }

            }
        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

        return wikiProperties;
    }

    /**
     *
     */
    public void createDbpediaExtractionStructure(){

        try{

            HashMap<String,WikiaWikiProperties> wikisPropertiesSet=extractPropertiesForAllWikis();

            String downloadDirectoryForExtraction = ResourceBundle.getBundle("config").getString("downloadDirectoryforExtraction");

            String wikiSourceFileName= ResourceBundle.getBundle("config").getString("wikiSourceFileName");

            WikiaWikiProperties wikiProperties=null;
            String languageCode;
            String siteName;
            String wikiFilePath;
            File languageDirectory=null;
            File dateDirectory=null;
            File wikiFileToCopy=null;
            int index=0;
            String DATE_FORMAT_NOW = "YYYYMMdd";
            Calendar calender = Calendar.getInstance();
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT_NOW);
            String dateFolderName;




            File extractionDirectory=new File(downloadDirectoryForExtraction);

            if(!extractionDirectory.exists()){
                extractionDirectory.mkdir();
            }

            for(String wikiName:wikisPropertiesSet.keySet()){

                wikiProperties=wikisPropertiesSet.get(wikiName);

                //Get properties
                languageCode=wikiProperties.getLanguageCode();
                siteName=wikiProperties.getWikiName();
                wikiFilePath=wikiProperties.getWikiPath();
                calender.add(Calendar.DATE,-index);
                dateFolderName=dateFormatter.format(calender.getTime());

                languageDirectory=new File(downloadDirectoryForExtraction+"/"+languageCode+"wiki");

                if(!languageDirectory.exists()){
                    languageDirectory.mkdir();
                }

                dateDirectory=new File(downloadDirectoryForExtraction+"/"+languageCode+"wiki"+"/"+dateFolderName);

                if(!dateDirectory.exists()){
                    dateDirectory.mkdir();
                }


                copyFileFromOneDirectorytoAnotherDirectory(wikiFilePath,downloadDirectoryForExtraction+"/"+languageCode+"wiki"+"/"+dateFolderName+"/"+
                                            languageCode+"wiki-"+dateFolderName+"-"+
                                            wikiSourceFileName);

                index++;
            }

        }
        catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

    }


    /**
     * Copies one file from one directory to another directory.
     * @param sourceFilePath
     * @param targetFilePath
     */

    public void copyFileFromOneDirectorytoAnotherDirectory(String sourceFilePath, String targetFilePath){

        try{
            File sourceFile=new File(sourceFilePath);
            File targetFile=new File(targetFilePath);

            if(!targetFile.exists())
                targetFile.createNewFile();


            FileInputStream sourceFileInputStream = new FileInputStream(sourceFile);
            FileOutputStream targetFileOutputStream = new FileOutputStream(targetFile);

            byte[] buffer = new byte[1024];

            int length;

            //copy the file content in bytes
            while ((length = sourceFileInputStream.read(buffer)) > 0){

                targetFileOutputStream.write(buffer, 0, length);

            }

            sourceFileInputStream.close();
            targetFileOutputStream.close();

        }
        catch(Exception exception){
            logger.log(Level.SEVERE, exception.getMessage());
        }
    }



    public void callDbPediaExtractorToExtractFile(){
        try{
            String downloadDirectoryForExtraction =
                    ResourceBundle.getBundle("config").getString("downloadDirectoryforExtraction");


        }
        catch(Exception exception){
            logger.log(Level.SEVERE, exception.getMessage());
        }

    }


    /**
     * There are various prerequisites. To allow for a stable program, the prerequisites are checked in this method.
     * @return
     */
    private boolean checkPrerequisites(){

        // check if path to dbpedia extraction framework is valid
        if (extractionFrameworkDirectory.exists()) {

            // check whether specified path is a directory
            if (!extractionFrameworkDirectory.isDirectory()) {
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
