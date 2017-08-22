package utils;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Logger;


/**
 * This class extracts individual 7zip files as well as all 7zip files within one directory.
 * There are methods specifically for the purpose of extracting to a certain folder (designated methods) as well as some more general purpose methods.
 */
public class Extraction7zip {
    private static Logger logger = Logger.getLogger(Extraction7zip.class.getName());
    private String directoryExtracted;
    private static String downloadedDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/downloadedWikis/";


    /**
     * Constructor for the 7zip Extraction.
     */
    public Extraction7zip() {
        // Note that the assureDesignatedFunctionality() now makes sure that the desired directory exists.
        // To make this class more flexible and allow for multiple purposes, this was moved out of the constructor.
        // Make sure that you call assureDesignatedFunctionality() for methods that assume that the "designated folder" (=root/extracted/) exists.
    }


    /**
     * This is a helper method which ensures that the target directory (= the designated folder) for methods that automatically use the "designated" folder exist.
     */
    private void assureDesignatedFunctionality(){
        directoryExtracted = downloadedDirectoryPath + "/decompressed/";
        IOoperations.createDirectory(directoryExtracted);
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

            SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");

            System.out.println("Last Modified Date:" + sdf.format(zipContents.getLastModifiedDate()));

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
                extractedFile.setLastModified(zipContents.getLastModifiedDate().getTime());
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
     * @return true if applications.extraction was successful, else false.
     */
    public boolean extract7ZipFileIntoDesignatedFolder(File compressedFile) {
        assureDesignatedFunctionality();
        return extract7ZipFile(compressedFile, new File(directoryExtracted));
    }

    /**
     * Extracts a 7zip file.
     * @param pathToFile Path to the file to be compressed.
     * @return true if applications.extraction was successful, else false.
     */
    public boolean extract7ZipFileIntoDesignatedFolder(String pathToFile){
        assureDesignatedFunctionality();
        return extract7ZipFileIntoDesignatedFolder(new File(pathToFile));
    }


    /**
     * Unzips all 7zip files of the wikiaDumps/downloaded/7zip folder into the wikiaDumps/extracted7z folder
     */
    public void extractAll7ZipFilesIntoDesignatedFolder() {
        assureDesignatedFunctionality();
        String folder7z = downloadedDirectoryPath+"/downloaded/" + "/7z/";
        File folder = new File(folder7z);

        for (File file7z : folder.listFiles()) {
            logger.info("Extraction for following 7z file is started: " + file7z.getAbsolutePath());
            extract7ZipFileIntoDesignatedFolder(file7z.getAbsolutePath());
        }
    }

    /**
     * Unzips all 7zip files of the specified directory into the specified directory.
     */
    public void extractAll7ZipFilesIntoFolder(File extractFrom, File extractTo) {

        for (File file7z : extractFrom.listFiles()) {
            logger.info("Extraction for following 7z file is started: " + file7z.getAbsolutePath());
            extract7ZipFile(file7z.getAbsolutePath(), extractTo);
        }

    }

}