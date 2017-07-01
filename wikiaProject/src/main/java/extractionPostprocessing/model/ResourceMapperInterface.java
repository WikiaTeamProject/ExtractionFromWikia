package extractionPostprocessing.model;

import extractionPostprocessing.controller.EntitiesMappingExecutor;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This interface provides a protocol for different mappers.
 * It allows to create multiple mappers, to test them and to compare them.
 * Every mapper must implement this interface.
 */
public interface ResourceMapperInterface {

    void createMappingFileForSingleWiki(File pathToWikiFolder);

    static Logger logger = Logger.getLogger(EntitiesMappingExecutor.class.getName());


    HashSet<String> createResourceMappings(File directory, HashSet resourcesToMap);

    /**
     * Write the mapings file to the disk.
     * @param entitiesMapping A HashSet of Strings, each representing one line to be written to the mapping file.
     */
    static void writeContentsToMappingFile(HashSet<String> entitiesMapping, String directoryPath) {

        String mappingFileName = ResourceBundle.getBundle("config").getString("mappingfilename");
        String filePath = directoryPath + "/" + mappingFileName;
        logger.info("Writing mappings file: " + filePath);
        Iterator<String> entitiesMappingIterator;

        try {
            // Initialize file Writer Objects
            PrintWriter fileWriter = new PrintWriter(filePath, "UTF-8");

            entitiesMappingIterator = entitiesMapping.iterator();

            while (entitiesMappingIterator.hasNext()) {
                // Write content to file
                fileWriter.write(entitiesMappingIterator.next());
            }

            // Close file Writer
            fileWriter.close();

        } catch (Exception exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }
    }

}
