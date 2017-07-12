package extractionPostprocessing.util;

import extractionPostprocessing.model.ResourceServiceResult;
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
        String redirect;
        if (redirectsMap == null) {
            loadRedirects();
        }

        if (pageIds == null) {
            loadPageIds();
        }

        if (redirectsMap.get(resource) != null) {
            redirect = redirectsMap.get(resource);
        } else if (pageIds.contains(resource)) {
            redirect = resource;
        } else {
            // TODO: evaluate what's better
            redirect = "<null>";
            //return null;
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
        if (pageIds == null) {
            // pageIds were not loaded yet
            this.loadPageIds();
        }
        return pageIds.contains(resource);
    }


    /**
     * This function will load the redirect file into a HashMap.
     * If the HashMap already exists, a reload takes place.
     */
    public void loadRedirects() {
        logger.info("Loading redirects from file into memory. This may take a while.");
        try {
            IOoperations ioOps = new IOoperations();
            redirectsMap = ioOps.getResourcesRedirects();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
    }

    /**
     * This function will get pageids
     * by calling a function which will read the page ids file and return a HashSet of pageids to this function.
     * If the HashSet already exists, a reload takes place.
     */
    public void loadPageIds() {
        logger.info("Loading page ids from file into memory. This may take a while.");
        try {
            IOoperations ioOps = new IOoperations();
            this.pageIds = ioOps.getPageIDs();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
    }

    @Override
    public ResourceServiceResult getResourceAndRedirectInDBpedia(String resource) {

        ResourceServiceResult result = new ResourceServiceResult();
        result.resourceExists = resourceExistsInDBpedia(resource);
        if(result.resourceExists){
            result.redirectResource = getRedirect(resource);
        }

        return result;

    }
}
