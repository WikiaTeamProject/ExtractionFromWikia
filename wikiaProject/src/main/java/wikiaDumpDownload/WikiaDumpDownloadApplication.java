package wikiaDumpDownload;

import wikiaDumpDownload.util.Extraction7zip;

/**
 * This class represents the application to download dumps of Wikia wikis.
 *
 * Please specify the directory for the downloaded files in resources > config.properties
 * Before running this code you have to run wikiaStatisticsApplication to retrieve a list of all wikis.
 *
 * You can either enter an amount of dumps to download or download all available wikis.
 *
 * If needed, you can unzip all downloaded 7zip files.
 */
public class WikiaDumpDownloadApplication {

    public static void main(String[] args) {

//        WikiaDumpDownloadThreadImpl.downloadWikiaDumps();

        Extraction7zip extractor = new Extraction7zip();
        extractor.extractAll7ZipFiles();

    }

}