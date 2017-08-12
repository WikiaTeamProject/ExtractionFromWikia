package extractionPostprocessing.controller;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.NTripleWriter;
import org.apache.jena.rdfxml.xmloutput.impl.BaseXMLWriter;
import org.apache.jena.rdfxml.xmloutput.impl.Basic;
import org.apache.jena.util.iterator.ExtendedIterator;
import utils.IOoperations;

import java.io.File;
import java.io.PrintWriter;
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


    public void createOntologyOption1(){

        // still undecided what to do

        /**
         * Option 1: Manually concatenate an ontology.
         * The output would look like http://downloads.dbpedia.org/2016-04/dbpedia_2016-04.nt
         */

        StringBuffer contentForFile = new StringBuffer();
        String ontologyTag = "<http://" + targetNamespace + "/ontology/>";

        // set type = ontology
        String type = ontologyTag + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Ontology> .";
        contentForFile.append(type);

        // set vocabulary
        String vocabulary = ontologyTag + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://purl.org/vocommons/voaf#Vocabulary> .";
        contentForFile.append(vocabulary);

        IOoperations.createDirectory(rootDirectoryPath + "/DBkwikOntology");
        File ontologyFile = new File(rootDirectoryPath + "/DBkwikOntology" + "ontology.nt");
        IOoperations.writeContentToFile(ontologyFile, contentForFile.toString());

    }

    public void createOntologyOption2(){
        /**
         * Option 2: Use Apache Jena
         */

        OntModel ontologyModel = ModelFactory.createOntologyModel();
        ontologyModel.setNsPrefix("dbkwik", "http://" + targetNamespace);

        // add our ontology
        Ontology dbkwikOntology = ontologyModel.createOntology("http://" + targetNamespace + "/ontology/");

        // add a sample class
        ontologyModel.createClass("http://" + targetNamespace + "/ontology/BasketballLeague" );

        NTripleWriter writer = new NTripleWriter();
        writer.write(ontologyModel, System.out, null);
        // TODO: Find out what base is and whether we want to use it.

        Basic writer2 = new Basic();
        writer2.write(ontologyModel, System.out, null);




    }


    public static void main(String[] args) {
        OntologyCreator oc = new OntologyCreator(new HashSet<String>());
        oc.createOntologyOption2();
    }



}
