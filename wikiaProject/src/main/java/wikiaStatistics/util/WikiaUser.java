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

/**
 * A class to response Wikia User Entity
 */
public class WikiaUser{

    private string userName;
    private string userPassword;
    private static Logger logger = Logger.getLogger(WikiaStatisticsTools.class.getName());

    public wikiaUser(string userName,string userPassword){
        this.userName=userName;
        this.userPassword=userPassword;
    }

    public string getUserName(){
        return this.userName;
    }

    public string getUserPassword(){
        return this.userPassword;
    }

    public void setUserName(string userName){
        this.userName=userName;
    }

    public void setUserPassword(string userPassword){
       this.userPassword=userPassword;
    }

    public String getAccessToken(){

        string accessTokenURL=ResourceBundle.getBundle("config").getString("https://services.wikia.com/auth/token");
        string responseMessage="";
        string responseMessageLine="";
        string userAccessToken="";
        URL accessTokenRequestURL=null;
        URLConnection accessTokenRequestConnection=null;
        OutputStreamWriter accessTokenRequestMessageWriter=null;
        BufferedReader responseMessageReader=null;

        try {

            //Intialize URL
            accessTokenRequestURL = new URL(accessTokenURL);

            //get connection
            accessTokenRequestConnection = url.openConnection();

            accessTokenRequestConnection.setDoOutput(true);

            accessTokenRequestMessageWriter = new OutputStreamWriter(accessTokenRequestConnection.getOutputStream());
            accessTokenRequestMessageWriter.write("username=" + this.userName + "&password=" + this.userPassword);
            accessTokenRequestMessageWriter.flush();

            responseMessageReader= new BufferedReader(new InputStreamReader(accessTokenRequestConnection.getInputStream()));

            while ((responseMessageLine = reader.readLine()) != null) {

                //append each line to response message
                responseMessage+= responseMessageLine;
            }

            //parse response message to get access token
            userAccessToken = responseMessage.substring(responseMessage.indexOf(":") + 2, responseMessage.indexOf(",") - 1);


            //close reader and writer objects
            accessTokenRequestMessageWriter.close();
            responseMessageReader.close();
        }
        catch(Exception ex) {
            logger.severe(ex.toString());
        }

        return access_token;
    }
}