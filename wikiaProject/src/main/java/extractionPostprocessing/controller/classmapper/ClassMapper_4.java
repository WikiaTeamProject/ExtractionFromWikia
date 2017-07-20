package extractionPostprocessing.controller.classmapper;

import extractionPostprocessing.util.DBpediaResourceServiceOffline;

/**
 * Mapping of templates to classes.
 * Algorithm:
 * - check whether the Template is an Infobox Template (else: map to <null>)
 * - if there is an ontology with the same name -> map it (else: map to <null>)
 */
public class ClassMapper_4 extends ClassMapper{

    @Override
    public String mapSingleClass(String templateToMap) {

        String lookupOntology = this.transformTemplateToOntology(templateToMap, false);
        DBpediaResourceServiceOffline dbPediaService = DBpediaResourceServiceOffline.getDBpediaResourceServiceOfflineObject();

        if (dbPediaService.ontologyClassExistInDBpedia(lookupOntology)) {
            return lookupOntology;
        } else {
            return "<null>";
        }

    }
}
