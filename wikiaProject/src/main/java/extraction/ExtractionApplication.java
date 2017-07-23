package extraction;

/**
 *
 * This class will perform the extraction of the previously downloaded wikis.
 *
 * Prerequisites
 * - DBpedia Extraction Framework is available on the machine.
 *   @see <a href="https://github.com/dbpedia/extraction-framework">https://github.com/dbpedia/extraction-framework</a>
 * - The DBpedia extraction framework was successfully built.
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
