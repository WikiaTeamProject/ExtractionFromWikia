package extractionPostprocessing;

import wikiaStatistics.util.WikiaStatisticsTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Samresh Kumar on 4/28/2017.
 */
public class EntitiesMapping {

    private static Logger logger = Logger.getLogger(WikiaStatisticsTools.class.getName());

    /**
     *
     * @param pathToExtractedFiles
     */
    public void extractWikiaDbpediaEntitiesMapping(String pathToExtractedFiles){


        String targetNameSpace= ResourceBundle.getBundle("config").getString("targetnamespace");

        HashSet<String> entitiesMapping=new HashSet<String>();

        try{

            File extractedFilesPath = new File(pathToExtractedFiles);

            //get list of extracted files in a folder
            File[] listOfFiles = extractedFilesPath.listFiles();

            String dbPediaNameSpace = "";
            String mappingFileContents="";


            for (int i = 0; i < listOfFiles.length; i++) {

                if (listOfFiles[i].isFile() && listOfFiles[i].toString().endsWith(".ttl") ) {

                    String line = "";
                    String fileContents = "";
                    String languageCode = "";


                    FileReader fr = new FileReader(listOfFiles[i].getAbsolutePath());
                    BufferedReader br = new BufferedReader(fr);

                    while ((line = br.readLine()) != null) {

                        if(!line.trim().toLowerCase().startsWith("#") || !line.trim().toLowerCase().startsWith("#")) {
                            dbPediaNameSpace = line.trim().substring(0, line.indexOf(" "));

                            mappingFileContents = dbPediaNameSpace.replace("dbpedia.org", targetNameSpace) + "<owl:As>" + dbPediaNameSpace + "\n";

                            entitiesMapping.add(mappingFileContents);
                        }

                    }
                    br.close();
                    fr.close();

                }
            }


            //Write Contents to Mapping File
            writeContentsToMappingFile(entitiesMapping);

        }
        catch(Exception exception){
            logger.severe(exception.toString());
        }
    }

    /**
     *
     * @param entitiesMapping
     */
    public void writeContentsToMappingFile(HashSet<String> entitiesMapping){

        String directoryPath = ResourceBundle.getBundle("config").getString("directory");
        String mappingFileName=ResourceBundle.getBundle("config").getString("mappingfilename");
        String filePath = directoryPath + "/"+mappingFileName;
        Iterator<String> entitiesMappingIterator;

        try{
            //Initialize file Writer Objects
            PrintWriter fileWriter = new PrintWriter(filePath, "UTF-8");

            entitiesMappingIterator=entitiesMapping.iterator();

            while(entitiesMappingIterator.hasNext()) {
                //Write contents to file
                fileWriter.write(entitiesMappingIterator.next());
            }

            //Close file Writer
            fileWriter.close();
        }
        catch(Exception exception){

            logger.log(Level.SEVERE, exception.getMessage());
        }
    }

}
