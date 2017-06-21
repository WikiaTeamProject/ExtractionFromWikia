package wikiaStatistics.controller;

import utils.FileOperations;
import wikiaStatistics.util.WikiaStatisticsTools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class represents the implementation of MetadataThread threads.
 */
public class MetadataThreadImpl {

    private static Logger logger = Logger.getLogger(MetadataThreadImpl.class.getName());

    private static Thread[] threads = new Thread[40];
    private static ArrayList<String> filePaths = new ArrayList<String>();



    public static void downloadWikiaMetadata() {

        String directoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory");

        // files will be saved in the newly created subdirectory
        File file = FileOperations.createDirectory(directoryPath + "/wikiStatistics");

        int lowerIdLimit = 0;
        int upperIdLimit = 50000;

        for (int i = 0; i < threads.length; i++) {

            String filePath = directoryPath + "/wikiStatistics/wikis_" + lowerIdLimit + "_to_" + upperIdLimit +".csv";
            filePaths.add(filePath);
            threads[i] =  new Thread(new MetadataThread(filePath, lowerIdLimit, upperIdLimit), "Thread " + i);

            lowerIdLimit += 50000;
            upperIdLimit += 50000;

            threads[i].start();
        }

        // wait until all threads are finished before merging files
        try {
            for (Thread t: threads) {
                t.join();
            }

            logger.info("Download process finished.");
            logger.info("Concatenating files...");

        } catch(InterruptedException ie){
            logger.severe(ie.toString());
        }

        WikiaStatisticsTools.mergeFiles(filePaths);

    }

}
