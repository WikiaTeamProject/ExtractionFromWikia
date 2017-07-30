package wikiaDumpDownload.controller;

import utils.IOoperations;
import wikiaStatistics.controller.MetadataThreadImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class represents the implementation of WikiaDumpDownloadThread threads.
 */
public class WikiaDumpDownloadThreadImpl  {

    private static Logger logger = Logger.getLogger(WikiaDumpDownloadThreadImpl.class.getName());
    private static Thread[] threads = new Thread[40];

//    public static void downloadWikiaDumps(int beginLine, int endLine) {
//        String filePath = getFilePath();
//
//        Thread t1 = new Thread(new WikiaDumpDownloadThread(filePath, beginLine, endLine));
//        t1.run();
//
//    }
//
//    public static void downloadWikiaDumps(int amount) {
//        String filePath = getFilePath();
//
//        // exclude header line (0)
//        Thread t1 = new Thread(new WikiaDumpDownloadThread(filePath, 1, amount));
//        t1.run();
//
//    }

    public static void downloadWikiaDumps() {

        createThreads();
    }

    private static String getFilePath() {
        String statisticsDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/wikiStatistics";
        String filePath = statisticsDirectoryPath + "/wikiaAllOverview.csv";

        return filePath;
    }


    private static void createThreads() {
        String statisticsDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/wikiStatistics";

        ArrayList<String> filePaths = new ArrayList<String>();

        int lowerIdLimit = 0;
        int upperIdLimit = 50000;

        IOoperations.createDirectory(statisticsDirectoryPath + "/wikiaOverviewIndividualDumpSizes");


        for (int i = 0; i < threads.length; i++) {

            String filePath = statisticsDirectoryPath + "/wikiaOverviewIndividualFiles/wikis_" + lowerIdLimit + "_to_" + upperIdLimit + ".csv";
            String dumpSizeFilePath = statisticsDirectoryPath + "/wikiaOverviewIndividualDumpSizes/dumpsSizes_" + lowerIdLimit + "_to_" + upperIdLimit + ".csv";
            filePaths.add(dumpSizeFilePath);

            threads[i] = new Thread(new WikiaDumpDownloadThread(filePath, dumpSizeFilePath));

            lowerIdLimit += 50000;
            upperIdLimit += 50000;

            threads[i].start();
        }

        // wait until all threads are finished before merging files
        try {
            for (Thread t : threads) {
                t.join();
            }

            logger.info("Dump Download process finished.");
            logger.info("Concatenating files...");

        } catch(InterruptedException ie){
            logger.severe(ie.toString());
        }

        mergeFiles(filePaths);

    }

    private static void mergeFiles(ArrayList<String> filePaths) {
        String statisticsDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/wikiStatistics";

        File resultFile = new File(statisticsDirectoryPath + "/wikiaOverviewDumpSizes.csv");
        File f;
        int fileNumber = 0;
        BufferedReader bufferedReader;
        String currentLine;
        BufferedWriter bufferedWriter;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(resultFile));
            for (String path : filePaths) {
                fileNumber++;
                logger.info("Starting with file number " + fileNumber + ": " + path);
                f = new File(path);
                bufferedReader = new BufferedReader(new FileReader(f));

                if (fileNumber == 1) {
                    // read header
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(currentLine + "\n");
                    }
                } else {
                    // skip header line
                    bufferedReader.readLine();
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        bufferedWriter.write(currentLine + "\n");
                    }
                }

            }

            bufferedWriter.close();
        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }

        logger.info("Finished merging " + fileNumber + " files.");
    }

    /**
     * There are various prerequisites. To allow for a stable program, the prerequisites are checked in this method.
     *
     * @return
     */
    public static boolean checkPrerequisites() {

        // check whether config.properties file was copied
        if (ClassLoader.getSystemResource("config.properties") == null) {
            logger.severe("Please copy the sample config.properties file from folder additionalFiles into resources and adjust it.");
            return false;
        }

        File file = new File(ResourceBundle.getBundle("config").getString("pathToRootDirectory"));

        // check whether the path to the root directory is really a directory
        if (! file.isDirectory()) {
            logger.severe("Variable pathToRootDirectory in file config.properties is not a directory. Please adjust it.");
            return false;
        }

        String wikiAllOverview = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/wikiStatistics/wikiaAllOverview.csv";
        File wikiCSV = new File(wikiAllOverview);

        // check if wiki overview CSV file is already existing, if not create it
        if (!wikiCSV.exists()) {
            logger.info("File wikiaAllOverview.csv does not exist yet. Wikia metadata will first be downloaded.");
            MetadataThreadImpl.downloadWikiaMetadata();
        }

        return true;
    }

}
