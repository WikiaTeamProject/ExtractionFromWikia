package wikiaStatistics.util;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WikiaNewDumpRequest{

    private static Logger logger = Logger.getLogger(WikiaStatisticsTools.class.getName());

    /**
     * @param accessToken : access token received from Wikia for respective access credentials
     * @param WikiaURL : URL of Wikia Wiki Statistics page , e.g : http://gameofthrones.wikia.com/wiki/Special:Statistics
     */
    public void RequestNewWikiaDump(String accessToken,String WikiaURL){
        try {
                //Initialize URL object
                URL url = new URL(WikiaURL);

                HttpURLConnection newDumpRequestCon = (HttpURLConnection) url.openConnection();
                newDumpRequestCon.setDoOutput(true);
                newDumpRequestCon.setRequestMethod("POST");

                //Pass access token as cookie in HTTP request
                httpCon.setRequestProperty("Cookie", "access_token=" + accessToken);

               //Get response stream
                OutputStreamWriter requestMessage = new OutputStreamWriter(newDumpRequestCon.getOutputStream());


            /*
                This code is writtern to debug response message.

                BufferedReader reader = new BufferedReader(new InputStreamReader(newDumpRequestCon.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    //  access_token+=line;
                }

                reader.close();
                */
                logger.info("HTTP Response Code : " + newDumpRequestCon.getResponseCode() +
                        "HTTP Response Message :  " + newDumpRequestCon.getResponseMessage() );



                requestMessage.close();
        }
        catch(Exception ex){
            logger.severe(ex.toString());
        }
    }

}

