package extractionPostprocessing.controller.propertymapper;

import extractionPostprocessing.util.DBpediaResourceServiceOffline;
/**
 * Created by Samresh Kumar on 7/15/2017.
 */
public class PropertyMapper_3 extends PropertyMapper {

    @Override
    public String mapSingleProperty(String propertyToMap) {

        //Equivalent Ontology class
        String ontologyClass=propertyToMap.toLowerCase().replace("/property/","/ontology/");

        if(DBpediaResourceServiceOffline.getDBpediaResourceServiceOfflineObject().
                ontologyClassExistInDBpedia(ontologyClass)){
            return ontologyClass;

        }

        else return null;
    }
}
