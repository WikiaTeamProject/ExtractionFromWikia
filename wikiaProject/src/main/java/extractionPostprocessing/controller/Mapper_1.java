package extractionPostprocessing.controller;

import extractionPostprocessing.model.MapperInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * First, relatively simple, implementation of a mapper.
 * - maps all other resources to dbpedia resources (same name)
 */
public class Mapper_1 implements MapperInterface {


    /**
     * This method creates the mapping file for the given wiki (using the file path to the wiki).
     * @param pathToWikiFolder
     */
    public void createMappingFileForSingleWiki(File pathToWikiFolder) {

        String targetNameSpace = ResourceBundle.getBundle("config").getString("targetnamespace");

        HashSet<String> entitiesMapping = new HashSet<String>();

        try {
            //get list of extracted files in a folder
            File[] listOfFiles = pathToWikiFolder.listFiles();

            String dbPediaEntity = "";
            String mappingFileContents = "";

            String mappingFileName = ResourceBundle.getBundle("config").getString("mappingfilename");

            // Loop over all ttl files in the directory and create the mappings.
            // This mapper works relatively simple: It only replaces the name space.
            for (int i = 0; i < listOfFiles.length; i++) {

                if      (
                        listOfFiles[i].isFile()
                        && listOfFiles[i].toString().endsWith(".ttl")
                        && !listOfFiles[i].toString().endsWith("_evaluation.ttl") // do not use resources from the evaluation file
                        && !listOfFiles[i].toString().endsWith(mappingFileName)   // do not use resources from the mapping file
                        ) {

                    String line = "";
                    String fileContents = "";
                    String languageCode = "";


                    FileReader fr = new FileReader(listOfFiles[i].getAbsolutePath());
                    BufferedReader br = new BufferedReader(fr);

                    while ((line = br.readLine()) != null) {

                        if (!line.trim().toLowerCase().startsWith("#") || !line.trim().toLowerCase().startsWith("#")) {
                            try {
                                // get the actual entity
                                dbPediaEntity = line.trim().substring(0, line.indexOf(" "));
                                System.out.println(dbPediaEntity);
                            } catch(StringIndexOutOfBoundsException sioobe){
                                logger.info("Exception in file " + listOfFiles[i].getAbsolutePath() + ": " + sioobe.toString());
                                logger.info("Problem in file: " + listOfFiles[i].toString());
                                logger.info("With String: " + dbPediaEntity);
                                continue;
                            }

                            // do not do for wikipedia entities
                            if(!dbPediaEntity.toLowerCase().contains("wikipedia.org")) {
                                mappingFileContents = dbPediaEntity.replace("dbpedia.org", targetNameSpace) + "<owl:As>" + dbPediaEntity + "\n";
                                entitiesMapping.add(mappingFileContents);
                            }
                        }

                    }
                    br.close();
                    fr.close();
                }
            }

            //Write Contents to Mapping File
            MapperInterface.writeContentsToMappingFile(entitiesMapping, pathToWikiFolder.getAbsolutePath());

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
