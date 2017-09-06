package applications.extractionPostprocessing.controller;

import applications.extractionPostprocessing.model.EvaluationResultAllWikis;
import applications.extractionPostprocessing.model.EvaluationResultSingleWiki;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

/**
 * Test class for {@link MappingEvaluator MappingEvaluator}.
 * The test also covers some functionality of {@Link applications.extractionPostprocessing.model.EvaluationResultSingleWiki EvaluationResultSingleWiki}.
 */
public class MappingEvaluatorTest {


    private static String pathToRootDirectory = ResourceBundle.getBundle("config").getString("pathToRootDirectory");

    @BeforeClass
    public static void setUp(){
        // make sure that the fake path to directory exists
        assertTrue(new File(ResourceBundle.getBundle("config").getString("pathToRootDirectory")).isDirectory());
    }


    public static void copyTestFilesForEvaluateAllMappings(){
        File sourceDirectory = new File(pathToRootDirectory + "/../evaluation_test_multiple_files");
        File targetDirectory = new File(pathToRootDirectory + "/postProcessedWikis");
        try {
            org.apache.commons.io.FileUtils.copyDirectory(sourceDirectory, targetDirectory);
        } catch (Exception e){
            e.printStackTrace();

            // raise an assertion error
            assertTrue(false);
        }
        System.out.println("Required files successfully copied.");
    }


    @Test
    public void evaluateAllMappings() throws Exception {

        // required for test to work:
        copyTestFilesForEvaluateAllMappings();

        EvaluationResultAllWikis result = MappingEvaluator.evaluateAllMappings(MappingEvaluator.EvaluationObjectAllWikis.RESOURCES, false);

        // macro average
        assertEquals(50.0, result.macroAverageAccuracyInPercent,  0.00000001);
        assertEquals(((7.0/6.0)/2.0) * 100 , result.macroAveragePrecisionInPercent,  0.00000001);
        assertEquals(50.0, result.macroAverageRecallInPercent,  0.00000001);
        assertEquals((40.0 + (2.0/3.0) * 100.0) / 2.0, result.macroAverageF1measureInPercent,  0.00000001);

        // micro average
        assertEquals(5, result.microAverageTruePositives,  0);
        assertEquals(3, result.microAverageFalsePositives,  0);
        assertEquals(3, result.microAverageTrueNegatives,  0);
        assertEquals(4, result.microAverageFalseNegatives,  0);


        assertEquals((8.0/15.0) * 100, result.microAverageAccuracyInPercent,  0.00000001);
        assertEquals((5.0/8.0) * 100, result.microAveragePrecisionInPercent,  0.00000001);
        assertEquals((5.0/9.0) * 100, result.microAverageRecallInPercent,  0.00000001);
        assertEquals(((2.0*(5.0/8.0)*(5.0/9.0)) / ((5.0/8.0)+(5.0/9.0))) * 100.0, result.microAverageF1measureInPercent, 0.00000001);

        // weighted average
        assertEquals((40.0/3 + 120.0/3), result.weightedOverallAccuracyInPercent,  0.00000001);
        assertEquals((50.0/3+(4.0/6.0)*200/3.0), result.weightedOverallPrecisionInPercent,  0.00000001);
        assertEquals((1.0/9.0) * 100 + (2.0/3.0) * (4.0/6.0) * 100.0, result.weightedOverallRecallInPercent,  0.00000001);
        assertEquals((40.0/3.0) + (2.0/3.0) * (2.0/3.0) * 100, result.weightedOverallF1MeasureInPercent, 0.00000001);

    }


    public static void copyTestFilesForEvaluateMappingsForOneWiki(){
        File sourceFile = new File(pathToRootDirectory + "/../evaluation_test_template/resourceMappings.ttl");
        File targetFile = new File(pathToRootDirectory + "/../evaluation_test/resourceMappings.ttl");
        try {
            org.apache.commons.io.FileUtils.copyFile(sourceFile, targetFile);
        } catch (Exception e){
            e.printStackTrace();

            // raise an assertion error
            assertTrue(false);
        }
    }

    @Test
    public void evaluateMappingsForOneWikiTest1() throws Exception {

        // case 1

        // copy required files
        copyTestFilesForEvaluateMappingsForOneWiki();

        EvaluationResultSingleWiki result = MappingEvaluator.evaluateMappingsForOneWiki("./src/test/test_files/evaluation_test", MappingEvaluator.EvaluationObjectSingleWiki.RESOURCES);
        assertTrue(result.getFalseNegatives() == 2);
        assertTrue(result.getFalsePositives() == 1);
        assertTrue(result.getTruePositives() == 1);
        assertTrue(result.getTrueNegatives() == 1);
        assertTrue(result.getPositives() == 3);
        assertTrue(result.getNegatives() == 2);
        assertTrue(result.getRecallInPercent() == (1.0/3) * 100);
        assertTrue(result.getPrecisionInPercent() == (1.0/2) * 100); // = 50
        assertTrue(result.getAccuracyInPercent() == (2.0/5) * 100);
        assertTrue(result.getF1MeasureInPercent() == ((2.0 * 0.5 * (1.0/3))/(0.5 + 1.0/3))*100); // = 40.0

    }


    @Test
    public void evaluateMappingsForOneWikiTest2() throws Exception {

        // case 2

        // copy required files
        copyTestFilesForEvaluateAllMappings();

        EvaluationResultSingleWiki result = MappingEvaluator.evaluateMappingsForOneWiki("./src/test/test_files/test_root/PostProcessedWikis/test_wiki_2", MappingEvaluator.EvaluationObjectSingleWiki.RESOURCES);
        assertTrue(result.getFalseNegatives() == 2);
        assertTrue(result.getFalsePositives() == 2);
        assertTrue(result.getTruePositives() == 4);
        assertTrue(result.getTrueNegatives() == 2);
        assertTrue(result.getPositives() == 6);
        assertTrue(result.getNegatives() == 4);
        assertTrue(result.getRecallInPercent() == (4.0/6) * 100);
        assertTrue(result.getPrecisionInPercent() == (4.0/6) * 100);
        assertTrue(result.getAccuracyInPercent() == (6.0/10) * 100);
        assertTrue(result.getF1MeasureInPercent() == ((2.0 * (4.0/6) * (4.0/6))/((4.0/6) + (4.0/6)))*100); // = ( 2.0 / 3 ) * 100 = 66.66666667

    }

}