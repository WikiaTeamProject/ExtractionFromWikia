package applications.extractionPostprocessing.model;

import java.util.HashSet;

/**
 * A structure class containing information of wikis which shall be mapped.
 */
public class WikiToMap {

    public WikiToMap(String wikiName, HashSet<String> resourcesToMap, HashSet<String> propertiesToMap, HashSet<String> classesToMap){
        this.wikiName = wikiName;
        this.resourcesToMap = resourcesToMap;
        this.propertiesToMap = propertiesToMap;
        this.classesToMap = classesToMap;
    }

    public String wikiName;
    public HashSet<String> resourcesToMap;
    public HashSet<String> propertiesToMap;
    public HashSet<String> classesToMap;
}
