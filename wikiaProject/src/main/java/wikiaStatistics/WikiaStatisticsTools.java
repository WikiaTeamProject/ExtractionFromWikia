package wikiaStatistics;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.logging.Logger;

/**
 * Created by Jan Portisch on 16.03.2017.
 * A class for helper methods.
 */
public class WikiaStatisticsTools {


    private static Logger logger = Logger.getLogger("WikiaStatisticsTools");


    /**
     * Merges multiple files into one (which is saved under /results/wikiaStatistics/wikiaStatistics.csv)
     * @param filePaths
     */
    public static void mergeFiles(String... filePaths) {
        File resultFile = new File("./results/wikiaStatistics.csv");
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
        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }

        logger.info("Finished merging " + fileNumber + " files.");
    }
}
