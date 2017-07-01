package extractionPostprocessing.model;

import java.io.File;
import java.util.HashSet;

/**
 * Created by D060249 on 01.07.2017.
 */
public interface ClassMapperInterface {

    HashSet<String> createClassMappings(File directory, HashSet resourcesToMap);

}
