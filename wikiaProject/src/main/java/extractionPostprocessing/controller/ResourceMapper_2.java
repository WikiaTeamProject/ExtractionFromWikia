package extractionPostprocessing.controller;

import extractionPostprocessing.model.ResourceMapper;
import extractionPostprocessing.model.ResourceMapperInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.ResourceBundle;

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
