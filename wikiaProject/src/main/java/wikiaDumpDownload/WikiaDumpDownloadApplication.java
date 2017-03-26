package wikiaDumpDownload;


import wikiaDumpDownload.controller.WikiaDumpDownloadThreadImpl;

/**
 * This class represents the application to download dumps of  Wikia wikis.
 *
 * Please specify the directory for the downloaded files in resources > config.properties
 */
public class WikiaDumpDownloadApplication {

    public static void main(String[] args) {

        WikiaDumpDownloadThreadImpl.downloadWikiaDumps();

    }

}