package applications.wikiaDumpRequester.util;

import loggingService.MessageLogger;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Level;


public class WikiaNewDumpRequest {

    private static MessageLogger logger=new MessageLogger();
    private static final String MODULE="wikiDumpRequester";
    private static final String CLASS=WikiaNewDumpRequest.class.getName();

    /**
     * This method requests a dump for the given URL.
     * @param accessToken : access token received from Wikia for respective access credentials
     * @param WikiaURL : URL of Wikia Wiki Statistics page , e.g : http://gameofthrones.wikia.com/wiki/Special:Statistics
     */
    public void RequestNewWikiaDump(String accessToken,String WikiaURL) throws IOException{
        try {
            //Initialize URL object
            URL url = new URL(WikiaURL);

            HttpURLConnection newDumpRequestConnection = (HttpURLConnection) url.openConnection();
            newDumpRequestConnection.setDoOutput(true);
            newDumpRequestConnection.setRequestMethod("POST");

            //Pass access token as cookie in HTTP request
            newDumpRequestConnection.setRequestProperty("Cookie", "access_token=" + accessToken);

            //Get response stream
            OutputStreamWriter requestMessage = new OutputStreamWriter(newDumpRequestConnection.getOutputStream());


                /*
               // This code is writtern to debug response message.
                String line="";
                BufferedReader reader;
                reader = new
                    BufferedReader(new InputStreamReader(newDumpRequestConnection.getInputStream()));

                 while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                reader.close();
                //debug code ends here
                */

            logger.logMessage(Level.INFO,MODULE,CLASS,"HTTP Response Code : " + newDumpRequestConnection.getResponseCode() +
                             "HTTP Response Message :  " + newDumpRequestConnection.getResponseMessage());

            requestMessage.close();
        } catch(MalformedURLException mue){
            logger.logMessage(Level.FATAL,MODULE,CLASS,mue.toString());
        }
    }

}

