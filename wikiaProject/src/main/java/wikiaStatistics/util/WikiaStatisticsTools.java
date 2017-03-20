package wikiaStatistics.util;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * A class for helper methods.
 */
public class WikiaStatisticsTools {


    private static Logger logger = Logger.getLogger("WikiaStatisticsTools");


    /**
     * Merges multiple files into one (which is saved as wikiaAllOverview.csv in resources)
     *
     * @param filePaths
     */
    public static void mergeFiles(String... filePaths) {
        File resultFile = new File("./wikiaProject/src/main/resources/wikiaAllOverview.csv");
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


    public static HashMap<String, Integer> getDifferentLanguages(File inputFile) throws FileNotFoundException {
        String[] tokens;
        int articles = 0;
        int pages = 0;
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
            String readLine;
            // ignore header line
            bufferedReader.readLine();
            while ((readLine = bufferedReader.readLine()) != null) {
                tokens = readLine.split(";");

                // tokens[1] refers to the url row
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


                try {
                    //tokens[11]: number of articles
                    articles += Integer.parseInt(tokens[11]);
                    //tokens[12]: number of pages
                    pages += Integer.parseInt(tokens[12]);

                } catch (NumberFormatException ne) {
                    logger.warning("Articles/pages of URL " + tokens[1] + " do not include an integer value.");
                }

            } // end of while loop

            System.out.println("Number of articles: " + articles);
            System.out.println("Number of pages: " + pages);

            bufferedReader.close();

        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }
        return result;
    }

}
