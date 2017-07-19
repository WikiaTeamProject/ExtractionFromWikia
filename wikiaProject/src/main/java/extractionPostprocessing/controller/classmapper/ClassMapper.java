package extractionPostprocessing.controller.classmapper;

import utils.IOoperations;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * Abstract class for property mappers.
 * Note that only one method is to be implemented: mapSingleProperty(String resourceToMap);
 */
public abstract class ClassMapper {

    /**
     * Maps a single resource. Accepts a dbpedia tag and will map that to the actual DBpedia tag.
     *
     * @param classToMap DBpedia tag from wiki to be mapped. Example: "<http://dbpedia.org/resource/Template:Speculation>"
     * @return DBpedia resource.
     */
    public abstract String mapSingleClass(String classToMap);


    /**
     * Returns all resource mappings of a wiki.
     *
     * @param targetNamespace The target namespace.
     * @param classesToMap    An array list of all the classes to be mapped (in DBpedia tag format, i.e. the domain is not yet replaced).
     * @return A HashMap of the form: key = <targetnamespace_resource> value = <dbpedia_resource>
     */
    public HashMap<String, String> getClassMappings(String targetNamespace, HashSet<String> classesToMap) {

        HashMap<String, String> result = new HashMap<String, String>();

        for (String resource : classesToMap) {
            result.put(resource.replace("dbpedia.org", targetNamespace), mapSingleClass(resource));
        }
        return result;
    }


    /**
     * Creates the resources mapping file.
     *
     * @param directory       Directory in which the mapping file shall be created.
     * @param targetNamespace The target namespace that shall be used.
     * @param classesToMap    An array list of the classes for which the mapping shall be created.
     */
    public void writeClassMappingsFile(File directory, String targetNamespace, HashSet<String> classesToMap) {
        IOoperations.writeMappingContentsToFile(getClassMappings(targetNamespace, classesToMap), new File(directory.getAbsolutePath() + "/classMappings.ttl"));
    }


    /**
     * This method transforms a template entity into a class entity.
     * Example:
     * Input: <http://uni-mannheim.de/HarryPotter/resource/Template:Creature_infobox>
     * Output: <http://uni-mannheim.de/HarryPotter/class/Creature>
     *
     * @param templateToTransform
     * @return A string representing a class.
     */
    public String performClassTransformation(String templateToTransform, boolean replaceNamespace) {

        String transformedTemplate = templateToTransform;


        if (replaceNamespace) {
            // check whether entity is already transformed to target namespace
            String targetNamespace = ResourceBundle.getBundle("config").getString("targetnamespace");
            if (!templateToTransform.contains(targetNamespace) && templateToTransform.contains("dbpedia.org")) {
                // transform into target namespace
                transformedTemplate = templateToTransform.replaceAll("dbpedia.org", targetNamespace);
            }
        }

        // transform into class
        transformedTemplate = transformedTemplate.replace("/resource/Template:", "/class/");

        // remove infobox information
        transformedTemplate = transformedTemplate.replace("infobox_", "");
        transformedTemplate = transformedTemplate.replace("_infobox", "");
        transformedTemplate = transformedTemplate.replace("Infobox_", "");
        transformedTemplate = transformedTemplate.replace("_Infobox", "");

        // transform first character of class name to uppercase
        transformedTemplate = transformedTemplate.substring(0, transformedTemplate.indexOf("/class/") + 7)
                + transformedTemplate.substring(transformedTemplate.indexOf("/class/") + 7).substring(0, 1).toUpperCase()
                + transformedTemplate.substring(transformedTemplate.indexOf("/class/") + 7).substring(1);

        return transformedTemplate;
    }

    /**
     * Convenience Overload
     *
     * @param templateToTransform
     * @return
     */
    public String performClassTransformation(String templateToTransform) {
        return performClassTransformation(templateToTransform, true);
    }


    /**
     * This method transforms a template entity into a class entity.
     * Example:
     * Input: <http://dbpedia.org/resource/Template:Speculation>
     * Output: <http://dbpedia.org/ontology/Speculation>
     *
     * @param templateToTransform
     * @return A string representing a class.
     */
    public String transformTemplateToOntology(String templateToTransform) {

        String transformedTemplate = templateToTransform;

        // transform into ontology
        transformedTemplate = transformedTemplate.replace("/resource/Template:", "/ontology/");

        // remove infobox information
        transformedTemplate = transformedTemplate.replace("infobox_", "");
        transformedTemplate = transformedTemplate.replace("_infobox", "");
        transformedTemplate = transformedTemplate.replace("Infobox_", "");
        transformedTemplate = transformedTemplate.replace("_Infobox", "");

        // transform first character of class name to uppercase
        transformedTemplate = transformedTemplate.substring(0, transformedTemplate.indexOf("/ontology/") + 10)
                + transformedTemplate.substring(transformedTemplate.indexOf("/ontology/") + 10).substring(0, 1).toUpperCase()
                + transformedTemplate.substring(transformedTemplate.indexOf("/ontology/") + 10).substring(1);
        return transformedTemplate;
    }


}


