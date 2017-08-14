package applications.extractionPostprocessing.controller.resourcemapper;

import applications.extractionPostprocessing.util.DBpediaResourceServiceOnline;
import applications.extractionPostprocessing.model.ResourceServiceResult;

/**
 * Fourth mapper implementation.
 * - automatically maps files to <null>
 * - checks whether a resource exists before mapping it (using SPARQL)
 * - does not map lists
 */
public class ResourceMapper_4 extends ResourceMapper {

    @Override
    public String mapSingleResource(String resourceToMap) {
        if(resourceToMap.contains("/File:")){
            return "<null>";
        } else {
            DBpediaResourceServiceOnline service = new DBpediaResourceServiceOnline();
            ResourceServiceResult result = service.getResourceAndRedirectInDBpedia(resourceToMap);

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
