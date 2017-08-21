package applications.extractionPostprocessing.controller.resourcemapper;

import utils.IOoperations;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Abstract class fro resource mappers.
 * Note that only one method is to be implemented: mapSingleResource(String resourceToMap);
 */
public abstract class ResourceMapper {

    /**
     * Maps a single resource. Accepts a dbpedia tag and will map that to the actual dbpedia tag.
     * @param resourceToMap DBpedia tag from wiki to be mapped. Example: "<http://dbpedia.org/resource/Stomp>"
     * @return DBpedia resource.
     */
    public abstract String mapSingleResource(String resourceToMap);


    /**
     * Returns all resource mappings of a wiki.
     * @param targetNamespace The target namespace.
     * @param resourcesToMap An array list of all the resources to be mapped (in dbpedia tag format, i.e. the domain is not yet replaced).
     * @return A hashmap of the form: key = <targetnamespace_resource> value = <dbpedia_resource>
     */
    public HashMap<String, String> getResourceMappings(String targetNamespace, HashSet<String> resourcesToMap) {

        HashMap<String, String> result = new HashMap<String, String>();

        for(String resource : resourcesToMap){
            result.put(resource.replace("dbpedia.org", targetNamespace), mapSingleResource(resource));
        }
        return result;
    }


    /**
     * Creates the resources mapping file.
     * @param directory Directory in which the mapping file shall be created.
     * @param targetNamespace The target namespace that shall be used.
     * @param resourcesToMap An array list of the resources for which the mapping shall be created.
     */
    public void writeResourceMappingsFile(File directory, String targetNamespace, HashSet<String> resourcesToMap, boolean includeNullMappings) {
        IOoperations.writeMappingContentsToFile(getResourceMappings(targetNamespace, resourcesToMap),
                new File(directory.getAbsolutePath() + "/resourceMappings.ttl"),
                "<http://www.w3.org/2002/07/owl#sameAs>", includeNullMappings);
    }

}
