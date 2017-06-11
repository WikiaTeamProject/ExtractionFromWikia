package wikiaDumpRequester;
import wikiaDumpRequester.controller.WikiaDumpRequesterExecutor;

/**
 * Created by Samresh Kumar on 4/1/2017.
 */
public class WikiaDumpRequesterApplication {


    public static void main(String[] args) {
        //System.setProperty("http.proxyHost", "proxy.wdf.sap.corp");
        //System.setProperty("http.proxyPort", "8080");

        WikiaDumpRequesterExecutor.requestDumpsForAllWikis(51000); // +185,800

    }


}
