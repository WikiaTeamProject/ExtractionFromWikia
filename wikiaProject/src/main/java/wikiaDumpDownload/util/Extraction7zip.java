package wikiaDumpDownload.util;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import utils.FileOperations;


public class Extraction7zip {
    private static Logger logger = Logger.getLogger(Extraction7zip.class.getName());
    private String directoryExtracted;
    private static String downloadedDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/downloadedWikis";


    /**
     * Constructor for the 7zip Extraction.
     *
     */
    public Extraction7zip() {
        directoryExtracted = downloadedDirectoryPath + "/extracted7z/";
        FileOperations.createDirectory(directoryExtracted);
    }


    /**
     * Extracts the specified .7z file to the target directory.
     * @param compressedFile The compressed file.
     * @param targetDirectory The directory where the extracted file is to be saved.
     * @return
     */
    public boolean extract7ZipFile(File compressedFile, File targetDirectory){
        try {

            String fileName = compressedFile.getName();

            // make sure the file really is a .7z file:
            if(! (fileName.endsWith(".7z")) ){
                logger.severe("Not a .7z file.");
                return false;
            }

            // make sure the target directory exists
            if(targetDirectory.exists()){
                if(!targetDirectory.isDirectory()){
                    logger.severe("Target directory not a directory.");
                    return false;
                }
            } else {
                targetDirectory.mkdir();
            }

            SevenZFile sevenZFile = new SevenZFile(compressedFile);
            SevenZArchiveEntry zipContents = sevenZFile.getNextEntry();
            File extractedFile=null;
            FileOutputStream fileStream=null;
            byte[] extractedFileContents=null;
            String extractedFileName="";

            while (zipContents != null) {
                extractedFileName = fileName.substring(0,fileName.indexOf(".7z"));

                extractedFile = new File(targetDirectory.getPath() + "/" + extractedFileName);
                if (!extractedFile.exists()) {
                    extractedFile.createNewFile();
                }
                fileStream = new FileOutputStream(extractedFile, false);
                extractedFileContents = new byte[(int) zipContents.getSize()];

                sevenZFile.read(extractedFileContents, 0, extractedFileContents.length);

                fileStream.write(extractedFileContents);
                fileStream.close();
                zipContents = sevenZFile.getNextEntry();
            }
            sevenZFile.close();

            logger.info("File saved: " + extractedFileName);
            logger.info("The file was extracted successfully as " + extractedFile.getAbsolutePath() + ".");
            return true;
        }
        catch(Exception ex){
            logger.severe("The file could not be downloaded. " + ex.toString());
            return false;
        }
    }

    // convenience overload
    public boolean extract7ZipFile(String compressedFile, String targetDirectory) {
        return extract7ZipFile(new File(compressedFile), new File(targetDirectory));
    }

    // convenience overload
    public boolean extract7ZipFile(File compressedFile, String targetDirectory) {
        return extract7ZipFile(compressedFile, new File(targetDirectory));
    }

    // convenience overload
    public boolean extract7ZipFile(String compressedFile, File targetDirectory) {
        return extract7ZipFile(new File(compressedFile), targetDirectory);
    }


    /**
     * Receives a filepath of a 7zip file as input string and extracts it into the wikiaDumps/extracted7z folder
     * @param compressedFile 7Zip file to extract.
     * @return true if extraction was successful, else false.
     */
    public boolean extract7ZipFileIntoDesignatedFolder(File compressedFile) {
        return extract7ZipFile(compressedFile, new File(directoryExtracted));
    }

    /**
     * Extracts a 7zip file.
     * @param pathToFile Path to the file to be compressed.
     * @return true if extraction was successful, else false.
     */
    public boolean extract7ZipFileIntoDesignatedFolder(String pathToFile){
        return extract7ZipFileIntoDesignatedFolder(new File(pathToFile));
    }


    /**
     * Unzips all 7zip files of the wikiaDumps/downloaded/7zip folder into the wikiaDumps/extracted7z folder
     *
     */
    public void extractAll7ZipFilesIntoDesignatedFolder() {
        String folder7z = downloadedDirectoryPath + "/downloaded/7z";
        File folder = new File(folder7z);

        for (File file7z : folder.listFiles()) {
            logger.info("Extraction for following 7z file is started: " + file7z.getAbsolutePath());
            extract7ZipFileIntoDesignatedFolder(file7z.getAbsolutePath());
        }
    }

}