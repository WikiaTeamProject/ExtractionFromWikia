package extractionPostprocessing.util;

import java.util.HashMap;
import java.util.ResourceBundle;
import extractionPostprocessing.model.Evaluator;
import wikiaStatistics.util.WikiaStatisticsTools;

import java.util.logging.Logger;

/**
 * Created by Samresh Kumar on 5/1/2017.
 */
public class MappingsEvaluation {

    private static Logger logger = Logger.getLogger(WikiaStatisticsTools.class.getName());

    public void EvaluateMappings(String wikiPath){

        Evaluator mappingsEvaluator;
        String dbPediaMappingFileName=ResourceBundle.getBundle("config").getString("mappingfilename");
        String manualMappingFileName=ResourceBundle.getBundle("config").getString("manualmappingfilename");
        HashMap<String,String> dbPediaMappings=new HashMap<String, String>();
        HashMap<String,String> manualMappings=new HashMap<String, String>();
        IOHandler ioHandler=new IOHandler();
        int truePositives=0;
        int trueNegatives=0;
        int falsePositives=0;
        int falseNegatives=0;
        int totalMapping=0;

        try{

            dbPediaMappings=    ioHandler.GetExtractorMappings(wikiPath+"//"+dbPediaMappingFileName);
            manualMappings=     ioHandler.GetExtractorMappings(wikiPath+"//"+manualMappingFileName);


            for(String resource:manualMappings.keySet()){
                if(dbPediaMappings.containsKey(resource)){
                    totalMapping++;
                    if(manualMappings.get(resource).toLowerCase().equals(dbPediaMappings.get(resource).toLowerCase())){
                        truePositives++;
                    }
                }
            }

            System.out.println("Accuracy : " + (truePositives/totalMapping*100)+"%");

        }
        catch(Exception ex){
            logger.severe(ex.getMessage());
        }

       // return mappingsEvaluator;
    }

}
