package utils;

import loggingService.MessageLogger;
import org.apache.tools.ant.DirectoryScanner;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Level;

/**
 * This class includes general IO/File methods usable in all packages.
 */
public class IOoperations {


    private static MessageLogger logger=new MessageLogger();
    private static final String MODULE="Utils";
    private static final String CLASS=IOoperations.class.getName();
    private static String rootDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory");


    /**
     * This method receives an array of file paths and merges the files into the specified target file
     *
     * @param filePaths
     * @param targetFile
     */
    public static void mergeFiles(ArrayList<String> filePaths, String targetFile) {
        logger.logMessage(Level.FATAL,MODULE,CLASS,"!!! mergeFiles not implemented yet !!!");
        // TODO: implement and use!
    }


    /**
     * Returns the rootDirectoryPath as specified in config.properties.
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return rootDirectoryPath;
    }

    /**
     * Returns a File object to the rootDirectory as specified in config.properties.
     *
     * @return
     */
    public static File getRootDirectory() {
        return new File(rootDirectoryPath);
    }

    /**
     * Returns the target namespace as specified in config.properties.
     *
     * @return
     */
    public static String getTargetNamespace() {
        return ResourceBundle.getBundle("config").getString("targetnamespace");
    }

    /**
     * This method receives a file path and creates a directory out of it.
     * Mainly used because OSX systems do not automatically use a file as a directory.
     *
     * @param filePath
     */
    public static File createDirectory(String filePath) {
        File file = new File(filePath);
        if (!file.isDirectory()) {
            try {
                Files.createDirectory(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        return file;
    }


    /**
     * Returns an array list containing the statistics pages of all wikis.
     * Prerequisite: A wiki CSV file exists.
     *
     * @param filePath
     * @return
     */
    public static ArrayList<String> getUrls(String filePath) {
        LineNumberReader fileReader;
        String line;
        String[] tokens;
        ArrayList<String> urls = new ArrayList<String>();

        // create file with specified file path
        File file = new File(filePath);

        // read lines of file
        try {
            fileReader = new LineNumberReader(new FileReader(file));

            // ignore header line
            fileReader.readLine();

            while ((line = fileReader.readLine()) != null) {

                // split line into tokens
                tokens = line.split(";");

                // add url to urls list
                urls.add(tokens[1] + "wiki/Special:Statistics");
            }

            // close stream
            fileReader.close();

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.toString());
        } catch (IOException ioe) {
            System.out.println(ioe.toString());
        }

        return urls;
    }


    /**
     * This method returns the file name of a given path to a file.
     *
     * @param path Path to a file.
     * @return File Name
     */
    public static String getFileNameFromPath(String path) {

        String regularExpressionToGetFileNameForwardSlash = "(?<=\\/)[^\\/]*$"; // plain: (?<=\/)[^\/]*$
        String regularExpressionToGetFileNameBackwardSlash = "(?<=\\\\)[^\\\\]*$"; // plain (?<=\\)[^\\]*$
        String fileName = null;

        Pattern pattern = Pattern.compile(regularExpressionToGetFileNameForwardSlash);
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            fileName = matcher.group(0);
        } else {
            // -> no file name could be derived using forward slash
            pattern = Pattern.compile(regularExpressionToGetFileNameBackwardSlash);
            matcher = pattern.matcher(path);
            if (matcher.find()) {
                fileName = matcher.group(0);
            }
        }
        return fileName;
    }


    /**
     * This method returns the file name of a given path to a file.
     *
     * @param path Path to a file.
     * @return File Name
     */
    public static String getFileNameFromPath(File path) {
        System.out.println(path.getAbsolutePath());
        return getFileNameFromPath(path.getPath());
    }


    /**
     * Overwrite file with content.
     *
     * @param content Content to be written
     * @param file    File to be overwritten
     */
    public static void updateFile(String content, File file) {
        logger.logMessage(Level.INFO,MODULE,CLASS,"Updating file " + file.getName());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.logMessage(Level.FATAL,MODULE,CLASS,"Could not update file " + file.getName() + ".");
            logger.logMessage(Level.FATAL,MODULE,CLASS,e.toString());
            e.printStackTrace();
        }
    }

    public static void updateFile(String content, String filepath) {
        updateFile(content, new File(filepath));
    }


    /**
     * Write the mappings file to the disk.
     *
     * @param entitiesMapping       A HashSet of Strings, each representing one line to be written to the mapping file.
     * @param pathToFileToBeWritten
     */
    public static void writeMappingContentsToFile(HashMap<String, String> entitiesMapping, File pathToFileToBeWritten, String ontology, boolean includeNullMappings) {

        StringBuffer contentToWrite = new StringBuffer();
        Iterator iterator = entitiesMapping.entrySet().iterator();

        while (iterator.hasNext()) {
            HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>) iterator.next();

            // exclude null mappings if includeNullMappings is false
            if (!includeNullMappings && entry.getValue().equals("<null>"))
                continue;

            contentToWrite.append(entry.getKey() + " " + ontology + " " + entry.getValue() + " .\n");
        }

        try {
            // Initialize file Writer Objects
            PrintWriter fileWriter = new PrintWriter(pathToFileToBeWritten, "UTF-8");

            // Writing Process
            fileWriter.write(contentToWrite.toString());

            // Close file Writer
            fileWriter.close();

        } catch (Exception exception) {
            exception.printStackTrace();
            logger.logMessage(Level.FATAL,MODULE,CLASS, exception.getMessage());
        }
    }


    /**
     * This function reads DBpedia pageids. pageIds file
     * Make sure that the folder "pageids" exists in the root directory
     * and that there is at least one page IDs file in the folder.
     *
     * @return HashMap containing pageIds in lower case as key and actual
     * case is preserved as value
     */
    public HashMap<String, String> getPageIDs() {
        String pageIDsFilePath = rootDirectoryPath + "//resources//pageids//";
        HashMap<String, String> pageIdsMap = new HashMap<String, String>();
        FileReader fileReader;
        BufferedReader bufferedReader;
        String fileLine = "";
        int i = 1;
        String fileLinePattern = "<[^<]*> <[^<]*> \"[0-9]+\"[\\^][\\^]<[^<]*> [.]";

        try {

            File pageIdsDirectory = new File(pageIDsFilePath);
            if (!pageIdsDirectory.exists()) {
                logger.logMessage(Level.FATAL,MODULE,CLASS,"<root>/resources/pageids/ directory does not exist.");
            }

            if (pageIdsDirectory.isDirectory()) {

                for (File pageIdsFile : pageIdsDirectory.listFiles()) {
                    if (pageIdsFile.getName().toLowerCase().endsWith(".ttl")) {

                        fileReader = new FileReader(pageIdsFile);
                        bufferedReader = new BufferedReader(fileReader);

                        while ((fileLine = bufferedReader.readLine()) != null) {

                            fileLine = fileLine.trim();

                            if ((!fileLine.startsWith("#")) && fileLine.matches(fileLinePattern)) {


                                String pageId =
                                        fileLine.substring(0, fileLine.indexOf(">") + 1);

                                pageIdsMap.put(pageId.toLowerCase(), pageId);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.logMessage(Level.FATAL,MODULE,CLASS,ex.getMessage());
        }
        return pageIdsMap;
    }


    /**
     * This function reads the DBpedia redirects file.
     * Make sure that the folder "redirects" exists in the root directory and that there is at least one redirect file in the folder.
     *
     * @return HashMap containing redirects mapping
     */
    public HashMap<String, String> getResourcesRedirects() {

        String redirectFilePath = rootDirectoryPath + "//resources//redirects//";
        HashMap redirectsMap = new HashMap<String, String>();
        FileReader fileReader;
        BufferedReader bufferedReader;
        String fileLine = "";
        String fileLinePattern = "<[^<]*> <[^<]*> <[^<]*> [.]";
        int i = 1;
        try {

            File redirectsDirectory = new File(redirectFilePath);
            if (!redirectsDirectory.exists()) {
                logger.logMessage(Level.FATAL,MODULE,CLASS,"<root>/resources/redirects/ directory does not exist.");
            }

            if (redirectsDirectory.isDirectory()) {

                for (File redirectsFile : redirectsDirectory.listFiles()) {
                    if (redirectsFile.getName().toLowerCase().endsWith(".ttl")) {

                        fileReader = new FileReader(redirectsFile);
                        bufferedReader = new BufferedReader(fileReader);

                        while ((fileLine = bufferedReader.readLine()) != null) {

                            fileLine = fileLine.trim();

                            if ((!fileLine.startsWith("#")) && fileLine.matches(fileLinePattern)) {

                                String resourceLink =
                                        fileLine.substring(0, fileLine.indexOf(">") + 1).toLowerCase();

                                String redirectLink =
                                        fileLine.substring(fileLine.lastIndexOf("<"),
                                                fileLine.lastIndexOf(">") + 1);

                                redirectsMap.put(resourceLink.toLowerCase(), redirectLink);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.logMessage(Level.FATAL,MODULE,CLASS,ex.getMessage());
        }

        return redirectsMap;
    }


    /**
     * A very simple file writer.
     *
     * @param file    The file in which shall be written.
     * @param content The content that shall be written.
     */
    public static void writeContentToFile(File file, String content) {

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            logger.logMessage(Level.FATAL,MODULE,CLASS,ioe.toString());
        }

    }

    /**
     * This function reads DBpedia ontology classes from ontology file
     * Make sure that the folder "ontology" exists
     * in the root directory and that there is at least one ontology classes file in the folder.
     *
     * @return HashMap containing ontology class in lower as key and actual
     * case is preserved as value
     */
    public HashMap<String, String> getOntologyClasses() {
        String ontologyFilePath = rootDirectoryPath + "//resources//ontology//";
        HashMap<String, String> ontologiesMap = new HashMap<String, String>();
        FileReader fileReader;
        BufferedReader bufferedReader;
        String fileLine = "";
        String fileLinePattern = "<[^<]*> <[^<]*>.+";
        try {

            File ontologyDirectory = new File(ontologyFilePath);
            if (!ontologyDirectory.exists()) {
                logger.logMessage(Level.FATAL,MODULE,CLASS,"<root>/resources/ontology/ directory does not exist.");
            }

            if (ontologyDirectory.isDirectory()) {

                for (File ontologyFile : ontologyDirectory.listFiles()) {
                    if (ontologyFile.getName().toLowerCase().endsWith(".nt")) {

                        fileReader = new FileReader(ontologyFile);
                        bufferedReader = new BufferedReader(fileReader);

                        while ((fileLine = bufferedReader.readLine()) != null) {

                            fileLine = fileLine.trim();

                            if ((!fileLine.startsWith("#")) && fileLine.matches(fileLinePattern)) {
                                String ontologyClass =
                                        fileLine.substring(0, fileLine.indexOf(">") + 1);

                                ontologiesMap.put(ontologyClass, ontologyClass);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.logMessage(Level.FATAL,MODULE,CLASS,ex.getMessage());
        }

        return ontologiesMap;
    }


    /**
     * This function reads list of properties
     * from dbpedia file
     *
     * @return list of properties in HashMap the key contains property
     * in lower case and actual case is preserved as value
     */
    public HashMap<String, String> getPropertiesSet() {
        String propertiesFilePath = rootDirectoryPath + "//resources//properties//";
        HashMap<String, String> propertiesMap = new HashMap<String, String>();
        FileReader fileReader;
        BufferedReader bufferedReader;
        String fileLine = "";
        String fileLinePattern = "<[^<]*> <[^<]*>.+";
        try {

            File propertiesDirectory = new File(propertiesFilePath);
            if (!propertiesDirectory.exists()) {
                logger.logMessage(Level.FATAL,MODULE,CLASS,"<root>/resources/properties/ directory does not exist.");
            }

            if (propertiesDirectory.isDirectory()) {

                for (File propertiesFile : propertiesDirectory.listFiles()) {
                    if (propertiesFile.getName().toLowerCase().endsWith(".ttl")) {

                        fileReader = new FileReader(propertiesFile);
                        bufferedReader = new BufferedReader(fileReader);

                        while ((fileLine = bufferedReader.readLine()) != null) {

                            fileLine = fileLine.trim();

                            if ((!fileLine.startsWith("#")) && fileLine.matches(fileLinePattern)) {
                                String property =
                                        fileLine.substring(0, fileLine.indexOf(">") + 1);

                                propertiesMap.put(property.toLowerCase(), property);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.logMessage(Level.FATAL,MODULE,CLASS,ex.getMessage());
        }
        return propertiesMap;
    }


    /**
     * @param filePath : Path where file needs to be searched
     * @param fileName : name of file to search ; function also accepts
     *                 wild card queries
     * @return true if file exists else false
     */
    public boolean checkIfFileExists(String filePath, String fileName) {

        boolean ifFileExist = false;
        String[] selectedFiles;
        try {

            DirectoryScanner scanner = new DirectoryScanner();

            scanner.setIncludes(new String[]{fileName});
            scanner.setBasedir(filePath);
            scanner.setCaseSensitive(false);
            scanner.scan();

            selectedFiles = scanner.getIncludedFiles();

            if (selectedFiles.length > 0)
                ifFileExist = true;


        } catch (Exception ex) {
            logger.logMessage(Level.FATAL,MODULE,CLASS,ex.getMessage());
        }
        return ifFileExist;
    }


    /**
     * @return
     */
    public HashMap<String, String> readDumpsURL() {

        HashMap<String, String> dumpURLs = new HashMap<String, String>();
        //String dumpsURLDirectoryPath = rootDirectoryPath + "//statistics//wikiaOverviewIndividualDumpURLs";
        String dumpsURLDirectoryPath=rootDirectoryPath + "//statistics//wikiaOverviewDumpURLs.csv";
        try {
            File dumpsURLFile = new File(dumpsURLDirectoryPath);

            //if (dumpsURLFile.exists() && dumpsURLFile.isDirectory()) {
            if (dumpsURLFile.exists()) {
                //for (File f : dumpsURLFile.listFiles()) {
                    FileReader fileReader = new FileReader(dumpsURLFile);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String fileLine;
                    boolean isHeaderRow = true;

                    while ((fileLine = bufferedReader.readLine()) != null) {

                        if (isHeaderRow) {
                            isHeaderRow = false;
                            continue;
                        }

                        String[] dumpURLsMapping = fileLine.split(",");
                        String dumpFilePath = dumpURLsMapping[0].substring(0, dumpURLsMapping[0].lastIndexOf("."));
                        String dumpBaseURL = dumpURLsMapping[1];

                        dumpURLs.put(dumpFilePath, dumpBaseURL);
                    }

                //}

            } else {
                logger.logMessage(Level.FATAL,MODULE,CLASS,"Dumps URL file not present. Please check ...");
            }

        } catch (Exception ex) {
            logger.logMessage(Level.FATAL,MODULE,CLASS,ex.toString());
        }

        return dumpURLs;
    }


    /**
     * This methods generates extraction
     * extraction properties file
     * using template file
     */
    public void generateExtractionProperties(){
        try{

            String extractionPropertiesFilePath =
                    this.getClass().getClassLoader().getResource("extraction.template.properties").getPath();

           File templateFile=new File(extractionPropertiesFilePath);

           FileInputStream templateFileStream=new FileInputStream(templateFile);

           Properties extractionProperties=new Properties();

           extractionProperties.load(templateFileStream);

            templateFileStream.close();


            extractionProperties.setProperty("base-dir",rootDirectoryPath+"/dbPediaExtractionFormat/");


            FileOutputStream extractionPropertiesFile = new FileOutputStream(templateFile.getParentFile()+"//extraction.properties");

            System.out.println(extractionPropertiesFilePath);
            extractionProperties.store(extractionPropertiesFile, null);

            extractionPropertiesFile.close();
        }
        catch(Exception ex){
            logger.logMessage(Level.FATAL,MODULE,CLASS,ex.toString());
        }
    }
}
