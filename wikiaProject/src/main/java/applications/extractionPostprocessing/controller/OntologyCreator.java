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
    private String pathToWikiDirectory;
    private File wikiDirectory;
    private static String rootDirectoryPath = IOoperations.getRootDirectoryPath();
    private static String targetNamespace = IOoperations.getTargetNamespace();

    /**
     * Constructor
     */
    public OntologyCreator( HashSet<String> classesForDefinition, HashSet<String> propertiesForDefinition, File wikiDirectory){
        this.classesForDefinition = classesForDefinition;
        this.propertiesForDefinition = propertiesForDefinition;
        this.wikiDirectory = wikiDirectory;
        this.pathToWikiDirectory = wikiDirectory.getAbsolutePath();
    }

    /**
     * Constructor
     */
    public OntologyCreator( HashSet<String> classesForDefinition, HashSet<String> propertiesForDefinition, String pathToWikiDirectory){
        this( classesForDefinition, propertiesForDefinition, new File(pathToWikiDirectory) );
    }


    /**
     * Creates an ontology.nt file in wikiDirectory
     */
    public void createOntology(){

        StringBuffer contentForOntologyFile = new StringBuffer();
        String lineToAdd = "";

        // add classes
        for(String classToAdd : classesForDefinition){
            lineToAdd = classToAdd + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Class> .\n";
            contentForOntologyFile.append(lineToAdd);
        }


        /*
        // add properties - currently not required
        for(String propertyToAdd : propertiesForDefinition){
            lineToAdd = propertyToAdd + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#ObjectProperty> .\n";
            contentForOntologyFile.append(lineToAdd);
        }
        */

        IOoperations.writeContentToFile(new File(pathToWikiDirectory + "/ontology.nt"), contentForOntologyFile.toString());

    }


    /**
     * Creates an ontology.nt file in wikiDirectory.
     */
    @Deprecated
    public void createOntologyWithJena(){

        OntModel ontologyModel = ModelFactory.createOntologyModel();
        ontologyModel.setNsPrefix("dbkwik", "http://" + targetNamespace);

        // add our ontology
        // Ontology dbkwikOntology = ontologyModel.createOntologyWithJena("http://" + targetNamespace + "/" + wikiName + "/ontology/");


        // add classes
        for(String classToAdd : classesForDefinition){

            // remove tags (those are added by the jena framework) and add class
            ontologyModel.createClass(removeTags(classToAdd));

        }


        // add properties - currently unclear whether that is intended
        /*
        for(String propertyToAdd : propertiesForDefinition){

            // remove tags (those are added by the jena framework) and add property
            ontologyModel.createProperty(removeTags(propertyToAdd));
            ontologyModel.createProperty("myTestProperty", "testNamespace");

        }
        */

        NTripleWriter nTripleWriter = new NTripleWriter(); // NTriple writer
        // Basic basicWriter = new Basic(); // RDF writer

        try {
            IOoperations.createDirectory(rootDirectoryPath + "/DBkwikOntology");
            FileOutputStream fileOutputStream = new FileOutputStream(new File(pathToWikiDirectory + "/ontology.nt"));

            // file output
            // writer2.write(ontologyModel, fileOutputStream, null); // RDF writer
            nTripleWriter.write(ontologyModel, fileOutputStream, null); // NTriple writer


            // command line output
            // basicWriter.write(ontologyModel, System.out, null);
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
    private static  String removeTags(String sequenceWithTags) {
        sequenceWithTags = sequenceWithTags.replace(">", "");
        sequenceWithTags = sequenceWithTags.replace("<", "");
        return sequenceWithTags;
    }

}
