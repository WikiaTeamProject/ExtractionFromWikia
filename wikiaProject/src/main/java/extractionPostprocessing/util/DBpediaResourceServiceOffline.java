package extractionPostprocessing.util;

import extractionPostprocessing.model.SPARQLresult;
import utils.IOoperations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * A class for storing dbpedia resources.
 * There are 2 requisites when using the class:
 * - make sure that the folder "pageids" exists in the root directory and that there is at least one redirect file in the folder.
 * - make sure that the folder "redirects" exists in the root directory and that there is at least one redirect file in the folder.
 */
public class DBpediaResourceServiceOffline extends DBpediaResourceService {

    private static DBpediaResourceServiceOffline DBpediaResourceServiceOfflineObject;
    private static HashMap<String, String> redirectsMap;
    private static Logger logger = Logger.getLogger(DBpediaResourceServiceOffline.class.getName());
    private static String rootDirectoryPath = ResourceBundle.getBundle("config").getString("pathToRootDirectory");
    private static HashSet<String> pageIds;

    /**
     * Private constructor -> Singleton pattern
     */
    private DBpediaResourceServiceOffline() {
        // do nothing
    }

    public static DBpediaResourceServiceOffline getDBpediaResourceServiceOfflineObject() {
        if (DBpediaResourceServiceOfflineObject == null) {
            DBpediaResourceServiceOfflineObject = new DBpediaResourceServiceOffline();
            return DBpediaResourceServiceOfflineObject;
        } else {
            return DBpediaResourceServiceOfflineObject;
        }
    }


    /**
     * This function will return redirects for a given
     * resource.
     *
     * @param resource : resource for which redirect is required
     * @return Redirect mapping for a resource. Null if there is no resource.
     */
    public String getRedirect(String resource) {
        String redirect = null;
        if (redirectsMap == null) {
            mapRedirects();
        }

        if (pageIds == null) {
            getPageIds();
        }

        if (redirectsMap.get(resource) != null) {
            redirect = redirectsMap.get(resource);
        } else if (pageIds.contains(resource)) {
            redirect = resource;
        } else {
            redirect = "<null>";
        }

        return redirect;
    }


    /**
     * Checks whether a resource exists in dbpedia. This method will also return true if there is a redirect.
     *
     * @param resource
     * @return
     */
    public boolean resourceExistsInDBpedia(String resource) {
        if (pageIds.isEmpty()) {
            getPageIds();
        }
        return pageIds.contains(resource);

    }


    /**
     * This function will map
     * redirects for resources
     */
    public void mapRedirects() {
        try {
            IOoperations ioOps = new IOoperations();
            redirectsMap = ioOps.getResourcesRedirects();
        } catch (Exception excpetion) {
            logger.severe(excpetion.getMessage());
        }
    }

    /**
     * This function will get page ids
     * by calling function which will read page ids file and return hashset of pageids
     * to this function
     */
    public void getPageIds() {
        try {
            IOoperations ioOps = new IOoperations();
            pageIds = ioOps.getPageIDs();
        } catch (Exception excpetion) {
            logger.severe(excpetion.getMessage());
        }
    }

    @Override
    public SPARQLresult getResourceAndRedirectInDBpedia(String resource) {

        SPARQLresult result = new SPARQLresult();
        result.resourceExists = resourceExistsInDBpedia(resource);
        if(result.resourceExists){
            result.redirectResource = getRedirect(resource);
        }

        return result;

    }
}
