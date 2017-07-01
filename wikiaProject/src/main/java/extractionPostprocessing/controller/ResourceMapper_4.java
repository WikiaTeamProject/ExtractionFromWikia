package extractionPostprocessing.controller;

import extractionPostprocessing.model.ResourceMapper;
import extractionPostprocessing.model.ResourceMapperInterface;
import extractionPostprocessing.model.SPARQLresult;
import utils.FileOperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Third mapper implementation.
 * - automatically maps files to <null>
 * - checks whether a resource exists before mapping it
 * - does not map lists
 */
public class ResourceMapper_4 extends ResourceMapper {

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
