package applications.wikiaStatistics.util;

import applications.extraction.model.WikiaWikiProperties;
import applications.extraction.Extractor;
import applications.wikiaStatistics.model.MetadataStatistics;
import utils.OSDetails;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;


/**
 * A class for helper methods concerning statistics.
 * Resulting data is saved in <root>/statistics
 */
public class WikiaStatisticsTools {

    private static Logger logger = Logger.getLogger(WikiaStatisticsTools.class.getName());
    private static String statisticsDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/statistics";


    /**
     * Merges multiple files into one (which is saved as wikiaAllOverview.csv in the specified directory)
     *
     * @param filePaths
     */
    public static void mergeFiles(ArrayList<String> filePaths) {

        File resultFile = new File(statisticsDirectoryPath + "/wikiaAllOverview.csv");
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
     * Creates a MetadataStatistics object with variables including statistics
     *
     * @return created MetadataStatistics object
     */
    public static MetadataStatistics getMetadataStatistics() {
        String[] tokens;
        String possibleLanguageCode;
        MetadataStatistics statistics = new MetadataStatistics();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(statisticsDirectoryPath + "/wikiaAllOverview.csv")));
            String readLine;

            // ignore header line
            bufferedReader.readLine();

            // read wiki metadata information one after another
            while ((readLine = bufferedReader.readLine()) != null) {
                tokens = readLine.split(";");

                logger.info("Processing: " + tokens[1]);

                // count language codes, default english

                // tokens[1] refers to the url row, url has to contain at least one dot
                if (tokens[1].indexOf(".") != -1) {

                    // retrieve string between "http://" and first dot as possible language code
                    possibleLanguageCode = tokens[1].substring(7, tokens[1].indexOf("."));

                    // find out if it is an actual language code
                    if (statistics.getLanguageCounts().containsKey(possibleLanguageCode)) {
                        // count +1 for this language code
                        statistics.getLanguageCounts().put(possibleLanguageCode, statistics.getLanguageCounts().get(possibleLanguageCode) + 1);

                    } else {
                        // if no specific language code exists, count +1 for english
                        statistics.getLanguageCounts().put("en", statistics.getLanguageCounts().get("en") + 1);

                    }

                    // count number of overall articles (tokens[11]) and pages (tokens[12])
                    try {
                        statistics.setNumberOfArticles(statistics.getNumberOfArticles() + Integer.parseInt(tokens[11]));
                        statistics.setNumberOfPages(statistics.getNumberOfPages() + Integer.parseInt(tokens[12]));

                    } catch (NumberFormatException ne) {
                        logger.warning("Articles/pages of URL " + tokens[1] + " do not include an integer value.");
                    }
                }

                // add hub
                if(!tokens[7].equalsIgnoreCase("null") && !tokens[7].equalsIgnoreCase("")  && tokens[7] != null ){
                    if(statistics.getHubCounts().containsKey(tokens[7])){
                        // key already in store, increment by 1
                        statistics.getHubCounts().put(tokens[7], statistics.getHubCounts().get(tokens[7])  + 1 );
                    } else {
                        // key not in store, initialize
                        statistics.getHubCounts().put(tokens[7], 1);
                    }
                }

               // add topic
                if(!tokens[8].equalsIgnoreCase("null") && !tokens[8].equalsIgnoreCase("") && tokens[8] != null ){
                    if(statistics.getTopicCounts().containsKey(tokens[8])){
                        // key already in store, increment by 1
                        statistics.getTopicCounts().put(tokens[8], statistics.getTopicCounts().get(tokens[8])  + 1 );
                    } else {
                        // key not in store, initialize
                        statistics.getTopicCounts().put(tokens[8], 1);
                    }
                }

            } // end of while loop

            bufferedReader.close();

        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }

        return statistics;
    }


    /**
     * This method generates statistics files from
     * downloaded dumps
     */
    public void createWikiaStatisticsFromDownloadedDumps(){

        Extractor extractor=new Extractor();

        SimpleDateFormat dateFormater=new
                SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        String newLineCharacter =
                OSDetails.getNewLineCharacter();

        String staticsFileName=statisticsDirectoryPath+"//applications.wikiaStatistics.csv";

        HashMap<String,WikiaWikiProperties> prop=extractor.extractPropertiesForAllWikis();

        try {

            File wikiaStatisticsDirectory=new File(statisticsDirectoryPath);

            if(!wikiaStatisticsDirectory.exists()){
                wikiaStatisticsDirectory.mkdir();
            }

            PrintWriter fileWriter
                    = new PrintWriter(staticsFileName);


            //Writing header row
            fileWriter.write
                    ("Wiki Name,Language Code,Last Modified Date," +
                            "File Path,wiki Size,"+newLineCharacter);

            for(String wiki:prop.keySet()){
                WikiaWikiProperties wikiProp=prop.get(wiki);

                fileWriter.write(wikiProp.getWikiName().replace(","," ")+
                        ","+ wikiProp.getLanguageCode()+ ","+
                        dateFormater.format(wikiProp.getLastModifiedDate())+","+
                        wikiProp.getWikiPath()+","
                        +wikiProp.getWikiSize() +","+
                        newLineCharacter);
            }

            fileWriter.close();
        }
        catch(Exception ex){
            logger.severe(ex.getMessage());

        }
    }

}
