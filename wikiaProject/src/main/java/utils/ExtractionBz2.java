package utils;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class can extract bz2 files.
 */
public class ExtractionBz2 {

    private static Logger logger = Logger.getLogger(ExtractionBz2.class.getName());

    /**
     * Extract one bz2 File.
     *
     * @param pathToFileToExtract The path to the bz2 compressed file that shall be extracted.
     * @param pathToNewDirectory  Path to where the new file shall be written (without the new filename). The new file name will be derived from the old file name.
     */
    public static boolean extract(String pathToFileToExtract, String pathToNewDirectory) {
        try {

            // get file name
            String regularExpressionToGetFileNameForwardSlash = "(?<=\\/)[^\\/]*$"; // plain: (?<=\/)[^\/]*$
            String regularExpressionToGetFileNameBackwardSlash = "(?<=\\\\)[^\\\\]*$"; // plain (?<=\\)[^\\]*$
            String fileName = "default.ttl.bz2"; // for the name of the new file, use default if it cannot be extracted

            Pattern pattern = Pattern.compile(regularExpressionToGetFileNameForwardSlash);
            Matcher matcher = pattern.matcher(pathToFileToExtract);
            if (matcher.find()) {
                fileName = matcher.group(0);
            } else {
                pattern = Pattern.compile(regularExpressionToGetFileNameBackwardSlash);
                matcher = pattern.matcher(pathToFileToExtract);
                if (matcher.find()) {
                    fileName = matcher.group(0);
                }
            }

            // cut the ".bz2" file ending
            fileName = fileName.substring(0, (fileName.length() - 4));

            File extractedResultFile = new File(pathToNewDirectory + "/" + fileName);
            if(extractedResultFile.exists()){
                return false;
            }

            // do the actual extraction
            FileInputStream in = new FileInputStream(pathToFileToExtract);
            FileOutputStream out = new FileOutputStream(extractedResultFile);
            BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
            final byte[] buffer = new byte[8192];
            int n = 0;
            while (-1 != (n = bzIn.read(buffer))) {
                out.write(buffer, 0, n);
            }
            out.close();
            bzIn.close();
            return true;

        } catch (IOException ioe) {
            logger.severe("A problem occurred. Common error: This method tries to extract a single file. If you want to extract all files in a directory use another method.");
            logger.severe(ioe.toString());
            return false;
        }
    }

    /**
     * This method will extract bz2 files specified in pathToFiles and write the decompressed files into the directory pathToNewDirectory.
     * If the specified target directory does not exist, it will be created.
     *
     * @param pathToFiles        Path to the files that shall be extracted. Only bz2 compressed files will be extracted and written to the new directory specified in pathToNewDirectory.
     * @param pathToNewDirectory The path where the decompressed files will be written to.
     */
    public static void extractAllFilesInDirectory(String pathToFiles, String pathToNewDirectory) {

        // create pathToNewDirectory if it does not exist yet
        File newDirectoryPath = new File(pathToNewDirectory);
        if (!newDirectoryPath.exists()) {
            newDirectoryPath.mkdir();
        }

        File sourceFilesDirectory = new File(pathToFiles);
        File[] filesList = sourceFilesDirectory.listFiles();

        for (File f : filesList) {
            if (f.isFile()) {

                if (f.getName().endsWith(".bz2")) {
                    extract(f.getAbsolutePath(), pathToNewDirectory);
                }
            }
        } // end of for loop
    } // end of method extractAllFilesInDirectory

}
