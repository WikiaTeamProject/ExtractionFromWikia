package extractionPostprocessing.util;

import extractionPostprocessing.model.ResourceServiceResult;
import utils.IOoperations;

import java.util.HashMap;
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
    private static HashMap<String,String> pageIdsMap;
    private static HashMap<String,String> ontologiesMap;
    private static HashMap<String,String> propertiesMap;


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

        if (pageIdsMap == null) {
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
     * @param resource resource/page ID to look for in DBpedia
     * @return true if it exist on DBpedia else false
     */
    public boolean resourceExistsInDBpedia(String resource) {
        resource = resource.toLowerCase();
        if (pageIdsMap == null) {
            // pageIds were not loaded yet
            this.loadPageIds();
        }
        return pageIdsMap.containsKey(resource);
    }


    /**
     * This function will return pageID in actual
     * case as stored in DBpedia
     * @param pageID page ID to look for in HashMap
     * @return pageID in actual case
     */
    public String getPageId(String pageID){

        String pageIDValue;

        if(pageIdsMap==null){
            // pageIds were not loaded yet
            this.loadPageIds();
        }

        if(pageIdsMap.get(pageID)!=null){
            pageIDValue = pageIdsMap.get(pageID);
        }
        else pageIDValue="<null>";

        return pageIDValue;
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
    private void loadPageIds() {
        logger.info("Loading page ids from file into memory. This may take a while.");
        try {
            IOoperations ioOps = new IOoperations();
            this.pageIdsMap = ioOps.getPageIDs();
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
     * This class returns true when the ontology exists. It ignores casing.
     * @param resource ontology class to check
     * @return true if ontology class is present in DBpedia else false
     */
    public boolean ontologyClassExistInDBpedia(String resource) {
        resource = resource.toLowerCase();
        if (ontologiesMap == null) {
            // ontologies were not loaded yet
            this.loadOntologyClasses();
        }
        return ontologiesMap.containsKey(resource);
    }


    /**
     * This function will return ontology Class in actual
     * case as stored in DBpedia
     * @param ontology  ontology class to look for in HashMap
     * @return ontology class in actual case
     */
    public String getOntologyClass(String ontology){

        ontology = ontology.toLowerCase();
        String ontologyClassValue;

        if(ontologiesMap==null){
            // ontologies were not loaded yet
            this.loadOntologyClasses();
        }

        if(ontologiesMap.get(ontology)!=null){
            ontologyClassValue = ontologiesMap.get(ontology);
        }
        else ontologyClassValue="<null>";

        return ontologyClassValue;
    }


    /**
     *
     * @param resource property to check for existance
     * @return true if property is present in DBpedia else false
     */
    public boolean propertyExistInDBPedia(String resource) {
        if (propertiesMap == null) {
            // pageIds were not loaded yet
            this.loadPropertiesSet();
        }
        return propertiesMap.containsKey(resource);
    }


    /**
     * This function will return property in actual
     * case as stored in DBpedia
     * @param property  property to look for in HashMap
     * @return property in actual case
     */
    public String getProperty(String property){

        String propertyValue;

        if(propertiesMap==null){
            // properties were not loaded yet
            this.loadPropertiesSet();
        }

        if(propertiesMap.get(property)!=null){
            propertyValue = propertiesMap.get(property);
        }
        else propertyValue="<null>";

        return propertyValue;
    }

    /**
     * This function will loads ontology classes
     * into static object by calling IO function
     */
    private void loadOntologyClasses() {
        logger.info("Loading ontology classes in memory. Please wait");
        try {
            IOoperations ioOps = new IOoperations();
            ontologiesMap = ioOps.getOntologyClasses();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
    }


    /**
     * This function will loads list of properties
     * into static object by calling IO function
     */
    private void loadPropertiesSet() {
        logger.info("Loading Properties Set in memory. Please wait");
        try {
            IOoperations ioOps = new IOoperations();
            propertiesMap = ioOps.getPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
    }

}
