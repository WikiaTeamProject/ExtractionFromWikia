package applications.wikiaDumpRequester.controller;

import loggingService.MessageLogger;
import utils.IOoperations;
import applications.wikiaDumpRequester.model.WikiaUser;
import applications.wikiaDumpRequester.util.WikiaNewDumpRequest;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Level;


public class WikiaDumpRequesterExecutor {

    private static MessageLogger logger=new MessageLogger();
    private static final String MODULE="wikiDumpRequester";
    private static final String CLASS=WikiaDumpRequesterExecutor.class.getName();
    private static ArrayList<String> urlsThatDidNotWork;


    /**
     * Request dumps for all wikis on wikia.
     */
    public static void requestDumpsForAllWikis(){
        requestDumpsForAllWikis(0);
    }

    /**
     * Request a dump from wikia.
     * @param beginAtLine Line of URL list where the requesting shall start.
     */
    public static void requestDumpsForAllWikis(int beginAtLine) {

        WikiaUser user = new WikiaUser(ResourceBundle.getBundle("credentials").getString("username"),
                         ResourceBundle.getBundle("credentials").getString("password"));
        String filepath = ResourceBundle.getBundle("credentials").getString("directory") + "/wikiaAllOverview.csv";

        // user output
        logger.logMessage(Level.INFO,MODULE,CLASS,"Credentials:\nUsername: " + ResourceBundle.getBundle("credentials").getString("username")
                         + "\nPassword: " + ResourceBundle.getBundle("credentials").getString("password"));

        WikiaNewDumpRequest requester = new WikiaNewDumpRequest();
        ArrayList<String> urls = IOoperations.getUrls(filepath);

        String token = user.getAccessToken();
        logger.logMessage(Level.INFO,MODULE,CLASS,"Token: " + token);

        urlsThatDidNotWork = new ArrayList<String>();
        int numberOfWikisProcessed = 0; // number of URLs that were processed

        int index = Math.max(beginAtLine - 1, 0); // subtract 1 because first entry of array list is 0

        while (index < urls.size()) {
            String url = urls.get(index);
            System.out.println(url);

            try {
                requester.RequestNewWikiaDump(token, url);
                System.out.println("Number of Wikis processed: " + ++numberOfWikisProcessed);
                try {
                    // wait for some milliseconds to avoid connection errors:
                    // TODO:I do not know whether we need that or whether it helps!
                    Thread.sleep(25);
                } catch (InterruptedException ie) {
                    logger.logMessage(Level.FATAL,MODULE,CLASS,ie.toString());
                }

            } catch (ConnectException ce) {
                logger.logMessage(Level.FATAL,MODULE,CLASS,"A ConnectException occurred. It will be handled...");
                logger.logMessage(Level.INFO,MODULE,CLASS,"Wait for a few seconds.");

                // not sure whether we need re-instantiation
                user = null;
                requester = null;

                try {
                    // wait for one minute:
                    // TODO: evaulate whether shorter time is more appropriate!
                    Thread.sleep(60000);

                } catch (InterruptedException ie) {
                    logger.logMessage(Level.FATAL,MODULE,CLASS,ie.toString());
                }

                // re-instantiate objects, I am not sure, whether this helps
                user = new WikiaUser(ResourceBundle.getBundle("credentials").getString("username"),
                                  ResourceBundle.getBundle("credentials").getString("password"));
                requester = new WikiaNewDumpRequest();

                logger.logMessage(Level.INFO,MODULE,CLASS,"Request a new access token...");
                token = user.getAccessToken();

                logger.logMessage(Level.INFO,MODULE,CLASS,"Continue with next URL.");
                urlsThatDidNotWork.add(url);
            } catch (IOException ioe){
                logger.logMessage(Level.FATAL,MODULE,CLASS,ioe.toString());
            }

            index++;
        } // end of loop through URLs

        logger.logMessage(Level.INFO,MODULE,CLASS,"Tokens for all URLs requested.");
        System.out.println("URLS that did not work:");
        for(String s : urlsThatDidNotWork){
            System.out.println(s);
        }
    }
}
