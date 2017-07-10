package extractionPostprocessing.util;
import extractionPostprocessing.model.ResourceServiceResult;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;

import java.util.ArrayList;

/**
 * A class handling tasks concerning db-pedia like answering the question whether an entity exists in dbpedia or not.
 * The class can also just execute SPARQL queries for dbpedia.
 */
public class DBpediaResourceServiceOnline extends DBpediaResourceService{

    /**
     * This method checks whether a resource exists and whether there is a redirect resource on DBpedia.
     * The results are written into the returning object.
     * @param resource The resource to be checked.
     * @return
     */
    public ResourceServiceResult getResourceAndRedirectInDBpedia(String resource){

        resource = addTagsIfNotAtag(resource);
        System.out.println("Looking up resource " + resource);
        String query =
                "SELECT ?uri ?re\n" +
                "WHERE {\n" +
                "     ?uri <http://dbpedia.org/ontology/wikiPageID> ?id.\n" +
                "     FILTER (?uri = " +
                resource +
                ")\n" +
                "     OPTIONAL {?uri <http://dbpedia.org/ontology/wikiPageRedirects> ?re}\n" +
                "}\n";

        ResultSet resultSet = executeSPARQLquery(query);
        if(resultSet.hasNext()){
            QuerySolution solution = resultSet.nextSolution();
            RDFNode uriNode = solution.get("uri");
            RDFNode reNode = solution.get("re");
            if(uriNode.toString() == null){
                return new ResourceServiceResult(false, null);
            } else {
                if(reNode == null){
                    return new ResourceServiceResult(true, null);
                } else {
                    return new ResourceServiceResult(true, addTagsIfNotAtag(reNode.toString()));
                }
            }
        } else {
            return new ResourceServiceResult(false, null);
        }
    }


    /**
     * Checks whether a resource exists in dbpedia. This method will also return true if there is a redirect.
     * @param resource
     * @return
     */
    public boolean resourceExistsInDBpedia(String resource) {
        String query =
                "SELECT ?uri ?id \n" +
                " WHERE {\n" +
                "     ?uri <http://dbpedia.org/ontology/wikiPageID> ?id.\n" +
                "     FILTER (?uri = " +
                        resource +
                        ")\n" +
                " }";
        if(executeSPARQLquery(query, "id").isEmpty()){
            return false;
        } else {
            return true;
        }
    }


    /**
     * This method executes a SPARQL query to DBpedia.
     * @param queryString The query to execute.
     * @return
     */
    public ResultSet executeSPARQLquery(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService
                (
                        "http://dbpedia.org/sparql",
                        query,
                        "http://dbpedia.org"
                );
        return queryExecution.execSelect();
    }


    /**
     * This method executes a SPARQL query to DBpedia and returns a list of the variable to be returned.
     * This method is restricted to returning just one variable (= result column).
     * @param queryString The SPARQL query to be executed.
     * @param variableToReturn The name of the column to be returned.
     * @return List of the values of the column that shall be returned.
     */
    public static ArrayList<String> executeSPARQLquery(String queryString, String variableToReturn) {
        ArrayList<String> resultList = new ArrayList<String>();
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService
                (
                        "http://dbpedia.org/sparql",
                        query,
                        "http://dbpedia.org"
                );

        ResultSet resultSet = queryExecution.execSelect();
        while(resultSet.hasNext()){
            QuerySolution solution = resultSet.nextSolution();
            RDFNode node = solution.get(variableToReturn);
            resultList.add(node.toString());
        }
        return resultList;
    }

}
