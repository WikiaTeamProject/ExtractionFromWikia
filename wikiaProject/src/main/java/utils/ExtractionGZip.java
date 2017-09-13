package utils;

import loggingService.MessageLogger;

import java.util.ResourceBundle;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.io.File;
import org.apache.log4j.Level;

/**
 * Created by Samresh Kumar on 6/26/2017.
 */
public class ExtractionGZip
{
    private static MessageLogger logger=new MessageLogger();
    private static final String MODULE="Utils";
    private static final String CLASS=ExtractionGZip.class.getName();
    private static String downloadedDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/downloadedWikis";
    private String directoryExtracted;

    /**
     * Constructor for Extraction GZip Class
     */
    public ExtractionGZip() {
        directoryExtracted = downloadedDirectoryPath + "/decompressed/";
        IOoperations.createDirectory(directoryExtracted);
    }


    /**
     * This method will extract Gzip file
     * (Please note: this method assumes that gzip archive contains just one file)
     * @param gzipFile :gzip file to extract
     * @param trgFolderName : Folder to where file will be extracted
     */
    public void extractGzipFile(String gzipFile,String trgFolderName){
        try{

            File archiveFile=
                    new File(gzipFile);

            GZIPInputStream gzipStream=
                    new GZIPInputStream(new FileInputStream(archiveFile));


            File outputFile=new
                    File(trgFolderName+"//"+archiveFile.getName().
                    substring(0,archiveFile.getName().lastIndexOf(".")));


            if(outputFile.exists())
                outputFile.delete();

            outputFile.createNewFile();


            FileOutputStream
                    outputFileStream=new FileOutputStream(outputFile);

            byte[] inputStreamBuffer = new byte[1024];
            int length;

            while((length = gzipStream.read(inputStreamBuffer)) != -1){
                outputFileStream.write(inputStreamBuffer, 0, length);
            }

            //close buffers
            outputFileStream.close();
            gzipStream.close();

        }
        catch(Exception ex){
            logger.logMessage(Level.FATAL,MODULE,CLASS,ex.getMessage());
        }

    }


    /**
     * This method will extract all GZip Files in a directory
     * @param sourceDirectory : directory containing gzip files
     * @param targetDirectory : directory where extracted files
     *                        will be placed
     */
    public void extractAllGzipFiles(String sourceDirectory,
                                 String targetDirectory){
        try{
            File srcDirectory=
                    new File(sourceDirectory);

            File trgDirectory=
                    new File(targetDirectory);

            if(!trgDirectory.exists()){
                trgDirectory.mkdir();
            }

            if(srcDirectory.isDirectory()) {
                File[] gzipFiles =
                        srcDirectory.listFiles();

                for(File file:gzipFiles){
                    if(file.getName().endsWith(".gz")) {
                        extractGzipFile(file.getAbsolutePath(),
                                file.getAbsolutePath());
                    }
                }
            }

        }
        catch(Exception ex){
            logger.logMessage(Level.FATAL,MODULE,CLASS,ex.getMessage());
        }
    }

    /**
     * Unzips all Gzip files of the wikiaDumps/downloaded/gz folder
     * into the wikiaDumps/extractedgz folder
     *
     */
    public void extractAllGZipFilesIntoDesignatedFolder() {
        String foldergz = downloadedDirectoryPath + "/downloaded/gz/";
        File folder = new File(foldergz);

        for (File filegz : folder.listFiles()) {
            logger.logMessage(Level.INFO,MODULE,CLASS,"Extraction for following gz file is started: " + filegz.getAbsolutePath());
            extractGzipFile(filegz.getAbsolutePath(),
                    this.directoryExtracted);
        }
    }
}
