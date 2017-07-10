package extractionPostprocessing.util;

import extractionPostprocessing.model.ResourceServiceResult;

/**
 * Abstract class defining the protocol for DBpedia Resource Services.
 */
public abstract class DBpediaResourceService {

    /**
     * Returns whether a resource exists on DBpedia and what the redirect is if there is any.
     * @param resource
     * @return
     */
    public abstract ResourceServiceResult getResourceAndRedirectInDBpedia(String resource);


    /**
     * A simple helper method that will trim the tags of a string, i.e. given the String "<my tag>" the method will
     * simply return "my tag". The method will not trim the string if it does not start with "<" and end with ">".
     * @param tag
     * @return String without tags.
     */
    public static String trimTagIfTag(String tag){
        if(tag.startsWith("<") && tag.endsWith(">")) {
            return tag.substring(1, tag.length() - 1);
        } else {
            return tag;
        }
    }

    /**
     * Add tags around an expression. The method will only add tags if the expression does not start with "<" and does
     * not end with ">".
     * @param expression
     * @return
     */
    public static String addTagsIfNotAtag(String expression){
        if(!expression.startsWith("<") && !expression.endsWith(">")) {
            return "<" + expression + ">";
        } else {
            return expression;
        }
    }


}
