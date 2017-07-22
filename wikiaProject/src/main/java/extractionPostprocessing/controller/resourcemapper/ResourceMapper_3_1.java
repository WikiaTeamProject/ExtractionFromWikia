package extractionPostprocessing.controller.resourcemapper;

import extractionPostprocessing.util.DBpediaResourceServiceOffline;
import extractionPostprocessing.model.ResourceServiceResult;

/**
 * Third mapper implementation.
 * - automatically maps files to <null>
 * - checks whether a resource exists on DBpedia before mapping it (using HashMap lookup)
 */
public class ResourceMapper_3_1 extends ResourceMapper{

    @Override
    public String mapSingleResource(String resourceToMap) {
        if(resourceToMap.contains("/File:")){
            return "<null>";
        } else {
            DBpediaResourceServiceOffline service = DBpediaResourceServiceOffline.getDBpediaResourceServiceOfflineObject();
            ResourceServiceResult result = service.getResourceAndRedirectInDBpedia(resourceToMap);

            if(result.resourceExists){
                if(result.redirectResource != null){
                    // redirect source found
                    return result.redirectResource;
                } else {
                    // -> no redirect resource -> use dbPediaResource
                    return(service.getResourceCorrectCase(resourceToMap));
                }
            } else {
                // -> resource does not exist -> map to <null>
                return "<null>";
            }
        }
    }

}
