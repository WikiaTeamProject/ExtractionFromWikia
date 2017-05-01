package extractionPostprocessing.util;

import wikiaStatistics.util.WikiaStatisticsTools;

import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.HashMap;

/**
 * Created by Samresh Kumar on 5/1/2017.
 */
public class IOHandler {

    private static Logger logger = Logger.getLogger(WikiaStatisticsTools.class.getName());

    public static HashMap<String,String> GetDbPediaExtractorMappings(String fileDirectory){
        HashMap<String,String> dbPediaExtractorMappings=new HashMap<String,String>();
        String mappingFileName=ResourceBundle.getBundle("config").getString("mappingfilename");
        String filePath = fileDirectory + "/"+mappingFileName;
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        String fileLine="";
        String key="";
        String value="";
        try{
            fileReader=new FileReader(filePath);
            bufferedReader=new BufferedReader(fileReader);
            while((fileLine=bufferedReader.readLine())!=null){

                key=fileLine.substring(1,fileLine.indexOf("><owl:As>"));
                value=fileLine.substring(fileLine.indexOf("><owl:As>")+10,fileLine.length()-1);
                dbPediaExtractorMappings.put(key,value);
            }
            bufferedReader.close();
            fileReader.close();
        }
        catch(Exception ex){
            logger.severe(ex.getMessage());
        }

        return dbPediaExtractorMappings;
    }

    public static HashMap<String,String> GetManualMappings(String fileDirectory){

        HashMap<String,String> manualMappings=new HashMap<String,String>();
        String mappingFileName=ResourceBundle.getBundle("config").getString("manualmappingfilename");
        String filePath = fileDirectory + "/"+mappingFileName;
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        String fileLine="";
        String key="";
        String value="";

        try{
            fileReader=new FileReader(filePath);
            bufferedReader=new BufferedReader(fileReader);

            while((fileLine=bufferedReader.readLine())!=null){

                key=fileLine.substring(1,fileLine.indexOf("><owl:As>"));
                value=fileLine.substring(fileLine.indexOf("><owl:As>")+10,fileLine.length()-1);

                manualMappings.put(key,value);
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch(Exception ex){
            logger.severe(ex.getMessage());
        }

        return manualMappings;
    }


    public static void main(String[] args){

        String directoryPath = ResourceBundle.getBundle("config").getString("directory");

        HashMap<String,String> test=GetManualMappings(directoryPath);

        System.out.println(test);
    }

}
