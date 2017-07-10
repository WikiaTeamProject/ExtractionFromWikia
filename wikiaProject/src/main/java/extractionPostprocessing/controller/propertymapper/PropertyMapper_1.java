package extractionPostprocessing.controller.propertymapper;

/**
 * Trivial implementation of a properties mapper.
 * - maps all other properties to dbpedia properties (same name)
 */
public class PropertyMapper_1 extends PropertyMapper{

    @Override
    public String mapSingleProperty(String propertyToMap) {
        return propertyToMap;
    }
}
