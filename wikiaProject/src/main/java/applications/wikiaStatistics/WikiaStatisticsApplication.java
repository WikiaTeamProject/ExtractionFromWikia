package applications.wikiaStatistics;

import applications.wikiaStatistics.controller.MetadataThreadImpl;


public class WikiaStatisticsApplication {

    public static void main(String[] args) {

        if (MetadataThreadImpl.checkPrerequisites()) {
            // metadata will be downloaded and files saved in the specified directory
            MetadataThreadImpl.downloadWikiaMetadata();
        }

        // statistics of the extracted wikis will be performed
       // MetadataStatistics statistics = WikiaStatisticsTools.getMetadataStatistics();

        // limit statistics to only view languages with more than 1000 occurrences
//        statistics.limitTopLanguages(1000);
//        System.out.println(statistics);

    }

}
