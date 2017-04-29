package extractionPostprocessing;

import utils.ExtractionBz2;

/**
 * Created by Jan Portisch on 29.04.2017.
 */
public class ExtractionPostprocessingApplication {

    public static void main(String[] args) {


        // Example on how to call the extractor for one file
        /**
        String testFile2 = "C:/Users/D060249/Desktop/TMP/GoT_Wikia_Dump/enwiki/20170331/enwiki-20170331-article-templates-nested.ttl.bz2";
        String newFile2 = "C:/Users/D060249/Desktop";
        ExtractionBz2.extract(testFile2, newFile2);
        **/

        // Example on how to call the extractor for multiple files
        /**
        String testFile3 = "C:/Users/D060249/Desktop/TMP/GoT_Wikia_Dump/enwiki/20170331/";
        String newFile3 = "C:/Users/D060249/Desktop/got_wiki";

        ExtractionBz2.extractExtractorResultFiles(testFile3, newFile3);
         **/

    }


}
