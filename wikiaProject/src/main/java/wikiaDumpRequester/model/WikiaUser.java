package wikiaDumpRequester.model;

import wikiaStatistics.util.WikiaStatisticsTools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * A class to represent Wikia User Entity
 */
public class WikiaUser {

    private String userName;
    private String userPassword;
    private static Logger logger = Logger.getLogger(WikiaStatisticsTools.class.getName());

    public WikiaUser(String userName, String userPassword){
        this.userName=userName;
        this.userPassword=userPassword;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getUserPassword(){
        return this.userPassword;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }

    public void setUserPassword(String userPassword){
       this.userPassword=userPassword;
    }

    public String getAccessToken(){

        String accessTokenURL=ResourceBundle.getBundle("config").getString("accessTokenURL");
        String responseMessage="";
        String responseMessageLine="";
        String userAccessToken="";
        URL accessTokenRequestURL=null;
        URLConnection accessTokenRequestConnection=null;
        OutputStreamWriter accessTokenRequestMessageWriter=null;
        BufferedReader responseMessageReader=null;

        try {

            //Intialize URL
            accessTokenRequestURL = new URL(accessTokenURL);

            //get connection
            accessTokenRequestConnection = accessTokenRequestURL.openConnection();

            accessTokenRequestConnection.setDoOutput(true);

            accessTokenRequestMessageWriter = new OutputStreamWriter(accessTokenRequestConnection.getOutputStream());
            accessTokenRequestMessageWriter.write("username=" + this.userName + "&password=" + this.userPassword);
            accessTokenRequestMessageWriter.flush();

            responseMessageReader= new BufferedReader(new InputStreamReader(accessTokenRequestConnection.getInputStream()));

            while ((responseMessageLine = responseMessageReader.readLine()) != null) {

                //append each line to response message
                responseMessage+= responseMessageLine;
            }

            logger.info(responseMessage);

            //parse response message to get access token
            userAccessToken = responseMessage.substring(responseMessage.indexOf(":") + 2, responseMessage.indexOf(",") - 1);


            //close reader and writer objects
            accessTokenRequestMessageWriter.close();
            responseMessageReader.close();
        }
        catch(Exception ex) {
            logger.severe(ex.toString());
        }

        return userAccessToken;
    }
}