package extractionPostprocessing.model;

/**
 * This interface defines the protocol for a class handling tasks concerning db-pedia like answering the question
 * whether an entity exists in dbpedia or not.
 */
public interface DBpediaResourceServiceInterface {

    /**
     * Method checks whether the resource exists on dbpedia.
     * @param resource Stringformat of resource link.
     * @return true if the resource exists on dbpedia, else false.
     */
    public boolean resourceExistsInDBpedia(String resource);

}
