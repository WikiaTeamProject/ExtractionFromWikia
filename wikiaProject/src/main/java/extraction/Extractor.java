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

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import utils.ExtractionBz2;
import utils.ExtractionGZip;
import utils.Extraction7zip;
import utils.IOoperations;

import java.util.Date;

import java.io.PrintWriter;


/**
 * This class will perform the extraction of the previously downloaded wikis.
 * <p>
 * Prerequisites
 * - DBpedia Extraction Framework is available on the machine.
 *
 * @see <a href="https://github.com/dbpedia/extraction-framework">https://github.com/dbpedia/extraction-framework</a>
 * - The DBpedia extraction framework was successfully built.
 * - The wikia wikis that shall be extracted were already downloaded and can be found in
 * /src/main/resources/files/wikiDumps.downloaded
 */
public class Extractor {

    private static Logger logger = Logger.getLogger(Extractor.class.getName());
    private File extractionFrameworkDirectory;
    private String extractionDefaultPropertiesFilePath;
    private HashMap<String, WikiaWikiProperties> wikisPropertiesSet;
    private String pathToRootDirectory =
            ResourceBundle.getBundle("config").getString("pathToRootDirectory");


    public Extractor() {

        // get the path to the dbpedia extraction framework
        String filepath = ResourceBundle.getBundle("config").getString("pathToExtractionFramework");
        extractionFrameworkDirectory = new File(filepath);
        extractionDefaultPropertiesFilePath = extractionFrameworkDirectory.getAbsolutePath()
                + "\\dump\\extraction.default.properties";

        // make sure prerequisites are fulfilled
        checkPrerequisites();

    }

    public void extractAllWikis() {

        logger.info("Unarachiving all dumps");
        unarchiveDownloadedDumps();

        logger.info("Creating folder structure for DBpedia extractor");
        createDbpediaExtractionStructure();

        logger.info("Calling dbPediaExtractor");
        callDbPediaExtractorToExtractFile();

        logger.info("Moving files for evaluation");
        moveExtractFilesforEvaluation();
    }


    /**
     * Method creates the required file structure for the extraction.
     */
    private void createExtractionFileStructureForWiki(File wikiFile) {

//        TODO: think about commons wiki

    }

    /**
     * This method works just like {@link Extractor#createExtractionFileStructureForWiki(File)}
     *
     * @param pathToWiki
     * @see Extractor#createExtractionFileStructureForWiki(File)
     */
    private void createExtractionFileStructureForWiki(String pathToWiki) {
        createExtractionFileStructureForWiki(new File(pathToWiki));
    }


    /**
     * This methods unarchives all the downloaded dumps
     */
    public void unarchiveDownloadedDumps() {
        try {
            String wikisFilePath =
                    ResourceBundle.getBundle("config").getString("pathToRootDirectory")
                            + "//downloadedWikis//";
            File downloadedWikisFolder = new File(wikisFilePath);
            Extraction7zip extractor7Zip = new Extraction7zip();
            ExtractionGZip extractorGZip = new ExtractionGZip();

            if (downloadedWikisFolder.isDirectory()) {
                File[] downloadedWikisFormats = downloadedWikisFolder.listFiles();
                for (File downloadedWikisFormat : downloadedWikisFormats) {
                    if (downloadedWikisFormat.isDirectory()) {
                        if (downloadedWikisFormat.getName().endsWith("7z")) {
                            extractor7Zip.extractAll7ZipFilesIntoDesignatedFolder();
                        } else if (downloadedWikisFormat.getName().endsWith("gz")) {
                            extractorGZip.extractAllGZipFilesIntoDesignatedFolder();
                        }

                        /*File[] downloadedWikis= downloadedWikisFormat.listFiles();
                        for(File wikis:downloadedWikis){
                            if(wikis.getName().endsWith(".gz")){

                                extractionGZip.extractGzipFile(wikis.getAbsolutePath(),
                                        wikis.getAbsolutePath());

                            }
                            else
                            if(wikis.getName().endsWith(".7z")){
                                extarctor7Zip.extract7ZipFile(wikis.getAbsolutePath(),
                                        wikis.getParent());
                            }
                        }*/
                    }
                }
            }


        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    /**
     * Method will generate a properties file and save it within the dbpedia extraction framework
     */
    private WikiaWikiProperties extractPropertiesForaWiki(String wikiFilePath) {
        WikiaWikiProperties wikiProperties = null;
        // get the path to the dbpedia extraction framework

        try {
            String line = "";
            String fileContents = "";
            String languageCode = "";
            String wikiName = "";
            String wikiPath = wikiFilePath;
            int lineNumber = 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date lastModifiedDate;
            long wikiSize;


            File wikiFile = new File(wikiFilePath);

            logger.info("Getting Properties for wiki : " + wikiFilePath);

            lastModifiedDate = simpleDateFormat.parse(simpleDateFormat.format(wikiFile.lastModified()));
            System.out.println("Total Size:" + (wikiFile.length() / 1024) + " KB");

            FileReader fr = new FileReader(wikiFile);

            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null && lineNumber <= 20) {

                fileContents += line;
                lineNumber++;
            }

            if (fileContents.length() > 0) {

                languageCode = fileContents.substring(fileContents.indexOf("xml:lang=") + 10, fileContents.indexOf(">", fileContents.indexOf("xml:lang=") + 10) - 1);


                if((fileContents.indexOf("<sitename>") + 10) <=
                        fileContents.indexOf("</sitename>", fileContents.indexOf("<sitename>") + 10) - 1)
                {

                    wikiName = (fileContents.substring(fileContents.indexOf("<sitename>") + 10, fileContents.indexOf("</sitename>", fileContents.indexOf("<sitename>") + 10) - 1)).trim().replace(" ", "_");
                }
                else
                    wikiName="";

                wikiSize = (wikiFile.length() / 1024);

                wikiProperties = new WikiaWikiProperties(wikiName, languageCode, wikiPath, lastModifiedDate, wikiSize);

            }
            br.close();
            fr.close();


        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

        return wikiProperties;

    }

    /**
     * This function reads downloaded wikis
     *
     * @return Hashmap contains key as wiki name and value as obejct containing
     * wiki properties
     */
    public HashMap<String, WikiaWikiProperties> extractPropertiesForAllWikis() {

        //WikiaWikisProperties
        HashMap<String, WikiaWikiProperties> wikiProperties =
                new HashMap<String, WikiaWikiProperties>();
        int index = 0;

        String wikisFilePath =
                ResourceBundle.getBundle("config").getString("pathToRootDirectory")
                        + "//downloadedWikis//decompressed//";

        try {
            File wikisFilesFolder = new File(wikisFilePath);

            //get list of wikis in a folder
            File[] wikiFiles = wikisFilesFolder.listFiles();

            System.out.println("Total Files :" + wikiFiles.length);

            for (File wikiFile : wikiFiles) {

                if (wikiFile.isFile() && wikiFile.getName().endsWith(".xml")) {

                    WikiaWikiProperties properties = extractPropertiesForaWiki(wikiFile.getPath());

                    if (wikiProperties != null) {
                        //wikiProperties.put(properties.getWikiName(), properties);
                        wikiProperties.put(properties.getWikiPath().substring(properties.getWikiPath().lastIndexOf("\\", properties.getWikiPath().length())), properties);
                    }

                } else {
                    logger.severe("File is not valid XML : " + wikiFile.getName());
                    break;
                }
            }

            /*
            for(File formatFolder:listOfFolders){

                if(formatFolder.isDirectory()) {

                    File[] listOfFiles=formatFolder.listFiles();

                    for (int i = 0; i < listOfFiles.length; i++) {

                        if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".xml")) {

                            WikiaWikiProperties properties = extractPropertiesForaWiki(listOfFiles[i].getPath());

                            if (wikiProperties != null) {
                                wikiProperties.put(properties.getWikiName(), properties);
                            }
                        }
                    }
                }

            }*/
        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

        return wikiProperties;
    }

    /**
     * Create structute expected by DBpedia extractor for extraction
     */
    public void createDbpediaExtractionStructure() {

        try {

            wikisPropertiesSet = extractPropertiesForAllWikis();

            String downloadDirectoryForExtraction =
                    ResourceBundle.getBundle("config").getString("pathToRootDirectory")
                            +"//dbPediaExtractionFormat//";

            String wikiSourceFileName =
                    ResourceBundle.getBundle("config").getString("wikiSourceFileName");

            WikiaWikiProperties wikiProperties = null;
            String languageCode;
            String siteName;
            String wikiFilePath;
            File languageDirectory = null;
            File dateDirectory = null;
            File wikiFileToCopy = null;
            int index = 1;
            String DATE_FORMAT_NOW = "YYYYMMdd";
            Calendar calender = Calendar.getInstance();
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT_NOW);
            String currentDate = dateFormatter.format(calender.getTime());


            File extractionDirectory = new File(downloadDirectoryForExtraction);

            if (!extractionDirectory.exists()) {
                extractionDirectory.mkdir();
            }

            for (String wikiName : wikisPropertiesSet.keySet()) {

                wikiProperties = wikisPropertiesSet.get(wikiName);

                //Get properties
                languageCode = wikiProperties.getLanguageCode();
                siteName = wikiProperties.getWikiName();
                wikiFilePath = wikiProperties.getWikiPath();

                //dateFolderName=wikiName.substring(0,wikiName.indexOf("_"));

                //dateFolderName=Integer.toString(index);

                languageDirectory = new File(downloadDirectoryForExtraction + "/" + languageCode + "wiki_");

                if (!languageDirectory.exists()) {
                    languageDirectory.mkdir();
                }

                dateDirectory = new File(downloadDirectoryForExtraction + "/" + languageCode + "wiki_" + "/" + index);

                if (!dateDirectory.exists()) {
                    dateDirectory.mkdir();
                }


                copyFileFromOneDirectorytoAnotherDirectory(wikiFilePath, downloadDirectoryForExtraction + "/" + languageCode + "wiki_" + "/" + index + "/" +
                        languageCode + "wiki-" + currentDate + "-" +
                        wikiSourceFileName);

                createWikiPropertiesFile(downloadDirectoryForExtraction + "/" + languageCode + "wiki_" + "/" + index + "/", wikiProperties);

                index++;
            }

        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

    }


    /**
     * Copies one file from one directory to another directory
     *
     * @param sourceFilePath
     * @param targetFilePath
     */

    public void copyFileFromOneDirectorytoAnotherDirectory(String sourceFilePath,
                                                           String targetFilePath) {

        try {
            File sourceFile = new File(sourceFilePath);
            File targetFile = new File(targetFilePath);

            if (!targetFile.exists())
                targetFile.createNewFile();


            FileInputStream sourceFileInputStream = new FileInputStream(sourceFile);
            FileOutputStream targetFileOutputStream = new FileOutputStream(targetFile);

            byte[] buffer = new byte[1024];

            int length;

            //copy the file content in bytes
            while ((length = sourceFileInputStream.read(buffer)) > 0) {

                targetFileOutputStream.write(buffer, 0, length);

            }

            sourceFileInputStream.close();
            targetFileOutputStream.close();

        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }
    }


    /**
     * This method will call dbpedia extarctor to extract all download wikis
     */
    public void callDbPediaExtractorToExtractFile() {
        try {
            String downloadDirectoryForExtraction =
                    ResourceBundle.getBundle("config").getString("pathToRootDirectory")
                             + "//dbPediaExtractionFormat//";
            String pathToExtractionFramework =
                    ResourceBundle.getBundle("config").getString("dbPediaExtractorPath");
            String DATE_FORMAT_NOW = "YYYYMMdd";
            Calendar calender = Calendar.getInstance();
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT_NOW);
            String current_date = dateFormatter.format(calender.getTime());
            String date="";
            String line = "dbpediaextraction.bat " + pathToExtractionFramework;
            CommandLine cmdLine = null;
            DefaultExecutor executor = null;
            IOoperations iOoperations=new IOoperations();


            File downloadedWikisDirectory = new File(downloadDirectoryForExtraction);

            File[] languageCodesFolders = downloadedWikisDirectory.listFiles();

            for (File languageCodeFolder : languageCodesFolders) {
                if (languageCodeFolder.isDirectory() &&
                        !languageCodeFolder.getName().toLowerCase().equals("commonswiki")) {

                    File renamedLanguageCodeFolder=new File(languageCodeFolder.getAbsolutePath().substring(0,languageCodeFolder.getAbsolutePath().length()-1));

                    languageCodeFolder.renameTo(renamedLanguageCodeFolder);

                    File[] dateFolders = renamedLanguageCodeFolder.listFiles();
                    for (File wikiDirectory : dateFolders) {

                        if(!iOoperations.checkIfFileExist(wikiDirectory.getAbsolutePath(),"*-complete")){

                        File[] wikiFiles = wikiDirectory.listFiles();

                        String folderName = wikiDirectory.getAbsolutePath();



                       // File[] filesToExtract = renamedFolder.listFiles();

                        for (File fileForExtraction : wikiFiles) {
                            if (fileForExtraction.getName().endsWith(".xml")) {

                                date=fileForExtraction.getName().substring (fileForExtraction.getName().indexOf("-")+1,
                                        fileForExtraction.getName().indexOf("-",fileForExtraction.getName().indexOf("-")+1));

                                File commonsWikiDirectory = new
                                        File(downloadedWikisDirectory.getAbsoluteFile()
                                        + "//commonswiki//" + date + "//");

                                if (commonsWikiDirectory.exists()) {
                                    commonsWikiDirectory.delete();
                                }

                                commonsWikiDirectory.mkdirs();

                                copyFileFromOneDirectorytoAnotherDirectory(
                                        fileForExtraction.getAbsolutePath(),
                                        commonsWikiDirectory.getAbsolutePath()
                                                + "//commonswiki-" + date + "-pages-current.xml");


                            }
                        }

                            File renamedFolder = new
                                    File(wikiDirectory.getParent() + "/" + date);

                            wikiDirectory.renameTo(renamedFolder);

                        try {
                            //call dbpedia extractor
                            cmdLine = CommandLine.parse(line);
                            executor = new DefaultExecutor();
                            executor.setExitValue(0);
                            int exitValue = executor.execute(cmdLine);
                        }
                        catch(Exception ex){
                            logger.severe("DBpedia extraction failed for this wiki .... !!");
                        }

                        //rename folder to orignal name
                        renamedFolder.renameTo(new File(folderName));
                        }
                    }

                    renamedLanguageCodeFolder.renameTo(languageCodeFolder);
                }
            }

        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

    }


    /**
     * There are various prerequisites. To allow for a stable program, the prerequisites are checked in this method.
     *
     * @return
     */
    private boolean checkPrerequisites() {

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

    /**
     * This function extracts compressed files obtained from DBpedia extractor
     * and moves to seperate directory for evaluation
     */
    public void moveExtractFilesforEvaluation() {

        String downloadDirectoryForExtraction =
                pathToRootDirectory + "//dbPediaExtractionFormat";

        String postProcessedFilesDirectoryPath =
                pathToRootDirectory +  "//postProcessedWikis";

        ExtractionBz2
                bz2Extractor = new ExtractionBz2();

        try {

            File extractedWikiFolder = new File(downloadDirectoryForExtraction);


            //get list of wikis in a folder
            File[] listOfFolders = extractedWikiFolder.listFiles();

            for (File languageCodeFolder : listOfFolders) {
                if (languageCodeFolder.isDirectory() &&
                        !languageCodeFolder.getName().toLowerCase().equals("commonswiki")) {

                    File[] dateFolders = languageCodeFolder.listFiles();

                    for (File dateFolder : dateFolders) {

                        if (dateFolder.isDirectory()) {

                            WikiaWikiProperties properties = readWikiPropertiesFile(dateFolder.getAbsolutePath());

                            File extractedFilesFolder = new File(postProcessedFilesDirectoryPath + "//" + properties.getWikiName());

                            if (!extractedFilesFolder.exists()) {
                                extractedFilesFolder.mkdirs();
                            }

                            File[] extractedFiles = dateFolder.listFiles();

                            for (File wikiFile : extractedFiles) {
                                if (wikiFile.getName().endsWith(".bz2")) {
                                    bz2Extractor.extract(wikiFile.getAbsolutePath(),
                                            postProcessedFilesDirectoryPath + "//" + properties.getWikiName());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }


    /**
     * This function create proerties file for wiki so that it can be used
     * to create folders with proper names for evaluation
     *
     * @param wikiFolderPath - folder path for respective wiki
     * @param wikiProperties - properties of respective wiki like wiki name , language
     */
    public void createWikiPropertiesFile(String wikiFolderPath,
                                         WikiaWikiProperties wikiProperties) {

        String downloadDirectoryForExtraction =
                ResourceBundle.getBundle("config").getString("wikiPropertiesFileName");
        String newLineCharacter =
                ResourceBundle.getBundle("config").getString("newLineCharacter");
        try {
            PrintWriter fileWriter = new PrintWriter(wikiFolderPath + "//" + downloadDirectoryForExtraction);

            System.out.println("Writing properties for wiki: " + wikiProperties.getWikiName());

            fileWriter.write("WikiName:" + wikiProperties.getWikiName() + newLineCharacter);
            fileWriter.write("LanguageCode:" + wikiProperties.getLanguageCode() + newLineCharacter);
            fileWriter.write("File Path:" + wikiProperties.getWikiPath() + newLineCharacter);

            fileWriter.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.severe(ex.getMessage());
        }
    }


    /**
     * This function read properties of wiki
     *
     * @param wikiFolderPath - path of directory where wiki is present
     * @return properties of respective wiki (wiki name, language)
     */
    public WikiaWikiProperties readWikiPropertiesFile(String wikiFolderPath) {

        String wikiPropertiesFileName =
                ResourceBundle.getBundle("config").getString("wikiPropertiesFileName");
        WikiaWikiProperties wikiProperties = new WikiaWikiProperties();

        try {


            String fileLine = "";
            FileReader fileReader =
                    new FileReader(wikiFolderPath + "//" + wikiPropertiesFileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);


            while ((fileLine = bufferedReader.readLine()) != null) {

                String key = fileLine.substring(0, fileLine.indexOf(":"));
                String value = fileLine.substring(fileLine.indexOf(":") + 1, fileLine.length());

                if (key.toLowerCase().equals("wikiname")) {

                    value = value.replace(":", "");
                    value = value.replace("@", "");
                    value = value.replace("*", "");
                    value = value.replace("/", "");
                    value = value.replace("\\", "");
                    value = value.replace("?", "");
                    value = value.replace("|", "");
                    value = value.replace("\"\"", "");
                    wikiProperties.setWikiName(value);
                } else if (key.toLowerCase().equals("languagecode")) {
                    wikiProperties.setLanguageCode(value);
                }
            }


            bufferedReader.close();
            fileReader.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.severe(ex.getMessage());
        }
        return wikiProperties;
    }
}
