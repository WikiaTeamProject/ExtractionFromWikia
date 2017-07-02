package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
/**
 * A class for storing redirects.
 */
public class Redirects {

    private static Redirects redirectsObject;
    private static HashMap<String,String> redirectsMap;
    private static Logger logger = Logger.getLogger(Redirects.class.getName());
    private static String rootDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory");
    private static HashSet<String> pageIds;

    private Redirects() {
        // do nothing
    }

    public static Redirects getRedirectsObject(){
        if(redirectsObject == null){
            redirectsObject = new Redirects();
            return redirectsObject;
        } else {
            return redirectsObject;
        }
    }


    /**
     * This function will return redirects for a given
     * resource.
     * @param resource : resource for which redirect is required
     * @return Redirect mapping for a resource. Null if there is no resource.
     */
    public String getRedirect(String resource){
        String redirect = null;
        if(redirectsMap == null){
            mapRedirects();
        }

        if(pageIds==null){
            getPageIds();
        }

        if(redirectsMap.get(resource)!= null){
            redirect = redirectsMap.get(resource);
        }
        else if(pageIds.contains(resource)){
            redirect=resource;
        }
        else{
            redirect = "<null>";
        }

        return redirect;
    }


    /**
     * This function will map
     * redirects for resources
     */
    public void mapRedirects(){
       try{
           IOoperations ioOps=new IOoperations();
           redirectsMap=ioOps.getResourcesRedirects();
       }
       catch(Exception excpetion){
           logger.severe(excpetion.getMessage());
       }
    }

    /**
     * This function will get page ids
     * by calling function which will read page ids file and return hashset of pageids
     * to this function
     */
    public void getPageIds(){
        try{
            IOoperations ioOps=new IOoperations();
            pageIds=ioOps.getPageIDs();
        }
        catch(Exception excpetion){
            logger.severe(excpetion.getMessage());
        }
    }

}
