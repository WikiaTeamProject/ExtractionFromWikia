package extractionPostprocessing.controller.resourcemapper;


/**
 * Second attempt, implementation of a mapper.
 * - maps files to <null>
 * - maps all other resources to dbpedia resources (same name)
 */
public class ResourceMapper_2 extends ResourceMapper {


    @Override
    public String mapSingleResource(String resourceToMap) {
        if(resourceToMap.contains("/File:")){
            return "<null>";
        } else {
            return resourceToMap;
        }
    }

}
