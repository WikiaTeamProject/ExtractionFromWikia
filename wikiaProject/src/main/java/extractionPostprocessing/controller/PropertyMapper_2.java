package extractionPostprocessing.controller;

import extractionPostprocessing.model.PropertyMapper;

/**
 * Created by Samresh Kumar on 7/5/2017.
 * This mapper will replace property in URI
 * to ontology
 */
public class PropertyMapper_2 extends PropertyMapper {

    @Override
    public String mapSingleProperty(String propertyToMap) {
        return propertyToMap.replace("property","ontology");
    }
}
