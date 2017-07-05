package extractionPostprocessing.controller;

import java.io.*;
import java.util.*;

import extractionPostprocessing.model.EvaluationResult;
import extractionPostprocessing.util.PostprocessingIOHandler;

import java.util.logging.Logger;

/**
 * After creating the mapping files using a mapper, the evaluation for that mapping can be performed using this class.
 */
public class MappingEvaluation {

    private static Logger logger = Logger.getLogger(MappingEvaluation.class.getName());

    private static EvaluationResult mappingsEvaluationResult;

    /**
     * Evaluate all mappings and print the result on the command line.
     * The results are also persisted in a file.
     */
    public static void evaluateAllMappings() {


        // KPIs
        double weightedOverallAccuracy = 0.0;
        double weightedOverallPrecision = 0.0;
        double weightedOverallRecall = 0.0;
        double weightedOverallF1Measure = 0.0;
        double microAverageTruePositives = 0.0;
        double microAverageFalsePositives = 0.0;
        double microAverageTrueNegatives = 0.0;
        double microAverageFalseNegatives = 0.0;
        double microAverageAccuracy = 0.0;
        double microAveragePrecision = 0.0;
        double microAverageRecall = 0.0;
        double microAverageF1measure = 0.0;
        double macroAverageAccuracy = 0.0;
        double macroAveragePrecision = 0.0;
        double macroAverageRecall = 0.0;
        double macroAverageF1measure = 0.0;


        int totalMappings = 0;
        ArrayList<EvaluationResult> evaluationResults = new ArrayList<>();
        String pathToRootDirectory = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/PostProcessedWikis";
        StringBuffer aggregatedEvaluationResults = new StringBuffer();
        String evaluationResultLine = "";
        File root = new File(pathToRootDirectory);

        if (root.isDirectory()) {
            for (File directory : root.listFiles()) {
                if (directory.isDirectory()) {
                    EvaluationResult evaluationResult = evaluateMappingsForOneWiki(directory);
                    if (evaluationResult != null) {
                        evaluationResultLine = "Accuracy: " + evaluationResult.getAccuracy() + "% (" + directory.getName() + ")\n"
                                + "Precision: " + evaluationResult.getPrecision() + "% (" + directory.getName() + ")\n"
                                + "Recall: " + evaluationResult.getRecall() + "% (" + directory.getName() + ")\n"
                                + "F1-Measure: " + evaluationResult.getF1Measure() + "% (" + directory.getName() + ")\n";
                        logger.info(evaluationResultLine);
                        aggregatedEvaluationResults.append(evaluationResultLine + "\n");
                        totalMappings += evaluationResult.getTotalMappings();
                        evaluationResults.add(evaluationResult);
                    }
                }
            }



            for(EvaluationResult e : evaluationResults){
                double e_accuracy = e.getAccuracy();
                int e_totalMappings = e.getTotalMappings();

                // enty-weighted
                weightedOverallAccuracy += (e.getAccuracy() * ( (double) e.getTotalMappings() / totalMappings ));
                weightedOverallPrecision += (e.getPrecision() * ( (double) e.getTotalMappings() / totalMappings));
                weightedOverallRecall += (e.getRecall() * ( (double) e.getTotalMappings() / totalMappings));
                weightedOverallF1Measure += (e.getF1Measure() * ( (double) e.getTotalMappings() / totalMappings));

                // microaverage
                microAverageTruePositives += e.getTruePositives();
                microAverageFalsePositives += e.getFalsePositives();
                microAverageTrueNegatives += e.getTrueNegatives();
                microAverageFalseNegatives += e.getFalseNegatives();

                // macroaverage (not final numbers yet, will be processed after loop.)
                macroAverageAccuracy += e.getAccuracy();
                macroAveragePrecision += e.getPrecision();
                macroAverageRecall += e.getRecall();
                macroAverageF1measure += e.getF1Measure();

            }


            // microaverage
            microAverageAccuracy = (microAverageTruePositives) / (microAverageTruePositives + microAverageTrueNegatives + microAverageFalsePositives + microAverageFalsePositives);
            microAveragePrecision = (microAverageTruePositives) / (microAverageTruePositives + microAverageFalsePositives);
            microAverageRecall = (microAverageTruePositives) / (microAverageTruePositives + microAverageFalseNegatives);
            microAverageF1measure = (2.0 * microAveragePrecision * microAverageRecall) / (microAveragePrecision + microAverageRecall);

            // macroaverage
            macroAverageAccuracy = macroAverageAccuracy / evaluationResults.size();
            macroAveragePrecision = macroAveragePrecision / evaluationResults.size();
            macroAverageRecall = macroAverageRecall / evaluationResults.size();
            macroAverageF1measure = macroAverageF1measure / evaluationResults.size();


        } else {
            // -> root is not a directory
            logger.severe("pathToRootDirectory is not a directory!");
        } // end of if(root.isDirectory())

        evaluationResultLine = "Entry-Weigted Resuls + \nEntry-Weigted Overall Accuracy of " + evaluationResults.size() + " wikis: " + weightedOverallAccuracy + "%\n" +
                "Entry-Weigted Overall Precision of " + evaluationResults.size() + " wikis: " + weightedOverallPrecision + "%\n" +
                "Entry-Weigted Overall Recall of " + evaluationResults.size() + " wikis: " + weightedOverallRecall + "%\n" +
                "Entry-Weigted Overall F1-Measure of "+ evaluationResults.size() + " wikis: " + weightedOverallF1Measure + "%\n\n\n" +
                "Microaverage\n" + "Microaverage Accuracy: " + microAverageAccuracy + "%\n" +
                "Microaverage Precision: " + microAveragePrecision + "%\n" +
                "Microaverage Recall: " + microAverageRecall + "%\n" +
                "Microaverage F1-Measure" + microAverageRecall + "%\n\n\n" +
                "Macroaverage\n" + "Macroaverage Accuracy " + macroAverageAccuracy + "%\n" +
                "Macroaverage Precision " + macroAveragePrecision + "%\n" +
                "Macroaverage Recall " + macroAverageRecall + "%\n" +
                "Macroaverage F1-Measure " + macroAverageF1measure + "%\n";


        logger.info(evaluationResultLine);
        aggregatedEvaluationResults.append(evaluationResultLine + "\n");

        // persist evaluation results to evaluation file:
        File evaluationFile = new File(pathToRootDirectory + "/evaluation_results.txt");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(evaluationFile));
            bw.write(aggregatedEvaluationResults.toString());
            bw.close();
        } catch (IOException ioe){
            logger.severe(ioe.toString());
        }
    }


    /**
     * Create evaluations for one wiki
     * @param wikiPath
     * @return
     */
    public static EvaluationResult evaluateMappingsForOneWiki(String wikiPath) {
        HashMap<String, String> dbPediaMappings;
        HashMap<String, String> manualMappings;

        PostprocessingIOHandler postprocessingIoHandler = new PostprocessingIOHandler();
        String dbPediaResourceMappingsFileName = "resourceMappings.ttl";
        String manualMappingFileName = ResourceBundle.getBundle("config").getString("manualmappingfilename");

        File mappingFile = new File(wikiPath + "/" + dbPediaResourceMappingsFileName);
        File manualMappingFile = new File(wikiPath + "/" + manualMappingFileName);

        if (!mappingFile.exists()) {
            // there is no generated mapping file
            logger.severe("No generated mapping file for wiki " + wikiPath + "\nRun mapper before evaluating wiki.");
            return null;
        }

        // if there is no manual mapping file with the name specified in the properties file use the file that ends
        // with the specified name
        if(!manualMappingFile.exists()){
            // check whether there is a file ending with the specified name
            File directory = new File(wikiPath);
            if(directory.isDirectory()){
                for (File f:directory.listFiles()) {
                    if(f.getName().endsWith(manualMappingFileName)){
                        manualMappingFile = f;
                    }
                }
            } else {
                // wikiPath is not a directory
                return null;
            }
            if(!manualMappingFile.exists()){
                // no mapping file could be found
                return null;
            }
        }

        int truePositives = 0;
        int trueNegatives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;
        int totalMapping = 0;

        try {
            dbPediaMappings = postprocessingIoHandler.getExtractorMappings(mappingFile);
            manualMappings = postprocessingIoHandler.getExtractorMappings(manualMappingFile);

            for (String resource : manualMappings.keySet()) {
                if (dbPediaMappings.containsKey(resource)) {
                    totalMapping++;

                    if(manualMappings.get(resource).equals("<null>")){
                        // NEGATIVE case
                        if (manualMappings.get(resource).toLowerCase().equals(dbPediaMappings.get(resource).toLowerCase())) {
                            trueNegatives++;
                        } else {
                            falsePositives++;
                        }
                    } else {
                        // POSITIVE case
                        if (manualMappings.get(resource).toLowerCase().equals(dbPediaMappings.get(resource).toLowerCase())) {
                            truePositives++;
                        } else {
                            if(dbPediaMappings.get(resource).equals("<null>")){
                                falseNegatives++;
                            } else {
                                falsePositives++;
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }

        mappingsEvaluationResult = new EvaluationResult(falseNegatives, falsePositives, truePositives, trueNegatives, totalMapping);
        return mappingsEvaluationResult;
    }

    /**
     * overloaded method
     * @param wikiPath
     * @return
     */
    private static EvaluationResult evaluateMappingsForOneWiki(File wikiPath) {
        return evaluateMappingsForOneWiki(wikiPath.getAbsolutePath());
    }

}
