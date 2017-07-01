package extractionPostprocessing.controller;

import extractionPostprocessing.model.ResourceMapper;


/**
 * First, relatively simple, implementation of a mapper.
 * - maps all other resources to dbpedia resources (same name)
 */
public class ResourceMapper_1 extends ResourceMapper{

    @Override
    public String mapSingleResource(String resourceToMap) {
        return resourceToMap;
    }

}
