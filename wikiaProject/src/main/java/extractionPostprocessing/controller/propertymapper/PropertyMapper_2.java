package extractionPostprocessing.controller.propertymapper;

import extractionPostprocessing.util.DBpediaResourceServiceOffline;

/**
 * This mapper maps properties.
 * It looks up whether the property exists in dbpedia and maps to it if it exists.
 * Otherwise it maps to <null>.
 */
public class PropertyMapper_2 extends PropertyMapper {

    @Override
    public String mapSingleProperty(String propertyToMap) {

        //Equivalent Ontology class
        String ontologyClass = propertyToMap.toLowerCase().replace("/property/","/ontology/");

        DBpediaResourceServiceOffline service = DBpediaResourceServiceOffline.getDBpediaResourceServiceOfflineObject();

        if(service.ontologyClassExistInDBpedia(ontologyClass)){
            return service.getOntologyClassIgnoreCase(ontologyClass);
        }

        else return null;
    }
}
