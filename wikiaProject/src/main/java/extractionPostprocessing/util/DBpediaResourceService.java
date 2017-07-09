package extractionPostprocessing.util;

import extractionPostprocessing.model.ResourceServiceResult;

/**
 * Abstract class defining the protocol for DBpedia Resource Services.
 */
public abstract class DBpediaResourceService {

    /**
     * Returns whether a resource exists on DBpedia and what the redirect is if there is any.
     * @param resource
     * @return
     */
    public abstract ResourceServiceResult getResourceAndRedirectInDBpedia(String resource);
}
