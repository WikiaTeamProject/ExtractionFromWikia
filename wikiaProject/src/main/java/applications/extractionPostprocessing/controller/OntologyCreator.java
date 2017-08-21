package applications.extractionPostprocessing.controller;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.impl.NTripleWriter;
import org.apache.jena.rdfxml.xmloutput.impl.Basic;
import utils.IOoperations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashSet;

/**
 * This class produces an ontology.
 */
public class OntologyCreator {


    private HashSet<String> classesForDefinition; // contains all classes for which a definition is to be created
    private HashSet<String> propertiesForDefinition; // contains all classes for which a definition is to be created
    private String pathToWiki;
    private static String rootDirectoryPath = IOoperations.getRootDirectoryPath();
    private static String targetNamespace = IOoperations.getTargetNamespace();


    /**
     * Constructor
     */
    public OntologyCreator( HashSet<String> classesForDefinition, HashSet<String> propertiesForDefinition, String pathToWiki){
        this.classesForDefinition = classesForDefinition;
        this.propertiesForDefinition = propertiesForDefinition;
        this.pathToWiki = pathToWiki;
    }


    /**
     * Creates an ontology.nt file in <root>/DBkwikOntology.
     */
    public void createOntology(){

        OntModel ontologyModel = ModelFactory.createOntologyModel();
        ontologyModel.setNsPrefix("dbkwik", "http://" + targetNamespace);

        // add our ontology
        Ontology dbkwikOntology = ontologyModel.createOntology("http://" + targetNamespace + "/ontology/");


        // add classes
        for(String classToAdd : classesForDefinition){

            // remove tags (those are added by the jena framework) and add class
            ontologyModel.createClass(removeTags(classToAdd));

        }


        // add properties
        for(String propertyToAdd : propertiesForDefinition){

            // remove tags (those are added by the jena framework) and add property
            ontologyModel.createProperty(removeTags(propertyToAdd));

        }

        NTripleWriter nTripleWriter = new NTripleWriter(); // NTriple writer
        Basic basicWriter = new Basic(); // RDF writer

        try {
            IOoperations.createDirectory(rootDirectoryPath + "/DBkwikOntology");
            FileOutputStream fileOutputStream = new FileOutputStream(new File(rootDirectoryPath + "/DBkwikOntology/ontology.nt"));

            // file output
            // writer2.write(ontologyModel, fileOutputStream, null); // RDF writer
            nTripleWriter.write(ontologyModel, fileOutputStream, null); // NTriple writer


            // command line output
            basicWriter.write(ontologyModel, System.out, null);
            nTripleWriter.write(ontologyModel, System.out, null);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * Remove tags.
     * @param sequenceWithTags
     * @return
     */
    private String removeTags(String sequenceWithTags) {
        sequenceWithTags = sequenceWithTags.replace(">", "");
        sequenceWithTags = sequenceWithTags.replace("<", "");
        return sequenceWithTags;
    }


}
