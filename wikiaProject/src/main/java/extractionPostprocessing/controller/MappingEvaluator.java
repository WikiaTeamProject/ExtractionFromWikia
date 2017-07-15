package extractionPostprocessing.controller;

import java.io.*;
import java.util.*;

import extractionPostprocessing.model.EvaluationResult;
import extractionPostprocessing.util.PostprocessingIOHandler;

import java.util.logging.Logger;

/**
 * After creating the mapping files using a mapper, the evaluation for that mapping can be performed using this class.
 */
public class MappingEvaluator {

    private static Logger logger = Logger.getLogger(MappingEvaluator.class.getName());

    private static EvaluationResult mappingsEvaluationResult;


    // KPIs that are calculated for evaluation
    // The result of the last evaluation is persisted (to allow for testability)
    private static double weightedOverallAccuracyInPercent;
    private static double weightedOverallPrecisionInPercent;
    private static double weightedOverallRecallInPercent;
    private static double weightedOverallF1MeasureInPercent;
    private static double microAverageTruePositives;
    private static double microAverageFalsePositives;
    private static double microAverageTrueNegatives;
    private static double microAverageFalseNegatives;
    private static double microAverageAccuracyInPercent;
    private static double microAveragePrecisionInPercent;
    private static double microAverageRecallInPercent;
    private static double microAverageF1measureInPercent;
    private static double macroAverageAccuracyInPercent;
    private static double macroAveragePrecisionInPercent;
    private static double macroAverageRecallInPercent;
    private static double macroAverageF1measureInPercent;

    /**
     * Evaluate all mappings and print the result on the command line.
     * The results are also persisted in a file.
     */
    public static void evaluateAllMappings(){

        weightedOverallAccuracyInPercent = 0;
        weightedOverallPrecisionInPercent = 0;
        weightedOverallRecallInPercent = 0;
        weightedOverallF1MeasureInPercent = 0;
        microAverageTruePositives = 0;
        microAverageFalsePositives = 0;
        microAverageTrueNegatives = 0;
        microAverageFalseNegatives = 0;
        microAverageAccuracyInPercent = 0;
        microAveragePrecisionInPercent = 0;
        microAverageRecallInPercent = 0;
        microAverageF1measureInPercent = 0;
        macroAverageAccuracyInPercent = 0;
        macroAveragePrecisionInPercent = 0;
        macroAverageRecallInPercent = 0;
        macroAverageF1measureInPercent = 0 ;

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
                        evaluationResultLine = "Accuracy: " + evaluationResult.getAccuracyInPercent() + "% (" + directory.getName() + ")\n"
                                + "Precision: " + evaluationResult.getPrecisionInPercent() + "% (" + directory.getName() + ")\n"
                                + "Recall: " + evaluationResult.getRecallInPercent() + "% (" + directory.getName() + ")\n"
                                + "F1-Measure: " + evaluationResult.getF1MeasureInPercent() + "% (" + directory.getName() + ")\n";
                        logger.info(evaluationResultLine);
                        aggregatedEvaluationResults.append(evaluationResultLine + "\n");
                        totalMappings += evaluationResult.getTotalMappings();
                        evaluationResults.add(evaluationResult);
                    }
                }
            }


            if(evaluationResults.size() == 0){
                logger.info("No evaluation file was found. Make sure that there is at least one evaluation file within a wiki folder.");
                return;
            }

            for (EvaluationResult e : evaluationResults) {
                double e_accuracy = e.getAccuracyInPercent();
                int e_totalMappings = e.getTotalMappings();

                // enty-weighted
                weightedOverallAccuracyInPercent += (e.getAccuracyInPercent() * ((double) e.getTotalMappings() / totalMappings));
                weightedOverallPrecisionInPercent += (e.getPrecisionInPercent() * ((double) e.getTotalMappings() / totalMappings));
                weightedOverallRecallInPercent += (e.getRecallInPercent() * ((double) e.getTotalMappings() / totalMappings));
                weightedOverallF1MeasureInPercent += (e.getF1MeasureInPercent() * ((double) e.getTotalMappings() / totalMappings));

                // microaverage
                microAverageTruePositives += e.getTruePositives();
                microAverageFalsePositives += e.getFalsePositives();
                microAverageTrueNegatives += e.getTrueNegatives();
                microAverageFalseNegatives += e.getFalseNegatives();

                // macroaverage (not final numbers yet, will be processed after loop.)
                macroAverageAccuracyInPercent += e.getAccuracyInPercent();
                macroAveragePrecisionInPercent += e.getPrecisionInPercent();
                macroAverageRecallInPercent += e.getRecallInPercent();
                macroAverageF1measureInPercent += e.getF1MeasureInPercent();

            }

            // microaverage
            microAverageAccuracyInPercent = ( (microAverageTruePositives + microAverageTrueNegatives) / (microAverageTruePositives + microAverageTrueNegatives + microAverageFalsePositives + microAverageFalseNegatives)) * 100;
            microAveragePrecisionInPercent = ( (microAverageTruePositives) / (microAverageTruePositives + microAverageFalsePositives) ) * 100;
            microAverageRecallInPercent = ( (microAverageTruePositives) / (microAverageTruePositives + microAverageFalseNegatives) ) * 100;
            microAverageF1measureInPercent = ( (2.0 * microAveragePrecisionInPercent * microAverageRecallInPercent) / (microAveragePrecisionInPercent + microAverageRecallInPercent) );

            // macroaverage
            macroAverageAccuracyInPercent = macroAverageAccuracyInPercent / evaluationResults.size();
            macroAveragePrecisionInPercent = macroAveragePrecisionInPercent / evaluationResults.size();
            macroAverageRecallInPercent = macroAverageRecallInPercent / evaluationResults.size();
            macroAverageF1measureInPercent = macroAverageF1measureInPercent / evaluationResults.size();


        } else {
            // -> root is not a directory
            logger.severe("pathToRootDirectory is not a directory!");
        } // end of if(root.isDirectory())

        evaluationResultLine = "Summarized Evaluation Results\n\n\n" +
                "Microaverage\n" + "Microaverage Accuracy: " + (microAverageAccuracyInPercent) + "%\n" +
                "Microaverage Precision: " + (microAveragePrecisionInPercent) + "%\n" +
                "Microaverage Recall: " + (microAverageRecallInPercent) + "%\n" +
                "Microaverage F1-Measure: " + (microAverageF1measureInPercent) + "%\n\n\n" +
                "Macroaverage\n" + "Macroaverage Accuracy: " + (macroAverageAccuracyInPercent) + "%\n" +
                "Macroaverage Precision: " + (macroAveragePrecisionInPercent) + "%\n" +
                "Macroaverage Recall: " + (macroAverageRecallInPercent) + "%\n" +
                "Macroaverage F1-Measure: " + (macroAverageF1measureInPercent) + "%\n\n\n" +
                "Entry-Weigted Resuls" + "\nEntry-Weigted Overall Accuracy of " + evaluationResults.size() + " wikis: " + weightedOverallAccuracyInPercent + "%\n" +
                "Entry-Weigted Overall Precision of " + evaluationResults.size() + " wikis: " + weightedOverallPrecisionInPercent + "%\n" +
                "Entry-Weigted Overall Recall of " + evaluationResults.size() + " wikis: " + weightedOverallRecallInPercent + "%\n" +
                "Entry-Weigted Overall F1-Measure of " + evaluationResults.size() + " wikis: " + weightedOverallF1MeasureInPercent + "%\n\n\n" +
                "Number of annotated wikis: " + evaluationResults.size();


        logger.info(evaluationResultLine);
        aggregatedEvaluationResults.append(evaluationResultLine + "\n");

        // persist evaluation results to evaluation file:
        File evaluationFile = new File(pathToRootDirectory + "/evaluation_results.txt");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(evaluationFile));
            bw.write(aggregatedEvaluationResults.toString());
            bw.close();
        } catch (IOException ioe) {
            logger.severe(ioe.toString());
        }
    }


    /**
     * Create evaluations for one wiki
     *
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
        if (!manualMappingFile.exists()) {
            // check whether there is a file ending with the specified name
            File directory = new File(wikiPath);
            if (directory.isDirectory()) {
                for (File f : directory.listFiles()) {
                    if (f.getName().endsWith(manualMappingFileName)) {
                        manualMappingFile = f;
                    }
                }
                if(manualMappingFile == null || !manualMappingFile.exists()){
                    // the manual mapping file does not exist
                    // look for a file ending with evaluation.ttl
                    for (File f : directory.listFiles()) {
                        if (f.getName().endsWith("evaluation.ttl")) {
                            manualMappingFile = f;
                        }
                    }
                }
            } else {
                // wikiPath is not a directory
                return null;
            }
            if (!manualMappingFile.exists()) {
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

                    if (manualMappings.get(resource).equals("<null>")) {
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
                            if (dbPediaMappings.get(resource).equals("<null>")) {
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

        mappingsEvaluationResult = new EvaluationResult(falseNegatives, falsePositives, truePositives, trueNegatives);
        return mappingsEvaluationResult;
    }

    /**
     * overloaded method
     *
     * @param wikiPath
     * @return
     */
    private static EvaluationResult evaluateMappingsForOneWiki(File wikiPath) {
        return evaluateMappingsForOneWiki(wikiPath.getAbsolutePath());
    }


    /*
     * Only getters and setters below this point
     */

    public static EvaluationResult getMappingsEvaluationResult() {
        return mappingsEvaluationResult;
    }

    public static double getWeightedOverallAccuracyInPercent() {
        return weightedOverallAccuracyInPercent;
    }

    public static double getWeightedOverallPrecisionInPercent() {
        return weightedOverallPrecisionInPercent;
    }

    public static double getWeightedOverallRecallInPercent() {
        return weightedOverallRecallInPercent;
    }

    public static double getWeightedOverallF1MeasureInPercent() {
        return weightedOverallF1MeasureInPercent;
    }

    public static int getMicroAverageTruePositives() {
        return (int) microAverageTruePositives;
    }

    public static int getMicroAverageFalsePositives() {
        return (int) microAverageFalsePositives;
    }

    public static int getMicroAverageTrueNegatives() {
        return (int) microAverageTrueNegatives;
    }

    public static int getMicroAverageFalseNegatives() {
        return (int) microAverageFalseNegatives;
    }

    public static double getMicroAverageAccuracyInPercent() {
        return microAverageAccuracyInPercent;
    }

    public static double getMicroAveragePrecisionInPercent() {
        return microAveragePrecisionInPercent;
    }

    public static double getMicroAverageRecallInPercent() {
        return microAverageRecallInPercent;
    }

    public static double getMicroAverageF1measureInPercent() {
        return microAverageF1measureInPercent;
    }

    public static double getMacroAverageAccuracyInPercent() {
        return macroAverageAccuracyInPercent;
    }

    public static double getMacroAveragePrecisionInPercent() {
        return macroAveragePrecisionInPercent;
    }

    public static double getMacroAverageRecallInPercent() {
        return macroAverageRecallInPercent;
    }

    public static double getMacroAverageF1measureInPercent() {
        System.out.println();
        return macroAverageF1measureInPercent;
    }
}
