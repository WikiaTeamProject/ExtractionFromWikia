package extractionPostprocessing.model;

import utils.FileOperations;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Abstract class for property mappers.
 * Note that only one method is to be implemented: mapSingleProperty(String resourceToMap);
 */
public abstract class ClassMapper {


    /**
     * Maps a single resource. Accepts a dbpedia tag and will map that to the actual dbpedia tag.
     * @param classToMap DBpedia tag from wiki to be mapped.
     * @return DBpedia resource.
     */
    public abstract String mapSingleClass(String classToMap);


    /**
     * Returns all resource mappings of a wiki.
     * @param targetNamespace The target namespace.
     * @param classesToMap An array list of all the classes to be mapped (in DBpedia tag format, i.e. the domain is not yet replaced).
     * @return A hashmap of the form: key = <targetnamespace_resource> value = <dbpedia_resource>
     */
    public HashMap<String, String> getClassMappings(String targetNamespace, HashSet<String> classesToMap) {

        HashMap<String, String> result = new HashMap<String, String>();

        for(String resource : classesToMap){
            result.put(resource.replace("dbpedia.org", targetNamespace), mapSingleClass(resource));
        }
        return result;
    }


    /**
     * Creates the resources mapping file.
     * @param directory Directory in which the mapping file shall be created.
     * @param targetNamespace The target namespace that shall be used.
     * @param classesToMap An array list of the classes for which the mapping shall be created.
     */
    public void writeClassMappingsFile(File directory, String targetNamespace, HashSet<String> classesToMap) {
        FileOperations.writeMappingContentsToFile(getClassMappings(targetNamespace, classesToMap), new File(directory.getAbsolutePath() + "/classMappings.ttl"));
    }



}


