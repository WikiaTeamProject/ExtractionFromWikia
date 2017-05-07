package extractionPostprocessing.util;

import java.io.File;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.HashMap;


public class IOHandler {

    private static Logger logger = Logger.getLogger(IOHandler.class.getName());


    public static HashMap<String, String> getExtractorMappings(String pathName) {
        File file = new File(pathName);
        return getExtractorMappings(file);
    }


    public static HashMap<String, String> getExtractorMappings(File fileName) {

        HashMap<String, String> dbPediaExtractorMappings = new HashMap<String, String>();
        BufferedReader bufferedReader;
        FileReader fileReader;
        String fileLine, key, value;

        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);

            while ((fileLine = bufferedReader.readLine()) != null) {

                key = fileLine.substring(1, fileLine.indexOf("><owl:As>"));
                value = fileLine.substring(fileLine.indexOf("><owl:As>") + 10, fileLine.length() - 1);
                dbPediaExtractorMappings.put(key, value);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }

        return dbPediaExtractorMappings;
    }

}
