package wikiaStatistics.controller;

import utils.IOoperations;
import wikiaStatistics.util.WikiaStatisticsTools;

import java.io.File;
import java.util.ArrayList;
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

        String statisticsDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/wikiStatistics";

        // files will be saved in the newly created subdirectory
        IOoperations.createDirectory(statisticsDirectoryPath);
        IOoperations.createDirectory(statisticsDirectoryPath + "/wikiaOverviewIndividualFiles");

        int lowerIdLimit = 0;
        int upperIdLimit = 50000;

        for (int i = 0; i < threads.length; i++) {

            String filePath = statisticsDirectoryPath + "/wikiaOverviewIndividualFiles/wikis_" + lowerIdLimit + "_to_" + upperIdLimit +".csv";
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

    /**
     * There are various prerequisites. To allow for a stable program, the prerequisites are checked in this method.
     *
     * @return
     */
    public static boolean checkPrerequisites() {
        File file = new File(ResourceBundle.getBundle("config").getString("pathToRootDirectory"));

        // check whether the path to the root directory is really a directory
        if (file.isDirectory()) return true;

        logger.severe("Variable pathToRootDirectory in file config.properties is not a directory.");
        return false;
    }

}
