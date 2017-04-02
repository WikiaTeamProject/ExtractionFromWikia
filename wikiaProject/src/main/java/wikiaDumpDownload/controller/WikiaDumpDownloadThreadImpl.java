package wikiaDumpDownload.controller;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class represents the implementation of WikiaDumpDownloadThread threads.
 */
public class WikiaDumpDownloadThreadImpl  {

    private static Logger logger = Logger.getLogger(WikiaDumpDownloadThreadImpl.class.getName());

    private static Thread[] threads = new Thread[40];


    public static void downloadWikiaDumps(int beginLine, int endLine) {
        String filePath = getFilePath();

        Thread t1 = new Thread(new WikiaDumpDownloadThread(filePath, beginLine, endLine));
        t1.run();

    }

    public static void downloadWikiaDumps(int amount) {
        String filePath = getFilePath();

        // exclude header line (0)
        Thread t1 = new Thread(new WikiaDumpDownloadThread(filePath, 1, amount));
        t1.run();

    }

    public static void downloadWikiaDumps() {
        createThreads();
    }

    private static String getFilePath() {
        String directoryPath = ResourceBundle.getBundle("config").getString("directory");
        String filePath = directoryPath + "/wikiaAllOverview.csv";

        return filePath;
    }


    private static void createThreads() {
        ArrayList<String> filePaths = new ArrayList<String>();
        String directoryPath = ResourceBundle.getBundle("config").getString("directory");

        int lowerIdLimit = 0;
        int upperIdLimit = 50000;

        File file = new File(directoryPath + "/wikiaOverviewIndividualDumpSizes");
        createDirectory(file);


        for (int i = 0; i < threads.length; i++) {

            String filePath = directoryPath + "/wikiaOverviewIndividualFiles/wikis_" + lowerIdLimit + "_to_" + upperIdLimit + ".csv";
            String dumpSizeFilePath = directoryPath + "/wikiaOverviewIndividualDumpSizes/dumpsSizes_" + lowerIdLimit + "_to_" + upperIdLimit + ".csv";
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

    private static File createDirectory(File file) {
        if (!file.isDirectory()) {
            try {
                Files.createDirectory(file.toPath());
            } catch (IOException e) {
                logger.severe(e.toString());
            }
        }
        return file;
    }

    private static void mergeFiles(ArrayList<String> filePaths) {

        String directoryPath = ResourceBundle.getBundle("config").getString("directory");

        File resultFile = new File(directoryPath + "/wikiaOverviewDumpSizes.csv");
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

}
