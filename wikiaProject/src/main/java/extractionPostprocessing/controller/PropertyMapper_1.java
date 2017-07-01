package extractionPostprocessing.controller;

import extractionPostprocessing.model.PropertyMapper;

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
