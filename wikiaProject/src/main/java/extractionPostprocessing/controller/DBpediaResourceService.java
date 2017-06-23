package extractionPostprocessing.controller;

import extractionPostprocessing.model.DBpediaResourceServiceInterface;

/**
 * Created by D060249 on 21.06.2017.
 */
public class DBpediaResourceService implements DBpediaResourceServiceInterface {

    @Override
    public boolean resourceExistsInDBpedia(String resource) {
        return false;
    }


    /**
     * A simple helper method that will trim the tags of a string, i.e. given the String "<my tag>" the method will
     * simply return "my tag".
     * @param tag
     * @return String without tags.
     */
    public String trimTag(String tag){
        String result = tag.substring(1, tag.length()-1);
        return result;
    }


}
