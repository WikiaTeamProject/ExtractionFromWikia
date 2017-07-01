package extractionPostprocessing.controller;

import extractionPostprocessing.model.PropertyMapper;

/**
 * Trivial implementation of a properties mapper.
 * - maps all other properties to dbpedia properties (same name)
 */
public class PropertiesMapper_1 extends PropertyMapper{

    @Override
    public String mapSingleProperty(String propertyToMap) {
        return propertyToMap;
    }
}
