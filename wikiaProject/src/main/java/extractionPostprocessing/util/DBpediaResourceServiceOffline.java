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
    private static HashSet<String> ontologiesSet;
    private static HashSet<String> propertiesSet;
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
            if(redirectsMap.get(resource).equals(resource)) {
                // we do not map entities to themselves
                return null;
            } else {
                redirect = redirectsMap.get(resource);
            }
        } else {
            // no redirect found
            return null;
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
        resource = addTagsIfNotAtag(resource);
        ResourceServiceResult result = new ResourceServiceResult();
        result.resourceExists = resourceExistsInDBpedia(resource);
        if(result.resourceExists){
            result.redirectResource = getRedirect(resource);
        }

        return result;

    }

    /**
     *
     * @param resource ontology class to check
     * @return true if ontology class is present in DBpedia else false
     */
    public boolean ontologyClassExistInDBpedia(String resource) {
        if (ontologiesSet == null) {
            // pageIds were not loaded yet
            this.loadOntologyClasses();
        }
        return ontologiesSet.contains(resource);
    }


    /**
     *
     * @param resource property to check for existance
     * @return true if property is present in DBpedia else false
     */
    public boolean propertyExistInDBPedia(String resource) {
        if (propertiesSet == null) {
            // pageIds were not loaded yet
            this.loadPropertiesSet();
        }
        return propertiesSet.contains(resource);
    }


    /**
     * This function will loads ontology classes
     * into static object by calling IO function
     */
    public void loadOntologyClasses() {
        logger.info("Loading ontology classes in memory. Please wait");
        try {
            IOoperations ioOps = new IOoperations();
            ontologiesSet = ioOps.getOntologyClasses();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
    }


    /**
     * This function will loads list of properties
     * into static object by calling IO function
     */
    public void loadPropertiesSet() {
        logger.info("Loading Properties Set in memory. Please wait");
        try {
            IOoperations ioOps = new IOoperations();
            propertiesSet = ioOps.getPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
    }

}
