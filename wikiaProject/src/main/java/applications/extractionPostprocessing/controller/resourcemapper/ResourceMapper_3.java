package applications.extractionPostprocessing.controller.resourcemapper;

import applications.extractionPostprocessing.util.DBpediaResourceServiceOnline;
import applications.extractionPostprocessing.model.ResourceServiceResult;


/**
 * Third mapper implementation.
 * - automatically maps files to <null>
 * - checks whether a resource exists on DBpedia before mapping it (using SPARQL)
 */
public class ResourceMapper_3 extends ResourceMapper {

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
                    return result.redirectResource;
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
