package extractionPostprocessing.controller.resourcemapper;

import extractionPostprocessing.util.DBpediaResourceServiceOnline;
import extractionPostprocessing.model.SPARQLresult;

/**
 * Third mapper implementation.
 * - automatically maps files to <null>
 * - checks whether a resource exists on DBpedia before mapping it (using HashMap lookup)
 */
public class ResourceMapper_3_1 extends ResourceMapper{

    //TODO: Implement
    @Override
    public String mapSingleResource(String resourceToMap) {
        if(resourceToMap.contains("/File:")){
            return "<null>";
        } else {

            // initialize

            SPARQLresult result = DBpediaResourceServiceOnline.getResourceAndRedirectInDBpedia(resourceToMap);

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
