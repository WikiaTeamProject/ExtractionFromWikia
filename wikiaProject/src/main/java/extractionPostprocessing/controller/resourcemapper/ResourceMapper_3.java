package extractionPostprocessing.controller.resourcemapper;

import extractionPostprocessing.controller.DBpediaResourceService;
import extractionPostprocessing.model.SPARQLresult;


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
            SPARQLresult result = DBpediaResourceService.getResourceAndRedirectInDBpedia(resourceToMap);

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
