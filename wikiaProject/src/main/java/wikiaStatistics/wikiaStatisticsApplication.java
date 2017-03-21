package wikiaStatistics;

import wikiaStatistics.controller.MetadataGetter;
import wikiaStatistics.controller.MetadataGetterImpl;
import wikiaStatistics.model.MetadataStatistics;
import wikiaStatistics.util.WikiaStatisticsTools;

import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.io.*;

/**
 * This class represents the application to download metadata of all Wikia wikis and receive statistics.
 *
 * Please specify the directory for the downloaded files in resources > config.properties
 */
public class wikiaStatisticsApplication {

    public static void main(String[] args) {

        // metadata will be downloaded and files saved in the specified directory
//        MetadataGetterImpl.downloadWikiaMetadata();

        // statistics of the extracted wikis will be performed
        MetadataStatistics statistics = WikiaStatisticsTools.getMetadataStatistics();

        // limit statistics to only view languages with more than 1000 occurrences
        statistics.limitTopLanguages(1000);
        System.out.println(statistics);

    }

}
