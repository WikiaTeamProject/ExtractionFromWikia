package wikiaDumpDownload.util;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import java.util.logging.Logger;
import wikiaStatistics.util.WikiaStatisticsTools;


public class Extraction7zip {
    private static Logger logger = Logger.getLogger(WikiaStatisticsTools.class.getName());

    /**
     *
     * @param fileName fileName of 7Zip file to extract
     */
    public void extract7ZipFile(String fileName){

        try {
            SevenZFile sevenZFile = new SevenZFile(new File(fileName));
            SevenZArchiveEntry zipContents = sevenZFile.getNextEntry();
            File extractedFile=null;
            FileOutputStream fileStream=null;
            byte[] extractedFileContents=null;

            while (zipContents != null) {

                extractedFile = new File(fileName.substring(0,fileName.indexOf(".7z")));
                if (!extractedFile.exists()) {
                    extractedFile.createNewFile();
                }
                fileStream = new FileOutputStream(extractedFile, false);
                extractedFileContents = new byte[(int) zipContents.getSize()];

                sevenZFile.read(extractedFileContents, 0, extractedFileContents.length);

                fileStream.write(extractedFileContents);
                fileStream.close();
                zipContents = (SevenZArchiveEntry) sevenZFile.getNextEntry();
            }
            sevenZFile.close();
        }
        catch(Exception ex){
            logger.severe(ex.toString());
        }

    }

}
