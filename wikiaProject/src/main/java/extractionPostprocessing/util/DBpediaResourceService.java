package extractionPostprocessing.util;

import extractionPostprocessing.model.SPARQLresult;

/**
 * Abstract class defining the protocol for DBpedia Resource Services.
 */
public abstract class DBpediaResourceService {

    public abstract SPARQLresult getResourceAndRedirectInDBpedia(String resource);
}
