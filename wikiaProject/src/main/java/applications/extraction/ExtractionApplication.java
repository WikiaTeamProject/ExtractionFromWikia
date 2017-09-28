package applications.extraction;

/**
 *
 * This class will perform the extraction of the previously downloaded wikis.
 *
 * Prerequisites
 * - The DBpedia framework (lib/dbpedia-extraction-framework) was successfully built.
 * - The wikia wikis that shall be extracted were already downloaded and can be found in
 *   /<root>/downloadedWikis/7z and /<root>/downloadedWikis/bz
 *
 */
public class ExtractionApplication {

    public static void main(String[] args) {

        Extractor extractor = new Extractor();
        //extract all wikis
        extractor.extractAllWikis();
    }
}
