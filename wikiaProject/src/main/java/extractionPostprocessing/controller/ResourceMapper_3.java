package extractionPostprocessing.controller;

import extractionPostprocessing.model.ResourceMapperInterface;
import extractionPostprocessing.model.SPARQLresult;
import utils.FileOperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Third mapper implementation.
 * - automatically maps files to <null>
 * - checks whether a resource exists before mapping it
 */
public class ResourceMapper_3 implements ResourceMapperInterface {

    /**
     * This method creates the mapping file for the given wiki (using the file path to the wiki).
     * @param pathToWikiFolder
     */
    public void createMappingFileForSingleWiki(File pathToWikiFolder) {

        String targetNameSpace = ResourceBundle.getBundle("config").getString("targetnamespace");

        // data structure containing unique resources for the mapping, used to query dbpedia only once for each resource
        HashSet<String> uniqueResourcesToBeMapped = new HashSet<>();

        // data structure containing the data that will be written into the mapping file
        // "<resource_1> <same_as> <resource_2>"
        HashSet<String> entitiesMapping = new HashSet<String>();

        try {
            //get list of extracted files in a folder
            File[] listOfFiles = pathToWikiFolder.listFiles();

            String dbPediaResource = "";
            String mappingFileContents = "";
            String mappingFileName = ResourceBundle.getBundle("config").getString("mappingfilename");


            // Loop over all ttl files in the directory and create the mappings.
            for (int i = 0; i < listOfFiles.length; i++) {

                if      (
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

                    while ((line = br.readLine()) != null) {

                        // if the line is a comment -> continue with the next line
                        if (!line.trim().toLowerCase().startsWith("#") || !line.trim().toLowerCase().startsWith("#")) {
                            // -> line is not a comment

                            try {
                                // get the actual entity
                                dbPediaResource = line.trim().substring(0, line.indexOf(" "));
                                //System.out.println(dbPediaResource);
                            } catch(StringIndexOutOfBoundsException sioobe){
                                logger.info("Exception in file " + listOfFiles[i].getAbsolutePath() + ": " + sioobe.toString());
                                logger.info("Problem in file: " + listOfFiles[i].toString());
                                logger.info("With String: " + dbPediaResource);
                                continue;
                            }

                            contentOfNewFile.append(line.replaceAll("dbpedia.org", targetNameSpace) + "\n");

                            // do not do for wikipedia entities
                            if(!dbPediaResource.toLowerCase().contains("wikipedia.org") || !dbPediaResource.toLowerCase().contains("commons.wikimedia.org"))
                            {
                                // map files to null
                                if(dbPediaResource.contains("/File:")){
                                    mappingFileContents = dbPediaResource.replace("dbpedia.org", targetNameSpace) + " <owl:sameAs> <null> .\n";
                                    entitiesMapping.add(mappingFileContents);
                                } else {
                                    // write that resource in a list to look it up at a later point in time
                                    uniqueResourcesToBeMapped.add(dbPediaResource);
                                }
                            }
                        }
                    }
                    br.close();
                    fr.close();

                    FileOperations.updateFile(contentOfNewFile.toString(), listOfFiles[i]);

                } // end of if relevant file
            } // end of loop over all files of that particular wiki


            // no loop over the set of unique resources and try to find them on DBpedia together with potential redirect resources
            Iterator iterator = uniqueResourcesToBeMapped.iterator();
            while(iterator.hasNext()){
                dbPediaResource = (String) iterator.next();
                SPARQLresult result = DBpediaResourceService.getResourceAndRedirectInDBpedia(dbPediaResource);

                if(result.resourceExists){
                    if(result.redirectResource != null){
                        // -> use redirect resource
                        mappingFileContents = dbPediaResource.replace("dbpedia.org", targetNameSpace) + " <owl:sameAs> " + result.redirectResource +" .\n";
                        entitiesMapping.add(mappingFileContents);
                    } else {
                        // -> no redirect resource -> use dbPediaResource
                        mappingFileContents = dbPediaResource.replace("dbpedia.org", targetNameSpace) + " <owl:sameAs> " + dbPediaResource + " .\n";
                        entitiesMapping.add(mappingFileContents);
                    }
                } else {
                    // -> resource does not exist -> map to <null>
                    mappingFileContents = dbPediaResource.replace("dbpedia.org", targetNameSpace) + " <owl:sameAs> " + "<null> " +".\n";
                    entitiesMapping.add(mappingFileContents);
                }
            }

            //Write Contents to Mapping File
            ResourceMapperInterface.writeMappingContentsToFile(entitiesMapping, pathToWikiFolder.getAbsolutePath());

        } catch (Exception exception) {
            logger.severe(exception.toString());
        }
    }


    /**
     * Overloaded method.
     * @param pathToExtractedFiles
     */
    public void createMappingFileForSingleWiki(String pathToExtractedFiles) {
        File file = new File(pathToExtractedFiles);
        createMappingFileForSingleWiki(file);
    }

}
