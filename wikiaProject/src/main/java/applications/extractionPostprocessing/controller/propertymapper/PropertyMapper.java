package applications.extractionPostprocessing.controller.propertymapper;

import utils.IOoperations;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Abstract class for property mappers.
 * Note that only one method is to be implemented: mapSingleProperty(String resourceToMap);
 */
public abstract class PropertyMapper {


    /**
     * Map a single property
     * @param propertyToMap Example property:
     * @return
     */
    public abstract String mapSingleProperty(String propertyToMap);


    /**
     * Returns all resource mappings of a wiki.
     * @param targetNamespace The target namespace.
     * @param propertiesToMap An array list of all the resources to be mapped (in dbpedia tag format, i.e. the domain is not yet replaced).
     *                        Example: "<http://dbpedia.org/property/type>"
     * @return A hasmap of the form: key = <targetnamespace_resource> value = <dbpedia_resource>
     */
    public HashMap<String, String> getPropertyMappings(String targetNamespace, HashSet<String> propertiesToMap) {

        HashMap<String, String> result = new HashMap<String, String>();

        for(String resource : propertiesToMap){
            result.put(resource.replace("dbpedia.org", targetNamespace), mapSingleProperty(resource));
        }
        return result;
    }


    /**
     * Creates the resources mapping file.
     * @param directory Directory in which the mapping file shall be created.
     * @param targetNamespace The target namespace that shall be used.
     * @param propertiesToMap An array list of the properties for which the mapping shall be created.
     */
    public void writePropertiesMappingsFile(File directory, String targetNamespace, HashSet<String> propertiesToMap, boolean includeNullMappings) {
        IOoperations.writeMappingContentsToFile(getPropertyMappings(targetNamespace, propertiesToMap),
                new File(directory.getAbsolutePath() + "/propertyMappings.ttl"),
                "<http://www.w3.org/2002/07/owl#equivalentProperty>", includeNullMappings);
    }

}
