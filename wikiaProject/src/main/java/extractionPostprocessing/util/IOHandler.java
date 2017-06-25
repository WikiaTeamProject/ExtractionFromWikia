package extractionPostprocessing.util;

import java.io.File;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A helper class for various IO methods that allows for modular usage of such methods.
 */
public class IOHandler {

    private static Logger logger = Logger.getLogger(IOHandler.class.getName());


    public static HashMap<String, String> getExtractorMappings(String pathName) {
        File file = new File(pathName);
        return getExtractorMappings(file);
    }

    /**
     * Read the manual mapping file and return a HashMap with key = local_resource (resource in the namespace you set)
     * and value = global_resource (dbpedia target).
     * @param fileName Path of the mapping file.
     * @return a HashMap with key = local_resource (resource in the namespace you set)
     * and value = global_resource (dbpedia target).
     */
    public static HashMap<String, String> getExtractorMappings(File fileName) {

        HashMap<String, String> dbPediaExtractorMappings = new HashMap<String, String>();
        BufferedReader bufferedReader;
        FileReader fileReader;
        String fileLine ="", key="", value="";

        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            lineLoop: while ((fileLine = bufferedReader.readLine()) != null) {
                if(!fileLine.startsWith("#")) {
                    // do only if the line does not start with # (used for comment)

                    Matcher matcher = null;
                    Pattern pattern = Pattern.compile("<[^<]*>");
                    // regex: <[^<]*>
                    // this regex captures everything between tags including the tags: <...>
                    // there are three tags in every line, we are not interested in the second tag

                    int index = 0;
                    matcher = pattern.matcher(fileLine);

                    while (matcher.find()) {
                        index++;
                        if (index == 1) {
                            if (dbPediaExtractorMappings.containsKey(matcher.group())) {
                                continue lineLoop;
                            } else {
                                key = matcher.group();
                            }
                        } else if(index == 3){
                            value = matcher.group();
                        }
                    }

                    dbPediaExtractorMappings.put(key, value);
                }
            }
            bufferedReader.close();
            fileReader.close();
        } catch (Exception ex) {
            logger.severe("Problem with file " +  fileName.getAbsolutePath() + "\nwith line: " + fileLine + ex.getMessage());
        }

        return dbPediaExtractorMappings;
    }

}
