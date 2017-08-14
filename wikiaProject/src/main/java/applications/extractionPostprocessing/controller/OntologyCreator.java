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
    private String rootDirectoryPath;
    private String targetNamespace;

    /**
     * Constructor
     */
    public OntologyCreator( HashSet<String> classesForDefinition){
        this.classesForDefinition = classesForDefinition;
        this.rootDirectoryPath = IOoperations.getRootDirectoryPath();
        this.targetNamespace = IOoperations.getTargetNamespace();
    }


    /**
     * Creates an ontology.nt file in <root>/DBkwikOntology.
     */
    public void createOntologyOption(){

        OntModel ontologyModel = ModelFactory.createOntologyModel();
        ontologyModel.setNsPrefix("dbkwik", "http://" + targetNamespace);

        // add our ontology
        Ontology dbkwikOntology = ontologyModel.createOntology("http://" + targetNamespace + "/ontology/");

        // add a sample class
        // ontologyModel.createClass("http://" + targetNamespace + "/ontology/BasketballLeague" );

        for(String classToAdd: classesForDefinition){

            // remove tags (those are added by the jena framework)
            classToAdd = classToAdd.replace(">", "");
            classToAdd = classToAdd.replace("<", "");

            // add class
            ontologyModel.createClass(classToAdd );
        }

        NTripleWriter nTripleWriter = new NTripleWriter(); // NTriple writer
        Basic basicWriter = new Basic(); // RDF writer

        // TODO: Find out what base is and whether we want to use it.

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


    public static void main(String[] args) {
        OntologyCreator oc = new OntologyCreator(new HashSet<String>());
        oc.createOntologyOption();
    }



}
