package wikiaStatistics.alex;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexandrahofmann on 14.03.17.
 */
public class URLExtractor {


    public static void main(String[] args) {
        String filePath = "C://Users/D060249/Documents/Mannheim/Semester 2/Team Project/wikis.csv";

        Thread t1 = new Thread(new URLGetter(filePath), "Thread 1");

        //TODO: add threads
        t1.start();
    }


    // runnable that calls the URLs
    private static class URLGetter implements Runnable {

        private static final int UPPER_LIMIT = 100; // the server will be queried up to this number

        private int startingID = 1; // this is the ID with which the current query starts
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


        public URLGetter(String filepath) {
            logger.setLevel(Level.FINEST);

            this.filepath = filepath;

            mapper = new ObjectMapper();
        }


        public void run() {

            try {
                bufferedWriter = new BufferedWriter(new FileWriter(new File(filepath)));
                bufferedWriter.write(ExpandedWikiaItem.getHeader() + "\n"); // just the header line for the CSV file
            } catch (IOException e1) {
                logger.severe(Thread.currentThread().getName() + ": The provided path for the file does not work.");
                e1.printStackTrace();
                return;
            }

            while (startingID <= UPPER_LIMIT) {

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
                    logger.info(Thread.currentThread().getName() + ": Number of Wikis: " + ids.length + " in iteration: " + numberOfIterations++);

                    for (String id : ids) {
                        // map json string with infos of one wiki to java object
                        ExpandedWikiaItem wiki = mapper.readValue(items.getJSONObject(id).toString(), ExpandedWikiaItem.class);
                        bufferedWriter.write(wiki.toString().replace("\n", "").replace("\r", "") + "\n"); // line breaks have to be deleted

                        logger.info(id);
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
    }

}
