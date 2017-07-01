package extractionPostprocessing.controller;

import extractionPostprocessing.model.ResourceMapper;
import extractionPostprocessing.model.ResourceMapperInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.ResourceBundle;

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
