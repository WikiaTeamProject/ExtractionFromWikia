package wikiaStatistics;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

/**
 * Created by Jan Portisch on 14.03.17.
 */
public class URLExtractor {


    public static void main(String[] args) {

        Logger logger = Logger.getLogger("wikiaStatisticsMain");

        // files will be saved in the 'results' directory;
        String filePath1 = "./results/wikiaStatistics/individualFiles/p1_wikis_1_to_500000.csv";
        String filePath2 = "./results/wikiaStatistics/individualFiles/p2_wikis_500000_to_1000000.csv";
        String filePath3 = "./results/wikiaStatistics/individualFiles/p3_wikis_1000000_to_1500000.csv";
        String filePath4 = "./results/wikiaStatistics/individualFiles/p4_wikis_1500000_to_2000000.csv";

        Thread t1 = new Thread(new URLGetter(filePath1,1, 500000), "Thread 1");
        Thread t2 = new Thread(new URLGetter(filePath2,500000, 1000000), "Thread 2");
        Thread t3 = new Thread(new URLGetter(filePath3,1000000, 1500000), "Thread 3");
        Thread t4 = new Thread(new URLGetter(filePath4,1500000, 2000000), "Thread 4");

        // t1.start();
        // t2.start();
        // t3.start();
        // t4.start();

        logger.info("Download process finished.");
        logger.info("Concatenating files...");

        // wait until all threads are finished before merging files
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch(InterruptedException ie){
            logger.severe(ie.toString());
        }

        // WikiaStatisticsTools.mergeFiles(filePath1, filePath2, filePath3, filePath4);
        try {
            HashMap<String, Integer> result = WikiaStatisticsTools.getDifferentLanguages(new File("./results/wikiaStatistics.csv"));
            for (Map.Entry<String, Integer> entry: result.entrySet()) {
                if(entry.getValue() < 10){
                    // skip entries with less that 10 occurrences
                    continue;
                }
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }

        } catch (FileNotFoundException fnfe){
            logger.severe(fnfe.toString());
        }

    }


    /**
     * This class queries the wikia API from the lowerIdLimit up to the upperIdLimit.
     * This class is optimized for multi-threading.
     */
    private static class URLGetter implements Runnable {

        private int lowerIdLimit; // the server will be queried starting with this id
        private int upperIdLimit; // the server will be queried up to this id
        private int startingID; // this is the ID with which the current query starts
        private int startingIDbefore; // this is the ID with which the previous query started
        private int numberOfIterations = 1; // just a counter to calculate how many iterations were completed

        private String filepath;
        private String responseLine;
        private StringBuffer completeResponse;
        private StringBuffer generatedURL;
        private InputStream inputStream;
        private InputStreamReader inputStreamReader;
        private BufferedReader bReader;
        private BufferedWriter bufferedWriter;

        private URL url;
        private URLConnection urlConnection;

        private ObjectMapper mapper;

        public Logger logger = Logger.getLogger(getClass().getName());


        /**
         * Constructor
         * @param filePath Path to the file where the queried results will be saved in CSV format.
         * @param lowerIdLimit ID that will be used for starting the queries. Must be positive. Must be at least 1.
         * @param upperIdLimit Upper Limit of IDs up to which will be queried. Must be positive. Must be larger than 1.
         */
        public URLGetter(String filePath, int lowerIdLimit, int upperIdLimit) {
            logger.setLevel(Level.FINEST);
            this.lowerIdLimit = Math.min(1, Math.abs(lowerIdLimit)); // just to be sure that the parameter is legitimate
            startingID = lowerIdLimit;
            this.filepath = filePath;
            this.upperIdLimit = Math.abs(upperIdLimit);
            mapper = new ObjectMapper();
        }

        /**
         * Constructor
         * @param filePath Path to the file where the queried results will be saved in CSV format.
         * @param upperIdLimit Upper Limit of IDs up to which will be queried. Must be positive. Must be larger than 1.
         */
        public URLGetter(String filePath, int upperIdLimit) {
            this(filePath, 1, upperIdLimit);
        }


        /**
         * Run method for the thread.
         */
        public void run() {

            try {
                bufferedWriter = new BufferedWriter(new FileWriter(new File(filepath)));
                bufferedWriter.write(ExpandedWikiaItem.getHeader() + "\n"); // just the header line for the CSV file
            } catch (IOException e1) {
                logger.severe(Thread.currentThread().getName() + ": The provided path for the file does not work.");
                e1.printStackTrace();
                return;
            }

            while (startingID <= upperIdLimit) {

                // create URL
                generatedURL = new StringBuffer("http://www.wikia.com/api/v1/Wikis/Details?ids=" + startingID);

                startingID++; // increment startingID because the first ID was already used in the initial URL creation
                startingIDbefore = startingID;

                logger.info(Thread.currentThread().getName() + ": Iteration " + numberOfIterations + " Starting ID: " + startingID);

                // there will always be 50 IDs in one query; 50 turned out to be very stable but I did not evaluate an optimal number. Higher would be better for performance. (jan)
                while (startingID < startingIDbefore + 49) {
                    generatedURL.append("%2C+" + Integer.toString(startingID));
                    startingID++;
                }

                try {

                    url = new URL(generatedURL.toString());
                    urlConnection = url.openConnection();

                    inputStream = urlConnection.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    bReader = new BufferedReader(inputStreamReader);

                    completeResponse = new StringBuffer();

                    // read line by line and save in completeResponse
                    while (((responseLine = bReader.readLine()) != null)) {
                        completeResponse.append(responseLine);
                    }

                    // get JSON OBJECT
                    JSONObject jsonResponse = new JSONObject(completeResponse.toString());

                    // navigate to items
                    JSONObject items = jsonResponse.getJSONObject("items");

                    // get all ids of wikis in items
                    String[] ids = JSONObject.getNames(items);

                    // the if is required to avoid null pointer exceptions
                    if (ids != null) {
                        logger.info(Thread.currentThread().getName() + ": Number of Wikis: " + ids.length + " in iteration: " + numberOfIterations++);

                        for (String id : ids) {
                            // map json string with infos of one wiki to java object
                            ExpandedWikiaItem wiki = mapper.readValue(items.getJSONObject(id).toString(), ExpandedWikiaItem.class);
                            bufferedWriter.write(wiki.toString().replace("\n", "").replace("\r", "") + "\n"); // line breaks have to be deleted
                            // logger.info(id); // leads to a lot of output on the console
                        }
                    }

                } catch (MalformedURLException mue) {
                    logger.severe(mue.toString());
                } catch (IOException ioe) {
                    logger.severe(ioe.toString());
                }

            }
            try {
                bufferedWriter.close();
            } catch (IOException e1) {
                logger.severe(Thread.currentThread().getName() + ": Something went wrong when closing the BufferedWriter.");
            }
        }

        public int getUpperIdLimit() {
            return upperIdLimit;
        }

        public int getLowerIdLimit() {
            return lowerIdLimit;
        }
    } // end of private class "URLGetter"

}
