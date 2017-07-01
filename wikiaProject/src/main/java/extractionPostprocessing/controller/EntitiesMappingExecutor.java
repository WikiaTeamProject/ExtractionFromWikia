package extractionPostprocessing.controller;

import extractionPostprocessing.model.*;
import utils.FileOperations;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Class for the creation of the mappings files.
 * Given a resourceMapper, this class performs the mapping for all wikis.
 */
public class EntitiesMappingExecutor {

    private static Logger logger = Logger.getLogger(EntitiesMappingExecutor.class.getName());
    private ResourceMapper resourceMapper;
    private PropertyMapper propertyMapper;
    private ClassMapper classMapper;


    /**
     * constructor
     * @param resourceMapper
     */
    public EntitiesMappingExecutor(ResourceMapper resourceMapper, PropertyMapper propertyMapper, ClassMapper classMapper) {
        this.resourceMapper = resourceMapper;
        this.classMapper = classMapper;
        this.propertyMapper = propertyMapper;
    }

    /**
     * Loops over the root directory and creates the mapping files.
     */
    public void createMappingFilesForAllWikis() {

        String pathToRootDirectory = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/PostProcessedWikis";
        File root = new File(pathToRootDirectory);

        if (root.isDirectory()) {

            // loop over all wikis
            for (File directory : root.listFiles()) {


                if (directory.isDirectory()) {
                    // we have a wiki file

                    WikiToMap wikiToMap = getMappingInformationOfWikiAndUpdateFiles(directory);
                    String targetNameSpace = ResourceBundle.getBundle("config").getString("targetnamespace") + "/" + directory.getName();

                    resourceMapper.writeResourceMappingsFile(directory, targetNameSpace, wikiToMap.resourcesToMap);
                    propertyMapper.writePropertiesMappingsFile(directory, targetNameSpace, wikiToMap.propertiesToMap);
                    classMapper.writeClassMappingsFile(directory, targetNameSpace, wikiToMap.classesToMap);


                } // end of check whether file is a directory
            } // end of loop over files
        } // end of check whether PostProcessedWikis is a directory
    }


    /**
     * This method looks for resources, properties and templates for a given wiki. It will collect those in sets and return them.
     * Additionally all files will be updated with the correct domain name.
     * @param directoryOfWiki The directory where the files of a single wiki are stored.
     * @return
     */
    private WikiToMap getMappingInformationOfWikiAndUpdateFiles(File directoryOfWiki){

        //get list of extracted files in a folder
        File[] listOfFiles = directoryOfWiki.listFiles();

        String dbPediaResource = "";
        String mappingFileName = ResourceBundle.getBundle("config").getString("mappingfilename");
        String targetNameSpace = ResourceBundle.getBundle("config").getString("targetnamespace") + "/" + directoryOfWiki.getName();

        HashSet<String> resourcesToMap = new HashSet<>();
        HashSet<String> propertiesToMap = new HashSet<>();
        HashSet<String> classesToMap = new HashSet<>();


        try {

            // Loop over all ttl files in the directory and create the mappings.
            for (int i = 0; i < listOfFiles.length; i++) {

                if ( // file is relevant
                        listOfFiles[i].isFile()
                                && listOfFiles[i].toString().endsWith(".ttl")
                                && !listOfFiles[i].toString().endsWith("_evaluation.ttl") // do not use resources from the evaluation file
                                && !listOfFiles[i].toString().endsWith(mappingFileName)   // do not use resources from the mapping file
                        ) {

                    String line = ""; // line to be read
                    String languageCode = "";

                    // content of the new file with updated domain to be overwritten
                    StringBuffer contentOfNewFile = new StringBuffer();

                    FileReader fr = new FileReader(listOfFiles[i].getAbsolutePath());
                    BufferedReader br = new BufferedReader(fr);


                    // read relevant file line by line
                    while ((line = br.readLine()) != null) {

                        // if the line is a comment -> continue with the next line
                        if (!line.trim().toLowerCase().startsWith("#") || !line.trim().toLowerCase().startsWith("#")) {
                            // -> line is not a comment

                            try {
                                // get the actual entity
                                dbPediaResource = line.trim().substring(0, line.indexOf(" "));
                                //System.out.println(dbPediaResource);
                            } catch (StringIndexOutOfBoundsException sioobe) {
                                logger.info("Exception in file " + listOfFiles[i].getAbsolutePath() + ": " + sioobe.toString());
                                logger.info("Problem in file: " + listOfFiles[i].toString());
                                logger.info("With String: " + dbPediaResource);
                                continue;
                            }

                            contentOfNewFile.append(line.replaceAll("dbpedia.org", targetNameSpace) + "\n");

                            // do not do for wikipedia and wikimedia entities and wikipedia entities
                            if (dbPediaResource.toLowerCase().contains("wikipedia.org") || dbPediaResource.toLowerCase().contains("commons.wikimedia.org")) {
                                // do nothing
                            } else {
                                // -> not a wikipedia or dbpedia resource

                                if (dbPediaResource.contains("/resource/")){
                                    resourcesToMap.add(dbPediaResource);
                                } else if (dbPediaResource.contains("/Template:")){
                                    classesToMap.add(dbPediaResource);
                                } else if (dbPediaResource.contains("/property/")) {
                                    propertiesToMap.add(dbPediaResource);
                                }

                                // Categories remain. Those are not mapped.
                            }
                    }
                }
                br.close();
                fr.close();

                // update the file, i.e. rewrite the file where the dbpedia domain is replaced with the actual domain
                FileOperations.updateFile(contentOfNewFile.toString(), listOfFiles[i]);

            } // end of if relevant file
        }// end of loop over all files of that particular wiki

        } catch (IOException ioe){
            logger.severe(ioe.toString());
            ioe.printStackTrace();
        }

        return new WikiToMap(directoryOfWiki.getName(), resourcesToMap, propertiesToMap, classesToMap);
    }




   /*
   ONLY GETTERS AND SETTERS BELOW.
    */

    public void setResourceMapper(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    public void setPropertyMapper(PropertyMapper propertyMapper) {
        this.propertyMapper = propertyMapper;
    }

    public void setClassMapper(ClassMapper classMapper) {
        this.classMapper = classMapper;
    }

    public ResourceMapper getResourceMapper() {
        return resourceMapper;
    }

    public PropertyMapper getPropertyMapper() {
        return propertyMapper;
    }

    public ClassMapper getClassMapper() {
        return classMapper;
    }
}
