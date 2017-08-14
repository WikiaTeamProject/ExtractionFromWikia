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
    private static HashMap<String,String> ontologiesClassMap;
    private static HashMap<String,String> ontologiesPropertiesMap;
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
        resource = resource.toLowerCase();
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
     * Casing is ignored.
     *
     * @param resource resource/page ID to look for in DBpedia
     * @return true if it exist on DBpedia else false
     */
    public boolean resourceExistsInDBpediaIgnoreCase(String resource) {
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
     * @param resource page ID to look for in HashMap
     * @return pageID in actual case
     */
    public String getResourceCorrectCase(String resource){
        resource = resource.toLowerCase();
        String pageIDValue;

        if(pageIdsMap==null){
            // pageIds were not loaded yet
            this.loadPageIds();
        }

        if(pageIdsMap.get(resource)!=null){
            pageIDValue = pageIdsMap.get(resource);
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
        resource = resource.toLowerCase();

        ResourceServiceResult result = new ResourceServiceResult();
        result.resourceExists = resourceExistsInDBpediaIgnoreCase(resource);
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
    public boolean ontologyClassExistInDBpediaIgnoreCase(String resource) {
        resource = resource.toLowerCase();
        if (ontologiesClassMap == null) {
            // ontologies were not loaded yet
            this.loadOntologyClasses();
        }
        return ontologiesClassMap.containsKey(resource);
    }


    /**
     * This function will return ontology Class in actual
     * case as stored in DBpedia
     * @param ontology  ontology class to look for in HashMap
     * @return ontology class in actual case
     */
    public String getOntologyClassCorrectCase(String ontology){


        ontology = ontology.toLowerCase();
        String ontologyClassValue;

        if(ontologiesClassMap==null){
            // ontologies were not loaded yet
            this.loadOntologyClasses();
        }

        if(ontologiesClassMap.get(ontology)!=null){
            ontologyClassValue = ontologiesClassMap.get(ontology);
        }
        else ontologyClassValue="<null>";

        return ontologyClassValue;
    }


    /**
     * This method resturns property in actual case as
     * present in DBpedia file
     * @param property property to search for in DBpedia file
     * @return property in correct case if is present in DBpedia file
     * otherwise returns "<NULL>"
     */
    public String getOntologyPropertyCorrectCase(String property){

        property = property.toLowerCase();
        String ontologyPropertyValue;

        if(ontologiesPropertiesMap==null){
            // load ontology properties in HashMap
            this.loadOntologyClasses();
        }

        if(ontologiesPropertiesMap.get(property)!=null){
            ontologyPropertyValue = ontologiesClassMap.get(property);
        }
        else ontologyPropertyValue="<null>";

        return ontologyPropertyValue;
    }



    /**
     * This method checks dbpedia ontology file if property is present
     * or not ignoring case
     * @param resource property to search in ontology file
     * @return true if property is present in ontology file otherwise false
     */
    public boolean propertyExistInDBPediaOntologyIgnoreCase(String resource) {
        resource = resource.toLowerCase();
        if (ontologiesPropertiesMap == null) {
            //load ontology properties
            this.loadOntologyClasses();
        }
        return ontologiesPropertiesMap.containsKey(resource);
    }



    /**
     * Returns a boolean indicating whether the specified property exists in DBpedia.
     * It ignores casing.
     * @param resource property to check for existance
     * @return true if property is present in DBpedia else false
     */
    public boolean propertyExistInDBPediaIgnoreCase(String resource) {
        resource = resource.toLowerCase();
        if (propertiesMap == null) {
            // pageIds were not loaded yet
            this.loadPropertiesSet();
        }
        return propertiesMap.containsKey(resource);
    }


    /**
     * This function will return property in actual
     * case as stored in DBpedia
     * @param property  Property to look for in the HashMap.
     * @return Property in the correct casing.
     */
    public String getPropertyCorrectCase(String property){
        property = property.toLowerCase();
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
        logger.info("Loading ontology classes in memory... Please wait.");
        try {
            IOoperations ioOps = new IOoperations();
            HashMap<String,String> ontologiesMap = ioOps.getOntologyClasses();
            ontologiesClassMap = new HashMap<String,String>();
            ontologiesPropertiesMap = new HashMap<String,String>();

            if(ontologiesMap!=null){
                for(String ontologyClass : ontologiesMap.keySet()){

                    String resourceName = ontologyClass.substring(ontologyClass.lastIndexOf("/")+1,ontologyClass.length());

                   if(resourceName.length() > 0 &&
                           Character.isUpperCase(resourceName.charAt(0))){

                       ontologiesClassMap.put(ontologyClass.toLowerCase(),ontologyClass);

                   }
                   else{
                       ontologiesPropertiesMap.put(ontologyClass.toLowerCase(),ontologyClass);
                   }

                } // end of loop over ontologies map

            } // end of if ontologiesMap != null

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
