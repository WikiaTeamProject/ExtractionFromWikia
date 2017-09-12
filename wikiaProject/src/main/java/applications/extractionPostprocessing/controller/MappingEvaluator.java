package applications.extractionPostprocessing.controller;

import java.io.*;
import java.util.*;

import applications.extractionPostprocessing.model.EvaluationResultAllWikis;
import applications.extractionPostprocessing.model.EvaluationResultSingleWiki;
import applications.extractionPostprocessing.util.PostprocessingIOHandler;
import utils.IOoperations;
import utils.OSDetails;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * After creating the mapping files using a mapper, the evaluation for that mapping can be performed using this class.
 */
public class MappingEvaluator {

    private static Logger logger = Logger.getLogger(MappingEvaluator.class.getName());

    private static EvaluationResultSingleWiki mappingsEvaluationResultSingleWiki;

    /**
     * Enum indicating what shall be evaluated.
     */
    public enum EvaluationObjectSingleWiki {
        RESOURCES, PROPERTIES, CLASSES;

        @Override
        public String toString() {
            switch (this) {
                case RESOURCES:
                    return "resources";
                case PROPERTIES:
                    return "properties";
                case CLASSES:
                    return "classes";
            }

            // none of the above
            return null;
        }
    }


    /**
     * Enum indicating what shall be evaluated.
     */
    public enum EvaluationObjectAllWikis {
        RESOURCES, PROPERTIES, CLASSES, ALL;

        @Override
        public String toString() {
            switch (this) {
                case RESOURCES:
                    return "resources";
                case PROPERTIES:
                    return "properties";
                case CLASSES:
                    return "classes";
                case ALL:
                    return "all";
            }

            // none of the above
            return null;
        }
    }


    /*
    * Evaluate all mappings and persist them in separate files
     */
    public static void evaluateAllMappings() {
        evaluateAllMappings(EvaluationObjectAllWikis.CLASSES, true);
        evaluateAllMappings(EvaluationObjectAllWikis.PROPERTIES, true);
        evaluateAllMappings(EvaluationObjectAllWikis.RESOURCES, true);
        evaluateAllMappings(EvaluationObjectAllWikis.ALL, true);
    }

    /**
     * Evaluate all mappings and print the result on the command line.
     * The results are also persisted in a file.
     *
     * @param evaluationObjectAllWikis The evaluationObjectAllWikis specifies for which mapping an evaluation shall be created.
     * @param persistResult            The persistResult flag indicates whether the result shall be persistet.
     */
    public static EvaluationResultAllWikis evaluateAllMappings(EvaluationObjectAllWikis evaluationObjectAllWikis, boolean persistResult) {

        // KPIs that are calculated for evaluation
        double weightedOverallAccuracyInPercent = 0;
        double weightedOverallPrecisionInPercent = 0;
        double weightedOverallRecallInPercent = 0;
        double weightedOverallF1MeasureInPercent = 0;
        double microAverageTruePositives = 0;
        double microAverageFalsePositives = 0;
        double microAverageTrueNegatives = 0;
        double microAverageFalseNegatives = 0;
        double microAverageAccuracyInPercent = 0;
        double microAveragePrecisionInPercent = 0;
        double microAverageRecallInPercent = 0;
        double microAverageF1measureInPercent = 0;
        double macroAverageAccuracyInPercent = 0;
        double macroAveragePrecisionInPercent = 0;
        double macroAverageRecallInPercent = 0;
        double macroAverageF1measureInPercent = 0;

        int totalMappings = 0;
        int totalAnnotations = 0;
        ArrayList<EvaluationResultSingleWiki> evaluationResultSingleWikis = new ArrayList<>();
        String pathToRootDirectory = ResourceBundle.getBundle("config").getString("pathToRootDirectory") + "/postProcessedWikis";
        StringBuffer aggregatedEvaluationResults = new StringBuffer();
        String evaluationResultLine = "";
        File root = new File(pathToRootDirectory);

        if (root.isDirectory()) {
            for (File directory : root.listFiles()) {
                if (directory.isDirectory()) {

                    EvaluationResultSingleWiki evaluationResultSingleWiki = null;

                    switch (evaluationObjectAllWikis) {
                        case CLASSES:
                            evaluationResultSingleWiki = evaluateMappingsForOneWiki(directory.getPath(), EvaluationObjectSingleWiki.CLASSES);
                            break;
                        case PROPERTIES:
                            evaluationResultSingleWiki = evaluateMappingsForOneWiki(directory.getPath(), EvaluationObjectSingleWiki.PROPERTIES);
                            break;
                        case RESOURCES:
                            evaluationResultSingleWiki = evaluateMappingsForOneWiki(directory.getPath(), EvaluationObjectSingleWiki.RESOURCES);
                            break;
                        case ALL:
                            int falseNegatives;
                            int falsePositives;
                            int truePositives;
                            int trueNegatives;
                            EvaluationResultSingleWiki evaluationResultSingleWikiClasses = evaluateMappingsForOneWiki(directory.getPath(), EvaluationObjectSingleWiki.CLASSES);
                            EvaluationResultSingleWiki evaluationResultSingleWikiProperties = evaluateMappingsForOneWiki(directory.getPath(), EvaluationObjectSingleWiki.PROPERTIES);
                            EvaluationResultSingleWiki evaluationResultSingleWikiResources = evaluateMappingsForOneWiki(directory.getPath(), EvaluationObjectSingleWiki.RESOURCES);

                            if(evaluationResultSingleWikiClasses == null){
                                evaluationResultSingleWikiClasses = new EvaluationResultSingleWiki(0, 0, 0, 0);
                            }

                            if(evaluationResultSingleWikiProperties == null){
                                evaluationResultSingleWikiProperties = new EvaluationResultSingleWiki(0, 0, 0, 0);
                            }

                            if(evaluationResultSingleWikiResources == null) {
                                evaluationResultSingleWikiResources = new EvaluationResultSingleWiki(0, 0, 0, 0);
                            }


                            // aggregate
                            falseNegatives = evaluationResultSingleWikiClasses.getFalseNegatives() + evaluationResultSingleWikiProperties.getFalseNegatives() +
                                    evaluationResultSingleWikiResources.getFalseNegatives();
                            falsePositives = evaluationResultSingleWikiClasses.getFalsePositives() + evaluationResultSingleWikiProperties.getFalsePositives() +
                                    evaluationResultSingleWikiResources.getFalsePositives();
                            truePositives = evaluationResultSingleWikiClasses.getTruePositives() + evaluationResultSingleWikiProperties.getTruePositives() +
                                    evaluationResultSingleWikiResources.getTruePositives();
                            trueNegatives = evaluationResultSingleWikiClasses.getTrueNegatives() + evaluationResultSingleWikiProperties.getTrueNegatives() +
                                    evaluationResultSingleWikiResources.getTrueNegatives();

                            evaluationResultSingleWiki = new EvaluationResultSingleWiki(
                                    falseNegatives,
                                    falsePositives,
                                    truePositives,
                                    trueNegatives
                            );

                    } // switch (evaluationObjectAllWikis)

                    if (evaluationResultSingleWiki != null) {
                        evaluationResultLine = "Accuracy: " + evaluationResultSingleWiki.getAccuracyInPercent() + "% (" + directory.getName() + ")\n"
                                + "Precision: " + evaluationResultSingleWiki.getPrecisionInPercent() + "% (" + directory.getName() + ")\n"
                                + "Recall: " + evaluationResultSingleWiki.getRecallInPercent() + "% (" + directory.getName() + ")\n"
                                + "F1-Measure: " + evaluationResultSingleWiki.getF1MeasureInPercent() + "% (" + directory.getName() + ")\n"
                                + "Number of Mannual Annotations: " + evaluationResultSingleWiki.getTotalMappings() + " (" + directory.getName() + ")\n";
                        logger.info(evaluationResultLine);
                        aggregatedEvaluationResults.append(evaluationResultLine + "\n");
                        totalMappings += evaluationResultSingleWiki.getTotalMappings();
                        totalAnnotations += evaluationResultSingleWiki.getTotalMappings();
                        evaluationResultSingleWikis.add(evaluationResultSingleWiki);
                    }
                }
            } // for File directory : root.listFiles())


            if (evaluationResultSingleWikis.size() == 0) {
                logger.info("No evaluation file was found. Make sure that there is at least one evaluation file within a wiki folder.");
                return null;
            }

            for (EvaluationResultSingleWiki e : evaluationResultSingleWikis) {
                double e_accuracy = e.getAccuracyInPercent();
                int e_totalMappings = e.getTotalMappings();

                // entry-weighted
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
            microAverageAccuracyInPercent = ((microAverageTruePositives + microAverageTrueNegatives) / (microAverageTruePositives + microAverageTrueNegatives + microAverageFalsePositives + microAverageFalseNegatives)) * 100;
            microAveragePrecisionInPercent = ((microAverageTruePositives) / (microAverageTruePositives + microAverageFalsePositives)) * 100;
            microAverageRecallInPercent = ((microAverageTruePositives) / (microAverageTruePositives + microAverageFalseNegatives)) * 100;
            microAverageF1measureInPercent = ((2.0 * microAveragePrecisionInPercent * microAverageRecallInPercent) / (microAveragePrecisionInPercent + microAverageRecallInPercent));

            // macroaverage
            macroAverageAccuracyInPercent = macroAverageAccuracyInPercent / evaluationResultSingleWikis.size();
            macroAveragePrecisionInPercent = macroAveragePrecisionInPercent / evaluationResultSingleWikis.size();
            macroAverageRecallInPercent = macroAverageRecallInPercent / evaluationResultSingleWikis.size();
            macroAverageF1measureInPercent = macroAverageF1measureInPercent / evaluationResultSingleWikis.size();


        } else {
            // -> root is not a directory
            logger.severe("pathToRootDirectory is not a directory!");
        } // end of if(root.isDirectory())

        evaluationResultLine = "\nSummarized Evaluation Results\n" +
                "-----------------------------\n\n" +
                "Microaverage\n" + "Microaverage Accuracy: " + (microAverageAccuracyInPercent) + "%\n" +
                "Microaverage Precision: " + (microAveragePrecisionInPercent) + "%\n" +
                "Microaverage Recall: " + (microAverageRecallInPercent) + "%\n" +
                "Microaverage F1-Measure: " + (microAverageF1measureInPercent) + "%\n\n\n" +
                "Macroaverage\n" + "Macroaverage Accuracy: " + (macroAverageAccuracyInPercent) + "%\n" +
                "Macroaverage Precision: " + (macroAveragePrecisionInPercent) + "%\n" +
                "Macroaverage Recall: " + (macroAverageRecallInPercent) + "%\n" +
                "Macroaverage F1-Measure: " + (macroAverageF1measureInPercent) + "%\n\n\n" +
                "Entry-Weigted Resuls" + "\nEntry-Weigted Overall Accuracy of " + evaluationResultSingleWikis.size() + " wikis: " + weightedOverallAccuracyInPercent + "%\n" +
                "Entry-Weigted Overall Precision of " + evaluationResultSingleWikis.size() + " wikis: " + weightedOverallPrecisionInPercent + "%\n" +
                "Entry-Weigted Overall Recall of " + evaluationResultSingleWikis.size() + " wikis: " + weightedOverallRecallInPercent + "%\n" +
                "Entry-Weigted Overall F1-Measure of " + evaluationResultSingleWikis.size() + " wikis: " + weightedOverallF1MeasureInPercent + "%\n\n\n" +
                "Number of annotated wikis: " + evaluationResultSingleWikis.size() + "\n" +
                "Number of annotations: " + totalAnnotations;


        logger.info(evaluationResultLine);
        aggregatedEvaluationResults.append(evaluationResultLine + "\n");


        if (persistResult) {
            // persist evaluation results to evaluation file:
            File evaluationFile = new File(pathToRootDirectory + "/" + evaluationObjectAllWikis.toString() + "_" + "evaluation_results.txt");
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(evaluationFile));
                bw.write(aggregatedEvaluationResults.toString());
                bw.close();
            } catch (IOException ioe) {
                logger.severe(ioe.toString());
            }
        }

        // return object
        EvaluationResultAllWikis evaluationResultAllWikis = new EvaluationResultAllWikis();

        // macro average
        evaluationResultAllWikis.macroAverageAccuracyInPercent = macroAverageAccuracyInPercent;
        evaluationResultAllWikis.macroAveragePrecisionInPercent = macroAveragePrecisionInPercent;
        evaluationResultAllWikis.macroAverageRecallInPercent = macroAverageRecallInPercent;
        evaluationResultAllWikis.macroAverageF1measureInPercent = macroAverageF1measureInPercent;

        // micro average
        evaluationResultAllWikis.microAverageTruePositives = microAverageTruePositives;
        evaluationResultAllWikis.microAverageFalsePositives = microAverageFalsePositives;
        evaluationResultAllWikis.microAverageTrueNegatives = microAverageTrueNegatives;
        evaluationResultAllWikis.microAverageFalseNegatives = microAverageFalseNegatives;
        evaluationResultAllWikis.microAverageAccuracyInPercent = microAverageAccuracyInPercent;
        evaluationResultAllWikis.microAveragePrecisionInPercent = microAveragePrecisionInPercent;
        evaluationResultAllWikis.microAverageRecallInPercent = microAverageRecallInPercent;
        evaluationResultAllWikis.microAverageF1measureInPercent = microAverageF1measureInPercent;

        // micro average
        evaluationResultAllWikis.weightedOverallAccuracyInPercent = weightedOverallAccuracyInPercent;
        evaluationResultAllWikis.weightedOverallPrecisionInPercent = weightedOverallPrecisionInPercent;
        evaluationResultAllWikis.weightedOverallRecallInPercent = weightedOverallRecallInPercent;
        evaluationResultAllWikis.weightedOverallF1MeasureInPercent = weightedOverallF1MeasureInPercent;

        return evaluationResultAllWikis;
    }


    /**
     * Evaluate all mappings and persist the result in a file.
     *
     * @param evaluationObjectAllWikis The evaluationObjectSingleWiki specifies for which mapping an evaluation shall be created.
     */
    public static EvaluationResultAllWikis evaluateAllMappings(EvaluationObjectAllWikis evaluationObjectAllWikis) {
        return evaluateAllMappings(evaluationObjectAllWikis, true);
    }

    /**
     * Create evaluations for one wiki.
     *
     * @param wikiPath
     * @return
     */
    public static EvaluationResultSingleWiki evaluateMappingsForOneWiki(String wikiPath, EvaluationObjectSingleWiki evaluationObjectSingleWiki) {

        String dbPediaResourceMappingsFileName = "";

        switch (evaluationObjectSingleWiki) {
            case CLASSES:
                dbPediaResourceMappingsFileName = "classMappings.ttl";
                break;
            case RESOURCES:
                dbPediaResourceMappingsFileName = "resourceMappings.ttl";
                break;
            case PROPERTIES:
                dbPediaResourceMappingsFileName = "propertyMappings.ttl";
                break;
        }

        HashMap<String, String> dbPediaMappings;
        HashMap<String, String> manualMappings;

        PostprocessingIOHandler postprocessingIoHandler = new PostprocessingIOHandler();
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
                if (manualMappingFile == null || !manualMappingFile.exists()) {
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

            // delete null mappings if they exist
            if (dbPediaMappings.containsValue("<null>"))
                deleteNullMappings(mappingFile);

        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }


        mappingsEvaluationResultSingleWiki = new EvaluationResultSingleWiki(falseNegatives, falsePositives, truePositives, trueNegatives);
        return mappingsEvaluationResultSingleWiki;
    }

    /**
     * Overloaded method.
     *
     * @param wikiPath
     * @return
     */
    private static EvaluationResultSingleWiki evaluateMappingsForOneWiki(File wikiPath, EvaluationObjectSingleWiki evaluationObjectSingleWiki) {
        return evaluateMappingsForOneWiki(wikiPath.getAbsolutePath(), evaluationObjectSingleWiki);
    }

    /**
     * Rewrite mappings file by deleting all included null mappings
     * @param mappingFile
     */
    private static void deleteNullMappings(File mappingFile) {
        String line;
        StringBuffer content = new StringBuffer();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(mappingFile))) {

            String newLineCharacter = "";

            // get newLineCharacter in a safe way
            try {
                newLineCharacter = OSDetails.getNewLineCharacter();
            } catch (Exception e){
                logger.severe(e.toString());
                logger.severe("Could not find resource 'newLineCharacter'.");
            }

            if(newLineCharacter.isEmpty()){
                newLineCharacter = "\n\r";
            }

            while ((line = bufferedReader.readLine()) != null) {

                // include everything except null mappings
                if (! line.contains("<null>"))
                    content.append(line + newLineCharacter);

            }
            bufferedReader.close();
            IOoperations.updateFile(content.toString(), mappingFile);

        } catch (IOException e) {
            logger.severe(e.toString());
        }
    }


}