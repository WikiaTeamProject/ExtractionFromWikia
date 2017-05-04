package extraction;

import java.util.logging.Logger;

/**
 *
 * This class will perform the extraction of the previously downloaded wikis.
 *
 * Prerequisites
 * - DBpedia Extraction Framework is available on the machine.
 *   @see <a href="https://github.com/dbpedia/extraction-framework">https://github.com/dbpedia/extraction-framework</a>
 * - The DBpedia extraction framework was successfully built.
 * - The wikia wikis that shall be extracted were already downloaded and can be found in
 *   /src/main/resources/files/wikiDumps.downloaded
 *
 * Created by Jan Portisch on 04.05.2017.
 */
public class Extractor {

    private Logger logger = Logger.getLogger(Extractor.class.getName());

    /**
     * There are various prerequisites. To allow for a stable program, the prerequisites are checked in this method.
     * @return
     */
    private boolean checkPrerequisites(){

        // check file

        return true;
    }

}
