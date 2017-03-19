package wikiaStatistics;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by Jan Portisch on 16.03.2017.
 * A class for helper methods.
 */
public class WikiaStatisticsTools {


    private static Logger logger = Logger.getLogger("WikiaStatisticsTools");


    /**
     * Merges multiple files into one (which is saved under /results/wikiaStatistics/wikiaStatistics.csv)
     *
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


    public static HashMap<String, Integer> getDifferentLanguages(File inputFile) throws FileNotFoundException {
        String[] tokens;
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                tokens = readLine.split(";");

                if (tokens[1].length() > 11) {

                    logger.info("Processing: " + tokens[1]);


                    if (tokens[1].charAt(9) == '.') {
                        // 2-character language
                        if (result.containsKey((tokens[1].substring(7, 9)))) {
                            // language has occurred before -> increment count by one
                            result.put(tokens[1].substring(7, 9), result.get(tokens[1].substring(7, 9) ) + 1);
                            logger.info("Language detected: " + tokens[1].substring(7,9));
                        } else {
                            // first occurrence of language
                            result.put(tokens[1].substring(7, 9), 1);
                            logger.info("New language detected: " + tokens[1].substring(7,9));
                        }

                    }


                    // language seems to be only 2 characters long
                    /**
                    else if (tokens[1].charAt(10) == '.') {
                        // 3-character language

                        if (result.containsKey((tokens[1].substring(7, 10)))) {
                            // language has occurred before -> increment count by one
                            result.put(tokens[1].substring(7, 10), result.get(tokens[1].substring(7, 10)) + 1);
                            logger.info("Language detected: " + tokens[1].substring(7,10));
                        } else {
                            // first occurrence of language
                            result.put(tokens[1].substring(7, 10), 1);
                            logger.info("New language detected: " + tokens[1].substring(7,10));
                        }
                    }
                     **/

                    else {
                        if(result.containsKey("en")){
                            result.put("en", result.get("en") + 1);
                        } else {
                            result.put("en", 1);
                        }
                    }
                }

            }
        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }

        return result;
    }

}
