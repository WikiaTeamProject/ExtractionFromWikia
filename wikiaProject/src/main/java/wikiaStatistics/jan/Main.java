package wikiaStatistics.jan;


import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jan Portisch on 13.03.2017.
 */
public class Main {

    public static final int UPPER_LIMIT = 10000; // the server will be queried up to this number

    public static void main(String[] args) {

        int startingID = 1; // this is the ID with which the current query starts
        int startingIDbefore; // this is the ID with which the previous query started
        int numberOfIterations = 0; // just a counter to calculate how many iterations were completed

        while (startingID <= UPPER_LIMIT) {

            // some delcarations
            InputStream inputStream;
            BufferedReader bReader;

            // create URL
            StringBuffer generatedURL = new StringBuffer("http://www.wikia.com/api/v1/Wikis/Details?ids=" + startingID);

            startingID++; // increment startingID because the first ID was already used in the initial URL creation
            startingIDbefore = startingID;

            // there will always be 50 IDs in one query
            while(startingID < startingIDbefore + 49) {
                generatedURL.append("%2C+" + Integer.toString(startingID));
                startingID++;
            }

            try {

                URL url = new URL(generatedURL.toString());
                URLConnection urlConnection = url.openConnection();

                inputStream = urlConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bReader = new BufferedReader(inputStreamReader);

                String responseLine;
                StringBuffer completeResponse = new StringBuffer();

                // read line by line and save in completeResponse
                while (((responseLine = bReader.readLine()) != null)) {
                    completeResponse.append(responseLine);
                }

                // get the JSON OBJECT
                JSONObject jsonResponse = new JSONObject(completeResponse.toString());

                // loop through the current JSON Object and look for element "id" in Object "items"
                for (int i = startingIDbefore - 1; i < startingIDbefore + 50; i++) {
                    try {
                        System.out.println(jsonResponse.getJSONObject("items").getJSONObject(Integer.toString(i)).get("id"));
                    } catch (JSONException jsone) {
                        // id not found → simply continue
                        continue;
                    }
                }
                System.out.println("Number of Iterations: " + ++numberOfIterations);
                System.out.println("StartingID: " + startingID);
            } catch (MalformedURLException mue) {
                System.out.println(mue);
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
    }
}
