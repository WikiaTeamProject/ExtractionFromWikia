package extractionPostprocessing.model;

/**
 * A structure serving as a tuple for a SPARQL result of a special query.
 */
public class SPARQLresult {

    public boolean resourceExists;
    public String redirectResource;

    /**
     * Constructor
     * @param resourceExists
     * @param redirectResource
     */
    public SPARQLresult(boolean resourceExists, String redirectResource){
        this.resourceExists = resourceExists;
        this.redirectResource = redirectResource;
    }

    /**
     * Constructor
     */
    public SPARQLresult(){}

    @Override
    public String toString(){
        return("Resource Exists: " + resourceExists + "\nRedirect Resource: " + redirectResource);
    }

}
