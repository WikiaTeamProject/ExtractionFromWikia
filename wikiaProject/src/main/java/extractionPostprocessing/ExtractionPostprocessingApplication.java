package extractionPostprocessing;

import utils.ExtractionBz2;

/**
 * Created by Jan Portisch on 29.04.2017.
 */
public class ExtractionPostprocessingApplication {

    public static void main(String[] args) {

        String testFile = "C:\\Users\\D060249\\Desktop\\TMP\\GoT_Wikia_Dump\\enwiki\\20170331\\enwiki-20170331-article-templates-nested.ttl.bz2";
        String newFile = "C:\\Users\\D060249\\Desktop\\enwiki-20170331-article-templates-nested.ttl";
        ExtractionBz2.extract(testFile, newFile);
    }

}
