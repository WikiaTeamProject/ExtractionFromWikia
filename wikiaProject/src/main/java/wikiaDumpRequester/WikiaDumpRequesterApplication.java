package wikiaDumpRequester;
import wikiaDumpRequester.controller.WikiaDumpRequesterExecutor;

/**
 * This program will request dumps for all wikis on wikia.
 * Note that the program can be executed at the moment but has no effect because wikia changed the rules.
 */
public class WikiaDumpRequesterApplication {


    public static void main(String[] args) {

        WikiaDumpRequesterExecutor.requestDumpsForAllWikis(51000); // +185,800

    }


}
