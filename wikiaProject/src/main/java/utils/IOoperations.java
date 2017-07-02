package utils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * This class includes general IO/File methods usable in all packages.
 */
public class IOoperations {


    static Logger logger = Logger.getLogger(IOoperations.class.getName());

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



}
