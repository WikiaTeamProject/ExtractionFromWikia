package extractionPostprocessing.controller;

import extractionPostprocessing.model.ResourceMapper;
import extractionPostprocessing.model.SPARQLresult;

/**
 * Fourth mapper implementation.
 * - automatically maps files to <null>
 * - checks whether a resource exists before mapping it (using HashMap lookup)
 * - does not map lists
 */
public class ResourceMapper_4_1 extends ResourceMapper{

    // TODO: Implement
    @Override
    public String mapSingleResource(String resourceToMap) {
        if(resourceToMap.contains("/File:")){
            return "<null>";
        } else {
            SPARQLresult result = DBpediaResourceService.getResourceAndRedirectInDBpedia(resourceToMap);

            if(result.resourceExists){
                if(result.redirectResource != null){
                    // redirect source found
                    if(
                            result.redirectResource.toLowerCase().contains("list_") || result.redirectResource.toLowerCase().contains("places_")
                            ) {
                        //-> the redirect resource is likely an enumeration of other resources; do not link to it
                        return "<null>";
                    } else {
                        return result.redirectResource;
                    }
                } else {
                    // -> no redirect resource -> use dbPediaResource
                    return(resourceToMap);
                }
            } else {
                // -> resource does not exist -> map to <null>
                return "<null>";
            }
        }
    }

}
