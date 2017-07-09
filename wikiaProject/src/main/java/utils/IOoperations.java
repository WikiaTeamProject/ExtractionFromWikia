package utils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet;

/**
 *
 * This class includes general IO/File methods usable in all packages.
 */
public class IOoperations {


    static Logger logger = Logger.getLogger(IOoperations.class.getName());
    private static String rootDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory");
    /**
     * This method receives an array of file paths and merges the files into the specified target file
     *
     * @param filePaths
     * @param targetFile
     */
    public static void mergeFiles(ArrayList<String> filePaths, String targetFile) {
        // TODO: implement and use!
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
     * @param path Path to a file.
     * @return File Name
     */
    public static String getFileNameFromPath(String path){

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
     * @param path Path to a file.
     * @return File Name
     */
    public static String getFileNameFromPath(File path){
        System.out.println(path.getAbsolutePath());
        return getFileNameFromPath(path.getPath());
    }


    /**
     * Overwrite file with content.
     * @param content Content to be written
     * @param file File to be overwritten
     */
    public static void updateFile(String content, File file){
        logger.info("Updating file " + file.getName());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.severe("Could not update file " + file.getName() + ".");
            logger.severe(e.toString());
            e.printStackTrace();
        }
    }

    public static void updateFile(String content, String filepath){
        updateFile(content, new File(filepath));
    }




    /**
     * Write the mapings file to the disk.
     * @param entitiesMapping A HashSet of Strings, each representing one line to be written to the mapping file.
     * @param pathToFileToBeWritten
     */
    public static void writeMappingContentsToFile(HashMap<String, String> entitiesMapping, File pathToFileToBeWritten) {

        StringBuffer contentToWrite = new StringBuffer();
        Iterator iterator = entitiesMapping.entrySet().iterator();

        while(iterator.hasNext()){
            HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>) iterator.next();
            contentToWrite.append(entry.getKey() + " <owl:sameAs> " + entry.getValue() + " .\n");
        }


        try {
            // Initialize file Writer Objects
            PrintWriter fileWriter = new PrintWriter(pathToFileToBeWritten, "UTF-8");

            //
            fileWriter.write(contentToWrite.toString());

            // Close file Writer
            fileWriter.close();

        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }
    }


    /**
     * This function will read page Ids file
     * @return Hashset containing pageIds
     */
    public HashSet<String> getPageIDs(){
        String redirectFilePath = rootDirectoryPath + "//pageids//";
        HashSet<String> pageIdsSet = new HashSet<String>();
        FileReader fileReader;
        BufferedReader bufferedReader;
        String fileLine = "";
        int i = 1;
        try {

            File pageIdsDirectory = new File(redirectFilePath);
            if (!pageIdsDirectory.exists()) {
                logger.severe("Page IDs directory does not exist.");
            }

            if (pageIdsDirectory.isDirectory()) {

                for (File pageIdsFile : pageIdsDirectory.listFiles()) {
                    if (pageIdsFile.getName().toLowerCase().endsWith(".ttl")) {

                        fileReader = new FileReader(pageIdsFile);
                        bufferedReader = new BufferedReader(fileReader);

                        while ((fileLine = bufferedReader.readLine().trim()) != "-1") {
                            String pageId =
                                    fileLine.substring(0, fileLine.indexOf(">") + 1);

                            pageIdsSet.add(pageId);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }

        return pageIdsSet;
    }

    /**
     * This function with read redirects file
     * @return HashMap containing redirects mapping
     */
    public HashMap<String,String> getResourcesRedirects() {

        String redirectFilePath = rootDirectoryPath + "//redirects//";
        HashMap redirectsMap = new HashMap<String, String>();
        FileReader fileReader;
        BufferedReader bufferedReader;
        String fileLine = "";
        int i = 1;
        try {

            File redirectsDirectory = new File(redirectFilePath);
            if (!redirectsDirectory.exists()) {
                logger.severe("DBpediaResourceServiceOffline directory does not exist.");
            }

            if (redirectsDirectory.isDirectory()) {

                for (File redirectsFile : redirectsDirectory.listFiles()) {
                    if (redirectsFile.getName().toLowerCase().endsWith(".ttl")) {

                        fileReader = new FileReader(redirectsFile);
                        bufferedReader = new BufferedReader(fileReader);

                        while ((fileLine = bufferedReader.readLine().trim()) != "-1") {
                            String resourceLink =
                                    fileLine.substring(0, fileLine.indexOf(">") + 1);

                            String redirectLink =
                                    fileLine.substring(fileLine.lastIndexOf("<"),
                                            fileLine.lastIndexOf(">") + 1);


                            redirectsMap.put(resourceLink, redirectLink);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }

        return redirectsMap;
    }
}
