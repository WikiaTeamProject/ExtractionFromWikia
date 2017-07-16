package extractionPostprocessing.controller.propertymapper;

import extractionPostprocessing.controller.propertymapper.PropertyMapper;

/**
 * This mapper will replace property in URI
 * to ontology
 */
public class PropertyMapper_1 extends PropertyMapper {

    @Override
    public String mapSingleProperty(String propertyToMap) {
        return propertyToMap.replace("property","ontology");
    }
}
