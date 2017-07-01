package extractionPostprocessing.model;

import java.io.File;
import java.util.HashSet;

/**
 * Interface for mappers that map properties to DBpedia properties.
 */
public interface PropertyMapperInterface {

    HashSet<String> createPropertyMappings(File directory, HashSet resourcesToMap);


}
