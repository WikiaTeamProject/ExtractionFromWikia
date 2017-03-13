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
 * Created by D060249 on 13.03.2017.
 */
public class Main {


    public static void main(String[] args) {

        // some delcarations
        InputStream inputStream;
        BufferedReader bReader;


        StringBuffer generatedURL = new StringBuffer("http://www.wikia.com/api/v1/Wikis/Details?ids=1");
        // create URL
        for(int i = 2; i <= 80; i++){
            generatedURL.append("%2C+" + Integer.toString(i));
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

            System.out.println(completeResponse);
            JSONObject jsonResponse = new JSONObject(completeResponse.toString());

            for (int i = 1; i < 100; i++) {
                try {
                    System.out.println(jsonResponse.getJSONObject("items").getJSONObject(Integer.toString(i)).get("id"));
                } catch (JSONException jsone) {
                    // id not found → simply continue
                    continue;
                }
            }

        } catch (MalformedURLException mue) {
            System.out.println(mue);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
