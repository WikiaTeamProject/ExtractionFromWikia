package applications.extractionPostprocessing.controller.propertymapper;

/**
 * This mapper will replace property in URI
 * to ontology
 */
@Deprecated
public class PropertyMapper_1 extends PropertyMapper {

    @Override
    public String mapSingleProperty(String propertyToMap) {
        return propertyToMap.replace("property","ontology");
    }
}
