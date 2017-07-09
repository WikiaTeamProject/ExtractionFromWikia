package extractionPostprocessing.util;

import utils.IOoperations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * A class for storing redirects.
 */
public class DBpediaResourceServiceOffline {

    private static DBpediaResourceServiceOffline DBpediaResourceServiceOfflineObject;
    private static HashMap<String,String> redirectsMap;
    private static Logger logger = Logger.getLogger(DBpediaResourceServiceOffline.class.getName());
    private static String rootDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory");
    private static HashSet<String> pageIds;

    /**
     * Private constructor -> Singleton pattern
     */
    private DBpediaResourceServiceOffline() {
        // do nothing
    }

    public static DBpediaResourceServiceOffline getDBpediaResourceServiceOfflineObject(){
        if(DBpediaResourceServiceOfflineObject == null){
            DBpediaResourceServiceOfflineObject = new DBpediaResourceServiceOffline();
            return DBpediaResourceServiceOfflineObject;
        } else {
            return DBpediaResourceServiceOfflineObject;
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
