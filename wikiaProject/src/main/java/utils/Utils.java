package utils;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * This class includes general util methods usable in all packages.
 */
public class Utils {

    /**
     * This method receives an array of file paths and merges the files into the specified target file
     *
     * @param filePaths
     * @param targetFile
     */
    public static void mergeFiles(ArrayList<String> filePaths, String targetFile) {

    }


    /**
     * This method receives a file path and creates a directory out of it.
     * Mainly used because OSX systems do not automatically use a file as a directory.
     *
     * @param filePath
     */
    public static void createDirectory(String filePath) {

    }


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



}
