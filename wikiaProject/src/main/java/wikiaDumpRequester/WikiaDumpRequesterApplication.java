package wikiaDumpRequester;
import wikiaDumpRequester.controller.WikiaDumpDownloadExecutor;

/**
 * Created by Samresh Kumar on 4/1/2017.
 */
public class WikiaDumpRequesterApplication {


    public static void main(String[] args) {
        WikiaDumpDownloadExecutor.requestDumpsForAllWikis();
    }


}
