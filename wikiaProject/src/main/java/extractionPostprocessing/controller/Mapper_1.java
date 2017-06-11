package extractionPostprocessing.controller;

import extractionPostprocessing.model.MapperInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * First, relatively simple, implementation of a mapper.
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

            String dbPediaNameSpace = "";
            String mappingFileContents = "";


            // Loop over all ttl files in the directory and create the mappings.
            // This mapper works relatively simple: It only replaces the name space.
            for (int i = 0; i < listOfFiles.length; i++) {

                if (listOfFiles[i].isFile() && listOfFiles[i].toString().endsWith(".ttl")) {

                    String line = "";
                    String fileContents = "";
                    String languageCode = "";


                    FileReader fr = new FileReader(listOfFiles[i].getAbsolutePath());
                    BufferedReader br = new BufferedReader(fr);

                    while ((line = br.readLine()) != null) {

                        if (!line.trim().toLowerCase().startsWith("#") || !line.trim().toLowerCase().startsWith("#")) {
                            try {
                                // get the actual entity
                                dbPediaNameSpace = line.trim().substring(0, line.indexOf(" "));
                            } catch(StringIndexOutOfBoundsException sioobe){
                                logger.info("Exception in file " + listOfFiles[i].getAbsolutePath() + ": " + sioobe.toString());
                                continue;
                            }

                            // do not do for wikipedia entities
                            if(!dbPediaNameSpace.toLowerCase().contains("wikipedia.org")) {
                                mappingFileContents = dbPediaNameSpace.replace("dbpedia.org", targetNameSpace) + "<owl:As>" + dbPediaNameSpace + "\n";
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
