package applications.wikiaDumpDownload.controller;

import utils.IOoperations;
import applications.wikiaStatistics.controller.MetadataThreadImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import loggingService.MessageLogger;
import org.apache.log4j.Level;

/**
 * This class represents the implementation of WikiaDumpDownloadThread threads.
 */
public class WikiaDumpDownloadThreadImpl {

    private static MessageLogger logger=new MessageLogger();
    private static final String MODULE="wikiDumpDOwnload";
    private static final String CLASS=WikiaDumpDownloadThreadImpl.class.getName();

    private static Thread[] threads = new Thread[40];
    private static String statisticsDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/statistics";

//    public static void downloadWikiaDumps(int beginLine, int endLine) {
//        String filePath = getFilePathOfWikiaAllOverview();
//
//        Thread t1 = new Thread(new WikiaDumpDownloadThread(filePath, beginLine, endLine));
//        t1.run();
//
//    }
//
//    public static void downloadWikiaDumps(int amount) {
//        String filePath = getFilePathOfWikiaAllOverview();
//
//        // exclude header line (0)
//        Thread t1 = new Thread(new WikiaDumpDownloadThread(filePath, 1, amount));
//        t1.run();
//
//    }

    /**
     * Start the process using threads.
     */
    public static void downloadWikiaDumps() {
        createThreads();
    }

    /**
     *
     * @param urls
     */
    public static void downloadWikiaDumps(List<String> urls) {

        IOoperations.createDirectory(statisticsDirectoryPath);
        IOoperations.createDirectory(statisticsDirectoryPath + "//wikiaOverviewIndividualDumpSizes");
        IOoperations.createDirectory(statisticsDirectoryPath + "//wikiaOverviewIndividualDumpURLs");

        String dumpSizeFilePath = statisticsDirectoryPath + "//wikiaOverviewDumpSizes.csv";
        String dumpURLsFilePath = statisticsDirectoryPath + "//wikiaOverviewDumpURLs.csv";

        Thread thread = new Thread(new WikiaDumpDownloadThread(urls, dumpSizeFilePath, dumpURLsFilePath));
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.logMessage(Level.INFO,MODULE,CLASS,"Dump Download process finished.");
    }

    /**
     * Returns the file path of the file containing all wiki links.
     *
     * @return
     */
    private static String getFilePathOfWikiaAllOverview() {
        String filePath = statisticsDirectoryPath + "/wikiaAllOverview.csv";
        return filePath;
    }


    /**
     *
     */
    private static void createThreads() {
        ArrayList<String> filePaths = new ArrayList<String>();
        ArrayList<String> dumpURLsFiles = new ArrayList<String>();

        int lowerIdLimit = 0;
        int upperIdLimit = 50000;

        IOoperations.createDirectory(statisticsDirectoryPath);
        IOoperations.createDirectory(statisticsDirectoryPath + "/wikiaOverviewIndividualDumpSizes");
        IOoperations.createDirectory(statisticsDirectoryPath + "/wikiaOverviewIndividualDumpURLs");

        for (int i = 0; i < threads.length; i++) {

            String filePath = statisticsDirectoryPath + "/wikiaOverviewIndividualFiles/wikis_" + lowerIdLimit + "_to_" + upperIdLimit + ".csv";
            String dumpSizeFilePath = statisticsDirectoryPath + "/wikiaOverviewIndividualDumpSizes/dumpsSizes_" + lowerIdLimit + "_to_" + upperIdLimit + ".csv";
            String dumpURLsFilePath = statisticsDirectoryPath + "/wikiaOverviewIndividualDumpURLs/dumpsURL_" + lowerIdLimit + "_to_" + upperIdLimit + ".csv";

            filePaths.add(dumpSizeFilePath);
            dumpURLsFiles.add(dumpURLsFilePath);

            threads[i] = new Thread(new WikiaDumpDownloadThread(filePath, dumpSizeFilePath, dumpURLsFilePath));

            lowerIdLimit += 50000;
            upperIdLimit += 50000;

            threads[i].start();
        }

        // wait until all threads are finished before merging files
        try {
            for (Thread t : threads) {
                t.join();
            }

            logger.logMessage(Level.INFO,MODULE,CLASS,"Dump Download process finished.");
            logger.logMessage(Level.INFO,MODULE,CLASS,"Concatenating files...");

        } catch (InterruptedException ie) {
            logger.logMessage(Level.FATAL,MODULE,CLASS,ie.toString());
        }

        mergeFiles(filePaths);
        mergeDumpURLsFiles(dumpURLsFiles);

    }


    /**
     * Merges individual files with wiki links created by the threads.
     *
     * @param filePaths
     */
    private static void mergeFiles(ArrayList<String> filePaths) {
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
                logger.logMessage(Level.INFO,MODULE,CLASS,"Starting with file number " + fileNumber + ": " + path);
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
            logger.logMessage(Level.FATAL,MODULE,CLASS,ioe.toString());
        }

        logger.logMessage(Level.INFO,MODULE,CLASS,"Finished merging " + fileNumber + " files.");
    }


    /**
     * There are various prerequisites. To allow for a stable program, the prerequisites are checked in this method.
     *
     * @return
     */
    public static boolean checkPrerequisites(boolean withFile) {

        // check whether config.properties file was copied
        if (ClassLoader.getSystemResource("config.properties") == null) {
            logger.logMessage(Level.FATAL,MODULE,CLASS,"Please copy the sample config.properties file from folder additionalFiles into resources and adjust it.");
            return false;
        }

        File file = new File(ResourceBundle.getBundle("config").getString("pathToRootDirectory"));

        // check whether the path to the root directory is really a directory
        if (!file.isDirectory()) {
            logger.logMessage(Level.FATAL,MODULE,CLASS,"Variable pathToRootDirectory in file config.properties is not a directory. Please adjust it.");
            return false;
        }

        String pathLanguageCodes = WikiaDumpDownloadThreadImpl.class.getClassLoader().getResource("files/wikiaLanguageCodes.csv").getPath();
        File languageCodes = new File(pathLanguageCodes);
        if (!languageCodes.exists()) {
            logger.logMessage(Level.FATAL,MODULE,CLASS,"wikiaLanguageCodes.csv in resource directory does not exist.");
            return false;
        }

        String wikiAllOverview = getFilePathOfWikiaAllOverview();
        File wikiCSV = new File(wikiAllOverview);

        // check if wiki overview CSV file is already existing, if not create it
        if (!wikiCSV.exists() && withFile) {
            logger.logMessage(Level.INFO,MODULE,CLASS,"File wikiaAllOverview.csv does not exist yet. Wikia metadata will first be downloaded.");
            MetadataThreadImpl.downloadWikiaMetadata();
        }

        return true;
    }


    /**
     * @param dumpURLsFiles
     */
    private static void mergeDumpURLsFiles(ArrayList<String> dumpURLsFiles) {

        File resultFile = new File(statisticsDirectoryPath + "/wikiaOverviewDumpURLs.csv");
        File f;
        int fileNumber = 0;
        BufferedReader bufferedReader;
        String currentLine;
        BufferedWriter bufferedWriter;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(resultFile));
            for (String path : dumpURLsFiles) {
                fileNumber++;
                logger.logMessage(Level.INFO,MODULE,CLASS,"Starting with file number " + fileNumber + ": " + path);

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
            logger.logMessage(Level.FATAL,MODULE,CLASS,ioe.toString());
        }
        logger.logMessage(Level.INFO,MODULE,CLASS,"Finished merging " + fileNumber + " files.");
    }

}
