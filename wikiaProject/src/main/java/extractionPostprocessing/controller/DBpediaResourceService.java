package extractionPostprocessing.controller;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import utils.OutputOperations;

import java.util.ArrayList;

/**
 * A class handling tasks concerning db-pedia like answering the question whether an entity exists in dbpedia or not.
 * The class can also just execute SPARQL queries for dbpedia.
 */
public class DBpediaResourceService {

    // TODO: delete
    public static void main(String[] args) {
        DBpediaResourceService service = new DBpediaResourceService();
        service.resourceExistsInDBpedia("");
    }





    /**
     * Checks wheter a resource exists in dbpedia. This method will also return true if there is a redirect.
     * @param resource
     * @return
     */
    public static boolean resourceExistsInDBpedia(String resource) {
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
            RDFNode resource = solution.get(variableToReturn);
            resultList.add(resource.toString());
        }

        return resultList;
    }


    /**
     * A simple helper method that will trim the tags of a string, i.e. given the String "<my tag>" the method will
     * simply return "my tag".
     * @param tag
     * @return String without tags.
     */
    public String trimTag(String tag){
        String result = tag.substring(1, tag.length()-1);
        return result;
    }


}
