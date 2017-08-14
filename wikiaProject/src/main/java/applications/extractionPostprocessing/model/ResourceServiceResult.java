package applications.extractionPostprocessing.model;

/**
 * A structure serving as a tuple for a SPARQL result of a special query.
 */
public class ResourceServiceResult {


    /**
     * Boolean which indicates whether the dbpedia resource exists.
     */
    public boolean resourceExists;

    /**
     * Null if there is no redirect.
     */
    public String redirectResource;

    /**
     * Constructor
     * @param resourceExists
     * @param redirectResource
     */
    public ResourceServiceResult(boolean resourceExists, String redirectResource){
        this.resourceExists = resourceExists;
        this.redirectResource = redirectResource;
    }

    /**
     * Constructor
     */
    public ResourceServiceResult(){}

    @Override
    public String toString(){
        return("Resource Exists: " + resourceExists + "\nRedirect Resource: " + redirectResource);
    }

}
